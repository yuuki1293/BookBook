package com.yuuki1293.bookbook.common.entity

import com.yuuki1293.bookbook.common.register.Items
import net.minecraft.nbt.StringTag
import net.minecraft.network.protocol.Packet
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.network.NetworkHooks

class ItemBookEntity(entityType: EntityType[_ <: ItemEntity], level: Level) extends ItemEntity(entityType, level) {
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
      val drownedBook = new ItemStack(Items.DROWNED_BOOK.get().asItem())

      drownedBook.setTag(this.getItem.getTag)
      drownedBook.setCount(this.getItem.getCount)
      drownedBook.setEntityRepresentation(this.getItem.getEntityRepresentation)
      drownedBook.setPopTime(this.getItem.getPopTime)

      this.setItem(drownedBook)


      val name = StringTag.valueOf("[\"\",{\"text\":\"b\",\"obfuscated\":true,\"color\":\"black\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_blue\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_green\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_aqua\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_red\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_purple\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"gold\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"gray\"},{\"text\":\"\\u672c\\u304c\\u6eba\\u308c\\u305f\\uff8c\\uff9e\\uff6f\\uff78\\uff8c\\uff9e\\uff6f\\uff78\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_gray\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"blue\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"green\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"aqua\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"red\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"light_purple\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"yellow\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"white\"}]")

      this.setCustomNameVisible(true)

      val nbt = this.serializeNBT().copy()
      nbt.put("CustomName", name)
      this.deserializeNBT(nbt)
    }
  }
}
