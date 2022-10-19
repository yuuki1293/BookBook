package com.yuuki1293.bookbook.common.block.entity

import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.world._
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.extensions.IForgeBlockEntity

import scala.jdk.CollectionConverters._

trait IBookContainerBlockEntity
  extends IForgeBlockEntity with Container with MenuProvider with Nameable with WorldlyContainer {
  protected var items: NonNullList[ItemStack]

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

  override def clearContent(): Unit = items.clear()

  def load(pTag: CompoundTag): Unit ={
    items = NonNullList.withSize(getContainerSize, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(pTag, items)
  }

  protected def saveAdditional(pTag: CompoundTag): Unit = {
    ContainerHelper.saveAllItems(pTag, items)
  }
}
