package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.BookCapacitorBlockEntity.{SLOT_INPUT, SLOT_OUTPUT}
import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookCapacitorMenu
import com.yuuki1293.bookbook.common.register.{BlockEntities, MenuTypes}
import com.yuuki1293.bookbook.common.util.Ticked
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.{ClientGamePacketListener, ClientboundBlockEntityDataPacket}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BaseContainerBlockEntity, BlockEntityTicker}
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.{ContainerHelper, WorldlyContainer}
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.wrapper.SidedInvWrapper
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}

import scala.jdk.CollectionConverters._

class BookCapacitorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_CAPACITOR.get(), worldPosition, blockState)
    with WorldlyContainer with Ticked {

  protected var items: NonNullList[ItemStack] = NonNullList.withSize(2, ItemStack.EMPTY)

  private val capacity = 10000
  private val maxTransfer = 500

  val energyStorage: BookEnergyStorage = createEnergyStorage
  private val energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => energyStorage)
  protected val dataAccess: ContainerData = new ContainerData {
    /**
     * @param pIndex 0 - [[getEnergy]]<br> 1 - [[getMaxEnergy]]
     * @return
     */
    override def get(pIndex: Int): Int = {
      pIndex match {
        case 0 => getEnergy
        case 1 => getMaxEnergy
        case _ => throw new UnsupportedOperationException("Unable to get index: " + pIndex)
      }
    }

    /**
     * Invalid
     */
    @Deprecated
    override def set(pIndex: Int, pValue: Int): Unit = {
      throw new UnsupportedOperationException("Unable to get index: " + pIndex)
    }

    override def getCount: Int = 2
  }

  override def getContainerSize: Int = items.size()

  override def isEmpty: Boolean = {
    for (item <- items.asScala) {
      if (!item.isEmpty)
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

  override def canPlaceItem(pIndex: Int, pStack: ItemStack): Boolean = {
    pIndex match {
      case 0 => true
      case 1 => false
    }
  }

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
      super.getCapability(cap)
  }

  def getEnergy: Int = energyStorage.getEnergyStored

  def getMaxEnergy: Int = energyStorage.getMaxEnergyStored

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    energy.invalidate()
    for (handler <- handlers)
      handler.invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)
  }

  def outputEnergy(): Unit = {
    if (energyStorage.getEnergyStored >= maxTransfer && energyStorage.canExtract) {
      val item = items.get(SLOT_INPUT)
      if (item != null) {
        item.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage => {
          if (storage.getEnergyStored < storage.getMaxEnergyStored) {
            val toSend = energyStorage.extractEnergy(maxTransfer, simulate = false)
            val received = storage.receiveEnergy(toSend, false)

            energyStorage.setEnergy(
              energyStorage.getEnergyStored + toSend - received
            )
          }

          if (storage.getEnergyStored >= storage.getMaxEnergyStored && items.get(SLOT_OUTPUT).isEmpty) {
            items.set(SLOT_OUTPUT, item)
            items.remove(SLOT_INPUT)
          }
        })
      }

      for (direction <- Direction.values()) {
        val be = level.getBlockEntity(worldPosition.relative(direction))
        if (be != null) {
          be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite).ifPresent(storage => {
            if (be != this && storage.getEnergyStored < storage.getMaxEnergyStored) {
              val toSend = energyStorage.extractEnergy(maxTransfer, simulate = false)
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

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    items = NonNullList.withSize(getContainerSize, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(pTag, items)
    energyStorage.setEnergy(pTag.getInt("Energy"))
  }

  override protected def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("Energy", getEnergy)
    ContainerHelper.saveAllItems(pTag, items)
  }

  private def createEnergyStorage: BookEnergyStorage = {
    new BookEnergyStorage(this, capacity, maxTransfer)
  }

  override def getDefaultName: Component = new TranslatableComponent("container.bookbook.book_capacitor")

  override def createMenu(pContainerId: Int, pPlayerInventory: Inventory): AbstractContainerMenu = {
    new BookCapacitorMenu(MenuTypes.BOOK_CAPACITOR.get(), pContainerId, pPlayerInventory, this, dataAccess)
  }

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(SLOT_INPUT, SLOT_OUTPUT)

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = {
    pIndex match {
      case SLOT_INPUT => false
      case SLOT_OUTPUT => true
    }
  }

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = {
    pIndex match {
      case SLOT_INPUT => true
      case SLOT_OUTPUT => false
    }
  }

  override def getUpdatePacket: Packet[ClientGamePacketListener] = ClientboundBlockEntityDataPacket.create(this)

  def tick(): Unit = {
    outputEnergy()
  }
}

object BookCapacitorBlockEntity extends BlockEntityTicker[BookCapacitorBlockEntity] {
  final val SLOT_INPUT = 0
  final val SLOT_OUTPUT = 1
  final val DATA_ENERGY_STORED = 0
  final val DATA_MAX_ENERGY = 1

  override def tick(pLevel: Level, pPos: BlockPos, pState: BlockState, pBlockEntity: BookCapacitorBlockEntity): Unit = {
    pBlockEntity.outputEnergy()
  }
}