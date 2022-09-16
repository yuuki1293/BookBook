package com.yuuki1293.bookbook.common.entity

import com.yuuki1293.bookbook.common.item.{ItemBook, ItemDrownedBook}
import com.yuuki1293.bookbook.common.register.RegisterItem
import net.minecraft.network.protocol.Packet
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.network.NetworkHooks

class EntityItemBook(entityType: EntityType[_ <: ItemEntity], level: Level) extends ItemEntity(entityType, level) {
  def this(entityType: EntityType[_ <: ItemEntity], level: Level, posX: Double, posY: Double, posZ: Double, itemStack: ItemStack) = {
    this(entityType, level)
    this.setPos(posX, posY, posZ)
    this.setItem(itemStack)
    this.lifespan = itemStack.getEntityLifespan(level)
  }

  override def getAddEntityPacket: Packet[_] = {
    NetworkHooks.getEntitySpawningPacket(this)
  }

  override def tick(): Unit = {
    super.tick()

    if (this.isInWater) {
      val drownedBook = new ItemStack(RegisterItem.DROWNED_BOOK.get().asItem())

      drownedBook.setTag(this.getItem.getTag)
      drownedBook.setCount(this.getItem.getCount)
      drownedBook.setEntityRepresentation(this.getItem.getEntityRepresentation)
      drownedBook.setPopTime(this.getItem.getPopTime)

      this.setItem(drownedBook)
    }
  }
}
