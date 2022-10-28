package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.BookCapacitorBlockEntity.{DATA_ENERGY_STORED, DATA_MAX_ENERGY, SLOT_INPUT, SLOT_OUTPUT}
import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookCapacitorMenu
import com.yuuki1293.bookbook.common.register.{BlockEntities, MenuTypes}
import com.yuuki1293.bookbook.common.util.ContainerUtil
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.{ClientGamePacketListener, ClientboundBlockEntityDataPacket}
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy

class BookCapacitorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseBookContainerBlockEntity(BlockEntities.BOOK_CAPACITOR.get(), worldPosition, blockState) {
  override var items: NonNullList[ItemStack] = NonNullList.withSize(2, ItemStack.EMPTY)

  private final val capacity = 10000
  private final val maxTransfer = 500

  val energyStorage: BookEnergyStorage = BookEnergyStorage(capacity, maxTransfer)(this)
  private var energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => energyStorage)
  protected val dataAccess: ContainerData = new ContainerData {
    /**
     * @param pIndex 0 - [[getEnergy]]<br> 1 - [[getMaxEnergy]]
     * @return
     */
    override def get(pIndex: Int): Int = {
      pIndex match {
        case DATA_ENERGY_STORED => getEnergy
        case DATA_MAX_ENERGY => getMaxEnergy
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

  override def canPlaceItem(pIndex: Int, pStack: ItemStack): Boolean = {
    pIndex match {
      case 0 => true
      case 1 => false
    }
  }

  override def getCapability[A](cap: Capability[A], side: Direction): LazyOptional[A] = {
    if (cap == CapabilityEnergy.ENERGY)
      energy.cast()
    else
      super.getCapability(cap, side)
  }

  def getEnergy: Int = energyStorage.getEnergyStored

  def getMaxEnergy: Int = energyStorage.getMaxEnergyStored

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    energy.invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    energy = LazyOptional.of(() => energyStorage)
  }

  def outputEnergy(): Unit =
    energyStorage.chargeItems(this, 0, 1)

  val item: ItemStack = items.get(SLOT_INPUT)
  item.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage =>
    if (storage.getEnergyStored >= storage.getMaxEnergyStored
      && ContainerUtil.canPlace(item, this, SLOT_OUTPUT)) {
      ContainerUtil.place(item, this, SLOT_OUTPUT)
      items.remove(SLOT_INPUT)
    })
  energyStorage.outputEnergy()

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    energyStorage.setEnergy(pTag.getInt("Energy"))
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("Energy", getEnergy)
  }

  override def getDefaultName: Component =
    new TranslatableComponent("container.bookbook.book_capacitor")

  override def createMenu(pContainerId: Int, pPlayerInventory: Inventory): AbstractContainerMenu = {
    BookCapacitorMenu(
      MenuTypes.BOOK_CAPACITOR.get(),
      pContainerId,
      pPlayerInventory,
      this,
      dataAccess)
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

  override def getUpdatePacket: Packet[ClientGamePacketListener] =
    ClientboundBlockEntityDataPacket.create(this)
}

object BookCapacitorBlockEntity extends BlockEntityTicker[BookCapacitorBlockEntity] {
  final val SLOT_INPUT = 0
  final val SLOT_OUTPUT = 1
  final val DATA_ENERGY_STORED = 0
  final val DATA_MAX_ENERGY = 1

  def apply(worldPosition: BlockPos, blockState: BlockState) =
    new BookCapacitorBlockEntity(worldPosition, blockState)

  override def tick(pLevel: Level, pPos: BlockPos, pState: BlockState, pBlockEntity: BookCapacitorBlockEntity): Unit = {
    pBlockEntity.outputEnergy()
  }
}