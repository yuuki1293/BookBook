package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.entity.EntityBook
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraft.world.level.Level

class ItemBook(pProperties: Item.Properties) extends Item(pProperties) {
  override def createEntity(level: Level, location: Entity, stack: ItemStack): Entity = {
    val entity = new EntityBook(level, location.getX, location.getY, location.getZ, stack)

    entity.setDeltaMovement(location.getDeltaMovement)

    entity.setPickUpDelay(40)

    entity
  }
}
