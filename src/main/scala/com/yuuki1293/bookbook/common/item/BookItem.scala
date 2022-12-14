package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.entity.BookItemEntity
import net.minecraft.world.entity.{Entity, EntityType}
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraft.world.level.Level

class BookItem(properties: Item.Properties) extends Item(properties) {
  override def hasCustomEntity(stack: ItemStack) = true

  override def createEntity(level: Level, location: Entity, stack: ItemStack): Entity = {
    val itemBook = BookItemEntity(EntityType.ITEM, level, location.getX, location.getY, location.getZ, stack)
    itemBook.setDeltaMovement(location.getDeltaMovement)
    itemBook.setPickUpDelay(40)

    itemBook
  }
}

object BookItem {
  def apply(properties: Item.Properties) = new BookItem(properties)
}