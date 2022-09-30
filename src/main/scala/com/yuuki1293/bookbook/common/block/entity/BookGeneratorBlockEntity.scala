package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity.SLOT_FUEL
import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookFurnaceMenu
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData, SimpleContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity.setChanged
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.{ContainerHelper, SimpleContainer, WorldlyContainer}
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}
import net.minecraftforge.items.wrapper.SidedInvWrapper

class BookGeneratorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_GENERATOR.get(), worldPosition, blockState) with WorldlyContainer {

  protected var items: NonNullList[ItemStack] = NonNullList.withSize(1, ItemStack.EMPTY)
  val energyStorage: BookEnergyStorage = createEnergyStorage()

  private val capacity = 1000
  private val maxExtract = 100
  private var burnTime = 0
  private var burnDuration = 0
  private var progress = 0
  private var maxProgress = 0
  private val energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => this.energyStorage)
  protected val dataAccess: ContainerData = new ContainerData() {
    override def get(pIndex: Int): Int = {
      pIndex match {
        case 0 => burnTime
        case 1 => burnDuration
        case _ => 0
      }
    }

    override def set(pIndex: Int, pValue: Int): Unit = {
      pIndex match {
        case 0 => burnTime = pValue
        case 1 => burnDuration = pValue
      }
    }

    override def getCount: Int = 2
  }

  private def isBurn = this.burnTime > 0

  private def canBurn = getEnergy < this.energyStorage.getMaxEnergyStored

  protected def getBurnDuration(stack: ItemStack): Int = {
    if (stack.isEmpty)
      0
    else
      ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)
  }

  override def getContainerSize: Int = this.items.size()

  override def isEmpty: Boolean = {
    for (itemStack: ItemStack <- this.items.toArray) {
      if (!itemStack.isEmpty)
        return false
    }
    true
  }

  override def getItem(pSlot: Int): ItemStack = this.items.get(pSlot)

  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ContainerHelper.removeItem(this.items, pSlot, pAmount)

  override def removeItemNoUpdate(pSlot: Int): ItemStack = ContainerHelper.takeItem(this.items, pSlot)

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = {
    this.items.set(pSlot, pStack)
    if (pStack.getCount > this.getMaxStackSize) {
      pStack.setCount(this.getMaxStackSize)
    }
  }

  override def stillValid(pPlayer: Player): Boolean = {
    if (this.level.getBlockEntity(this.worldPosition) != this) {
      false
    } else {
      pPlayer.distanceToSqr(this.worldPosition.getX.toDouble + 0.5D, this.worldPosition.getY.toDouble + 0.5D, this.worldPosition.getZ.toDouble + 0.5D) <= 64.0D
    }
  }

  override def canPlaceItem(pIndex: Int, pStack: ItemStack): Boolean = ForgeHooks.getBurnTime(pStack, RecipeType.SMELTING) > 0

  override def clearContent(): Unit = this.items.clear()

  var handlers: Array[LazyOptional[IItemHandlerModifiable]] = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityEnergy.ENERGY)
      this.energy.cast()
    else if (!this.remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (side == Direction.UP)
        handlers(0).cast()
      else if (side == Direction.DOWN)
        handlers(1).cast()
      else
        handlers(2).cast()
    }
    else
      super.getCapability(cap, side)
  }

  def getEnergy: Int = this.energyStorage.getEnergyStored

  def getEnergyForStack(itemStack: ItemStack): Int = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING)

  def getMaxProgress: Int = this.maxProgress

  def getProgress: Int = this.progress

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    this.energy.invalidate()
    for (x <- handlers.indices)
      handlers(x).invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    this.items = NonNullList.withSize(this.getContainerSize, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(pTag, this.items)
    this.burnTime = pTag.getInt("BurnTime")
    this.burnDuration = this.getBurnDuration(this.items.get(0))
    this.energyStorage.setEnergy(pTag.getInt("Energy"))
  }

  def outputEnergy(): Unit = {
    if (this.energyStorage.getEnergyStored >= this.maxExtract && this.energyStorage.canExtract) {
      for (direction <- Direction.values()) {
        val be = this.level.getBlockEntity(this.worldPosition.relative(direction))
        if (be != null) {
          be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite).ifPresent(storage => {
            if (be != this && storage.getEnergyStored < storage.getMaxEnergyStored) {
              val toSend = BookGeneratorBlockEntity.this.energyStorage.extractEnergy(this.maxExtract, simulate = false)
              val received = storage.receiveEnergy(toSend, false)

              BookGeneratorBlockEntity.this.energyStorage.setEnergy(
                BookGeneratorBlockEntity.this.energyStorage.getEnergyStored + toSend - received
              )
            }
          })
        }
      }
    }
  }

  override protected def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("BurnTime", this.burnTime)
    pTag.putInt("Energy", getEnergy)
    ContainerHelper.saveAllItems(pTag, this.items)
  }

  private def createEnergyStorage() = {
    new BookEnergyStorage(this, this.capacity, 0, this.maxExtract, 0)
  }

  override def getDefaultName: Component = new TranslatableComponent("container.book_generator")

  override def createMenu(pContainerId: Int, pPlayerInventory: Inventory): AbstractContainerMenu = {
    new BookFurnaceMenu(pContainerId, pPlayerInventory, new SimpleContainer(1), new SimpleContainerData(2))
  }

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = false

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(SLOT_FUEL)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true
}

object BookGeneratorBlockEntity {
  protected val SLOT_FUEL = 0
  val DATA_LIT_TIME = 0
  val DATA_LIT_DURATION = 1
  val NUM_DATA_VALUES = 2
  val BURN_TIME_STANDARD = 200

  def serverTick(pLevel: Level, pPos: BlockPos, pState: BlockState, pBlockEntity: BookGeneratorBlockEntity): Unit = {
    var flag = pBlockEntity.isBurn
    var flag1 = false

    val itemStack = pBlockEntity.items.get(SLOT_FUEL)
    if (pBlockEntity.isBurn) {
      pBlockEntity.burnTime -= 1
      pBlockEntity.energyStorage.receiveEnergy(10, simulate = false)
    } else if (!itemStack.isEmpty) {
      val i = pBlockEntity.getMaxStackSize
      if (pBlockEntity.canBurn) {
        pBlockEntity.burnTime = pBlockEntity.getBurnDuration(itemStack)
        pBlockEntity.burnDuration = pBlockEntity.burnTime
        if (pBlockEntity.isBurn) {
          flag1 = true
          if (itemStack.hasContainerItem)
            pBlockEntity.items.set(SLOT_FUEL, itemStack.getContainerItem)
          else if (!itemStack.isEmpty) {
            itemStack.shrink(1)
            if (itemStack.isEmpty) {
              pBlockEntity.items.set(SLOT_FUEL, itemStack.getContainerItem)
            }
          }
        }
      }
    }

    if (flag != pBlockEntity.isBurn) {
      flag1 = true
      pState.setValue(BlockStateProperties.LIT, java.lang.Boolean.valueOf(pBlockEntity.isBurn))
      pLevel.setBlock(pPos, pState, 3)
    }

    if (flag1)
      setChanged(pLevel, pPos, pState)
  }
}