package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState

class BookStandBlockEntity(pPos: BlockPos, pState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_STAND.get(), pPos, pState) with WorldlyContainer {
  private var item = ItemStack.EMPTY

  override def getDefaultName: Component = new TranslatableComponent("container.bookbook.book_stand")

  override def createMenu(pContainerId: Int, pInventory: Inventory): AbstractContainerMenu = ???

  override def getSlotsForFace(pSide: Direction): Array[Int] = ???

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = ???

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = ???

  override def getContainerSize: Int = ???

  override def isEmpty: Boolean = ???

  override def getItem(pSlot: Int): ItemStack = ???

  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ???

  override def removeItemNoUpdate(pSlot: Int): ItemStack = ???

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = ???

  override def stillValid(pPlayer: Player): Boolean = ???

  override def clearContent(): Unit = ???
}
