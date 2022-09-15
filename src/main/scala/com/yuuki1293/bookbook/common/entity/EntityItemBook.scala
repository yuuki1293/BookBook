package com.yuuki1293.bookbook.common.entity

import net.minecraft.network.chat.TextComponent
import net.minecraft.network.protocol.Packet
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.network.NetworkHooks

class EntityItemBook(entityType: EntityType[_ <: ItemEntity], level: Level) extends ItemEntity(entityType, level) {
  setDrawn(false)

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
      setDrawn(true)
    }
  }

  def isDrawn(): Boolean = {
    val nbt = this.serializeNBT()

    nbt.getBoolean("drawn")
  }

  def setDrawn(drawn: Boolean): Unit ={
    val nbt = this.serializeNBT()

    nbt.putBoolean("drawn", drawn)

    this.deserializeNBT(nbt)
  }
}
