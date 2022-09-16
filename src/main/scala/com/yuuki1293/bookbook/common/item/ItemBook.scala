package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.entity.EntityItemBook
import com.yuuki1293.bookbook.common.item.ItemBook.set
import com.yuuki1293.bookbook.common.register.RegisterItem
import net.minecraft.core.NonNullList
import net.minecraft.world.entity.{Entity, EntityType}
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack}
import net.minecraft.world.level.Level

import java.util

class ItemBook(properties: Properties) extends Item(set(properties)) {

  def this() = this(new Properties)

  override def hasCustomEntity(stack: ItemStack) = true

  override def createEntity(level: Level, location: Entity, stack: ItemStack): Entity = {
    val itemBook = new EntityItemBook(EntityType.ITEM, level, location.getX, location.getY, location.getZ, stack)
    itemBook.setDeltaMovement(location.getDeltaMovement)
    itemBook.setPickUpDelay(40)

    itemBook
  }

  override def fillItemCategory(pCategory: CreativeModeTab, pItems: NonNullList[ItemStack]): Unit = {
    if (allowdedIn(pCategory)) {
      pItems.addAll(createBooks)
    }
  }

  private def createBooks: java.util.Collection[_ <: ItemStack] = {
    val books = new util.ArrayList[ItemStack]()

    val item = RegisterItem.BOOK.get().asItem()

    val drown = new ItemStack(item)
    ItemBook.setDrawn(drown, drown = true)
    books.add(drown)

    val not_drown = new ItemStack(item)
    ItemBook.setDrawn(not_drown, drown = false)
    books.add(not_drown)

    books
  }
}

object ItemBook {
  def set(p: Properties): Properties = {
    p.tab(CreativeModeTab.TAB_MATERIALS)
  }

  def isDrawn(itemStack: ItemStack): Boolean = {
    itemStack.getOrCreateTag().getBoolean("Drown")
  }

  def setDrawn(itemStack: ItemStack, drown: Boolean): Unit = {
    itemStack.getOrCreateTag().putBoolean("Drown", drown)
  }
}
