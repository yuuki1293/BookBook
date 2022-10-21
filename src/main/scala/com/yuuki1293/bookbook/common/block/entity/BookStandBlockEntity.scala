package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.{ClientGamePacketListener, ClientboundBlockEntityDataPacket}
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState

class BookStandBlockEntity(pPos: BlockPos, pState: BlockState)
  extends BaseBookContainerBlockEntity(BlockEntities.BOOK_STAND.get(), pPos, pState) {
  override protected var items: NonNullList[ItemStack] = NonNullList.withSize(1, ItemStack.EMPTY)
  private var oldItem = ItemStack.EMPTY

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(0)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = false

  def getItem: ItemStack = items.get(0)

  def removeItem(pAmount: Int): ItemStack = removeItem(0, pAmount)

  def removeItemNoUpdate(): ItemStack = ContainerHelper.takeItem(items, 0)

  def setItem(pStack: ItemStack): Unit = setItem(0, pStack)

  private def isItemChanged: Boolean = {
    if (getItem.isEmpty && oldItem.isEmpty)
      return false
    if (getItem.getItem == oldItem.getItem)
      return false
    true
  }

  override def getUpdatePacket: Packet[ClientGamePacketListener] = ClientboundBlockEntityDataPacket.create(this)

  override def getUpdateTag: CompoundTag = {
    val tag = new CompoundTag()
    ContainerHelper.saveAllItems(tag, this.items, true)
    tag
  }

  override def stillValid(pPlayer: Player): Boolean = false

  override def getDefaultName: Component = null

  override def createMenu(pContainerId: Int, pInventory: Inventory): AbstractContainerMenu = null
}

object BookStandBlockEntity extends BlockEntityTicker[BookStandBlockEntity] {
  def apply(worldPosition: BlockPos, blockState: BlockState) =
    new BookStandBlockEntity(worldPosition, blockState)

  override def tick(pLevel: Level, pPos: BlockPos, pState: BlockState, pBlockEntity: BookStandBlockEntity): Unit = {
    if (pBlockEntity.isItemChanged) {
      pBlockEntity.setChanged()
      pLevel.sendBlockUpdated(pPos, pState, pState, 2)
      pBlockEntity.oldItem = pBlockEntity.getItem
    }
  }
}