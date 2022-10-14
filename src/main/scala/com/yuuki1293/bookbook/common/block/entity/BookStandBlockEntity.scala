package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.world.{ContainerHelper, WorldlyContainer}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

import scala.annotation.unused

class BookStandBlockEntity(pPos: BlockPos, pState: BlockState)
  extends BlockEntity(BlockEntities.BOOK_STAND.get(), pPos, pState) with WorldlyContainer {
  private val items = NonNullList.withSize(1, ItemStack.EMPTY)

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(0)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = false

  override def getContainerSize: Int = 1

  override def isEmpty: Boolean = items.get(0).isEmpty

  @Deprecated
  override def getItem(pSlot: Int): ItemStack = items.get(pSlot)

  def getItem: ItemStack = items.get(0)

  @Deprecated
  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ContainerHelper.removeItem(items, pSlot, pAmount)

  def removeItem(pAmount: Int): ItemStack = ContainerHelper.removeItem(items, 0, pAmount)

  @Deprecated
  override def removeItemNoUpdate(pSlot: Int): ItemStack = ContainerHelper.takeItem(items, pSlot)

  def removeItemNoUpdate(): ItemStack = ContainerHelper.takeItem(items, 0)

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = ???

  override def stillValid(pPlayer: Player): Boolean = ???

  override def clearContent(): Unit = ???
}
