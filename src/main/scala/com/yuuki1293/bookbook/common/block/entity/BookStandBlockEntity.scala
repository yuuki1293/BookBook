package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.{ClientGamePacketListener, ClientboundBlockEntityDataPacket}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.{ContainerHelper, WorldlyContainer}

class BookStandBlockEntity(pPos: BlockPos, pState: BlockState)
  extends BlockEntity(BlockEntities.BOOK_STAND.get(), pPos, pState) with WorldlyContainer {
  private var items = NonNullList.withSize(1, ItemStack.EMPTY)

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(0)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = false

  override def getContainerSize: Int = items.size()

  override def isEmpty: Boolean = items.get(0).isEmpty

  @Deprecated
  override def getItem(pSlot: Int): ItemStack = items.get(pSlot)

  def getItem: ItemStack = items.get(0)

  def getItems: NonNullList[ItemStack] = items

  @Deprecated
  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = {
    val itemStack = ContainerHelper.removeItem(items, pSlot, pAmount)
    requestModelDataUpdate()
    itemStack
  }

  def removeItem(pAmount: Int): ItemStack = removeItem(0, pAmount)

  @Deprecated
  override def removeItemNoUpdate(pSlot: Int): ItemStack = ContainerHelper.takeItem(items, pSlot)

  def removeItemNoUpdate(): ItemStack = ContainerHelper.takeItem(items, 0)

  @Deprecated
  override def setItem(pSlot: Int, pStack: ItemStack): Unit = {
    items.set(pSlot, pStack)
    requestModelDataUpdate()
  }

  def setItem(pStack: ItemStack): Unit = setItem(0, pStack)

  override def stillValid(pPlayer: Player): Boolean = {
    if (level.getBlockEntity(worldPosition) != this) {
      false
    } else {
      pPlayer.distanceToSqr(worldPosition.getX.toDouble + 0.5D, worldPosition.getY.toDouble + 0.5D, worldPosition.getZ.toDouble + 0.5D) <= 64.0D
    }
  }

  override def clearContent(): Unit = {
    items.clear()
    requestModelDataUpdate()
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    items = NonNullList.withSize(getContainerSize, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(pTag, items)
    requestModelDataUpdate()
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    ContainerHelper.saveAllItems(pTag, items)
  }

  override def getUpdatePacket: Packet[ClientGamePacketListener] = ClientboundBlockEntityDataPacket.create(this)
}