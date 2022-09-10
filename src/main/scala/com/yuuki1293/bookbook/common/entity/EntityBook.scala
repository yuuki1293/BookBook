package com.yuuki1293.bookbook.common.entity

import com.yuuki1293.bookbook.common.BookBook
import net.minecraft.nbt.StringTag
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.apache.logging.log4j.{LogManager, Logger}

class EntityBook(pLevel: Level, pPosX: Double, pPosY: Double, pPosZ: Double, pItemStack: ItemStack)
  extends ItemEntity(pLevel, pPosX, pPosY, pPosZ, pItemStack) {

  val LOGGER: Logger = LogManager.getLogger(BookBook.MODID)

  this.setPos(pPosX, pPosY, pPosZ)
  this.setItem(pItemStack)

  override def tick(): Unit = {
    super.tick()
    LOGGER.debug("ﾃｨｯｸﾃｨｯｸ")
    if (this.isInWater) {
      val name = StringTag.valueOf("[\"\",{\"text\":\"b\",\"obfuscated\":true,\"color\":\"black\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_blue\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_green\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_aqua\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_red\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_purple\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"gold\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"gray\"},{\"text\":\"\\u672c\\u304c\\u6eba\\u308c\\u305f\\uff8c\\uff9e\\uff6f\\uff78\\uff8c\\uff9e\\uff6f\\uff78\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_gray\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"blue\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"green\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"aqua\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"red\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"light_purple\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"yellow\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"white\"}]")

      this.setCustomNameVisible(true)

      val nbt = this.serializeNBT().copy()
      nbt.put("CustomName", name)
      this.deserializeNBT(nbt)
    }
  }
}
