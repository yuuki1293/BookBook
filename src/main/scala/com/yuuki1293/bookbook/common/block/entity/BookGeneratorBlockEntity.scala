package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.BookGeneratorBlock
import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity.SLOT_FUEL
import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookGeneratorMenu
import com.yuuki1293.bookbook.common.register.{BlockEntities, MenuTypes}
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.{ClientGamePacketListener, ClientboundBlockEntityDataPacket}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.{ContainerHelper, WorldlyContainer}
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.wrapper.SidedInvWrapper
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}

class BookGeneratorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_GENERATOR.get(), worldPosition, blockState) with WorldlyContainer {

  protected var items: NonNullList[ItemStack] = NonNullList.withSize(1, ItemStack.EMPTY)

  private val capacity = 5000
  private val maxExtract = 100
  private var burnTime = 0
  private var burnDuration = 0

  val energyStorage: BookEnergyStorage = createEnergyStorage
  private val energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => energyStorage)
  protected val dataAccess: ContainerData = new ContainerData() {
    /**
     * @param pIndex 0 - [[burnTime]]<br> 1 - [[burnDuration]]<br> 2 - [[getEnergy]]<br> 3 - [[getMaxEnergy]]<br> others - [[UnsupportedOperationException]]
     * @return data
     */
    override def get(pIndex: Int): Int = {
      pIndex match {
        case 0 => burnTime
        case 1 => burnDuration
        case 2 => getEnergy
        case 3 => getMaxEnergy
        case _ => throw new UnsupportedOperationException("Unable to get index: '" + pIndex)
      }
    }

    /**
     *
     * @param pIndex 0 - [[burnTime]]<br> 1 - [[burnDuration]]<br> others - [[UnsupportedOperationException]]
     * @param pValue Assigned to pIndex
     */
    override def set(pIndex: Int, pValue: Int): Unit = {
      pIndex match {
        case 0 => burnTime = pValue
        case 1 => burnDuration = pValue
        case _ => throw new UnsupportedOperationException("Unable to get index: '" + pIndex)
      }
    }

    override def getCount: Int = 4
  }

  private def isBurn = burnTime > 0

  private def canBurn = getEnergy < getMaxEnergy

  protected def getBurnDuration(stack: ItemStack): Int = {
    if (stack.isEmpty)
      0
    else
      ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)
  }

  override def getContainerSize: Int = items.size()

  override def isEmpty: Boolean = {
    for (i <- 0 until items.size()) {
      if (!items.get(i).isEmpty)
        return false
    }
    true
  }

  override def getItem(pSlot: Int): ItemStack = items.get(pSlot)

  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ContainerHelper.removeItem(items, pSlot, pAmount)

  override def removeItemNoUpdate(pSlot: Int): ItemStack = ContainerHelper.takeItem(items, pSlot)

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = {
    items.set(pSlot, pStack)
    if (pStack.getCount > getMaxStackSize) {
      pStack.setCount(getMaxStackSize)
    }
  }

  override def stillValid(pPlayer: Player): Boolean = {
    if (level.getBlockEntity(worldPosition) != this) {
      false
    } else {
      pPlayer.distanceToSqr(worldPosition.getX.toDouble + 0.5D, worldPosition.getY.toDouble + 0.5D, worldPosition.getZ.toDouble + 0.5D) <= 64.0D
    }
  }

  override def canPlaceItem(pIndex: Int, pStack: ItemStack): Boolean = ForgeHooks.getBurnTime(pStack, RecipeType.SMELTING) > 0

  override def clearContent(): Unit = items.clear()

  var handlers: Array[LazyOptional[IItemHandlerModifiable]] = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)

  override def getCapability[A](cap: Capability[A], side: Direction): LazyOptional[A] = {
    if (cap == CapabilityEnergy.ENERGY)
      energy.cast()
    else if (!remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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

  def getEnergy: Int = energyStorage.getEnergyStored

  def getMaxEnergy: Int = energyStorage.getMaxEnergyStored

  def getEnergyForStack(itemStack: ItemStack): Int = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING)

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    energy.invalidate()
    for (x <- handlers.indices)
      handlers(x).invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    items = NonNullList.withSize(getContainerSize, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(pTag, items)
    burnTime = pTag.getInt("BurnTime")
    burnDuration = getBurnDuration(items.get(SLOT_FUEL))
    energyStorage.setEnergy(pTag.getInt("Energy"))
  }

  def outputEnergy(): Unit = {
    if (energyStorage.getEnergyStored >= maxExtract && energyStorage.canExtract) {
      for (direction <- Direction.values()) {
        val be = level.getBlockEntity(worldPosition.relative(direction))
        if (be != null) {
          be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite).ifPresent(storage => {
            if (be != this && storage.getEnergyStored < storage.getMaxEnergyStored) {
              val toSend = energyStorage.extractEnergy(maxExtract, simulate = false)
              val received = storage.receiveEnergy(toSend, false)

              energyStorage.setEnergy(
                energyStorage.getEnergyStored + toSend - received
              )
            }
          })
        }
      }
    }
  }

  override protected def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("BurnTime", burnTime)
    pTag.putInt("Energy", getEnergy)
    ContainerHelper.saveAllItems(pTag, items)
  }

  private def createEnergyStorage: BookEnergyStorage = {
    new BookEnergyStorage(this, capacity, 0, maxExtract, 0)
  }

  override def getDefaultName: Component = new TranslatableComponent("container.book_generator")

  override def createMenu(pContainerId: Int, pPlayerInventory: Inventory): AbstractContainerMenu = {
    new BookGeneratorMenu(MenuTypes.BOOK_GENERATOR.get(), pContainerId, pPlayerInventory, this, dataAccess)
  }

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = false

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(SLOT_FUEL)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true

  def isFuel(itemStack: ItemStack): Boolean = getBurnDuration(itemStack) > 0

  override def getUpdatePacket: Packet[ClientGamePacketListener] = ClientboundBlockEntityDataPacket.create(this)

  def tick(): Unit = {
    val burnFlag = isBurn

    if (canBurn) {
      if (isFuel(items.get(SLOT_FUEL)) && !isBurn) {
        burnDuration = getEnergyForStack(items.get(SLOT_FUEL))
        burnTime = burnDuration
        items.get(SLOT_FUEL).shrink(1)
      } else if (isBurn) {
        burnTime -= 1
        energyStorage.increase(10)
      } else {
        burnTime = 0
        burnDuration = 0
      }
    }

    if (burnFlag != isBurn) {
      val state = blockState.setValue(BookGeneratorBlock.LIT, java.lang.Boolean.valueOf(isBurn))
      level.setBlock(worldPosition, state, 3)
    }

    outputEnergy()
  }
}

object BookGeneratorBlockEntity {
  final val SLOT_FUEL = 0
  final val DATA_BURN_TIME = 0
  final val DATA_BURN_DURATION = 1
  final val DATA_ENERGY_STORED = 2
  final val DATA_MAX_ENERGY = 3
}