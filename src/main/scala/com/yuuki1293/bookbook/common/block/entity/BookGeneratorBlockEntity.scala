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
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy

class BookGeneratorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseBookContainerBlockEntity(BlockEntities.BOOK_GENERATOR.get(), worldPosition, blockState) {

  override var items: NonNullList[ItemStack] = NonNullList.withSize(1, ItemStack.EMPTY)

  private val capacity = 5000
  private val maxExtract = 100
  private var burnTime = 0
  private var burnDuration = 0

  val energyStorage: BookEnergyStorage = createEnergyStorage
  private var energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => energyStorage)
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

  override def canPlaceItem(pIndex: Int, pStack: ItemStack): Boolean = ForgeHooks.getBurnTime(pStack, RecipeType.SMELTING) > 0

  override def getCapability[A](cap: Capability[A], side: Direction): LazyOptional[A] = {
    if (cap == CapabilityEnergy.ENERGY)
      energy.cast()
    else
      super.getCapability(cap, side)
  }

  def getEnergy: Int = energyStorage.getEnergyStored

  def getMaxEnergy: Int = energyStorage.getMaxEnergyStored

  def getEnergyForStack(itemStack: ItemStack): Int = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING)

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    energy.invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    energy = LazyOptional.of(() => energyStorage)
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    burnTime = pTag.getInt("BurnTime")
    burnDuration = getBurnDuration(items.get(SLOT_FUEL))
    energyStorage.setEnergy(pTag.getInt("Energy"))
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("BurnTime", burnTime)
    pTag.putInt("Energy", getEnergy)
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

  private def createEnergyStorage: BookEnergyStorage = {
    new BookEnergyStorage(this, capacity, 0, maxExtract, 0)
  }

  override def getDefaultName: Component = new TranslatableComponent("container.bookbook.book_generator")

  override def createMenu(pContainerId: Int, pPlayerInventory: Inventory): AbstractContainerMenu = {
    new BookGeneratorMenu(MenuTypes.BOOK_GENERATOR.get(), pContainerId, pPlayerInventory, this, dataAccess)
  }

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = false

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(SLOT_FUEL)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true

  def isFuel(itemStack: ItemStack): Boolean = getBurnDuration(itemStack) > 0

  override def getUpdatePacket: Packet[ClientGamePacketListener] = ClientboundBlockEntityDataPacket.create(this)
}

object BookGeneratorBlockEntity extends BlockEntityTicker[BookGeneratorBlockEntity] {
  final val SLOT_FUEL = 0
  final val DATA_BURN_TIME = 0
  final val DATA_BURN_DURATION = 1
  final val DATA_ENERGY_STORED = 2
  final val DATA_MAX_ENERGY = 3

  override def tick(pLevel: Level, pPos: BlockPos, pState: BlockState, pBlockEntity: BookGeneratorBlockEntity): Unit = {
    val be = pBlockEntity

    val burnFlag = be.isBurn

    if (be.canBurn) {
      if (be.isFuel(be.items.get(SLOT_FUEL)) && !be.isBurn) {
        be.burnDuration = be.getEnergyForStack(be.items.get(SLOT_FUEL))
        be.burnTime = be.burnDuration
        be.items.get(SLOT_FUEL).shrink(1)
      } else if (be.isBurn) {
        be.burnTime -= 1
        be.energyStorage.increase(10)
      } else {
        be.burnTime = 0
        be.burnDuration = 0
      }
    }

    if (burnFlag != be.isBurn) {
      val state = pState.setValue(BookGeneratorBlock.LIT, java.lang.Boolean.valueOf(be.isBurn))
      be.getLevel.setBlock(be.getBlockPos, state, 3)
    }

    be.outputEnergy()
  }
}