package com.yuuki1293.bookbook

import net.minecraft.nbt._
import net.minecraft.world.entity.item.ItemEntity
import net.minecraftforge.event.TickEvent.WorldTickEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

import scala.collection.mutable

@Mod.EventBusSubscriber
class Main {
  var books: mutable.Set[ItemEntity] = mutable.Set[ItemEntity]()

  @SubscribeEvent
  def tossBook(event: ItemTossEvent): Unit = {
    books.add(event.getEntityItem)
  }

  @SubscribeEvent
  def worldTickEvent(event: WorldTickEvent): Unit = {
    books.map(entity => {
      if (entity.isAlive) {
        if (entity.isInWater) {
          val name = StringTag.valueOf("[\"\",{\"text\":\"b\",\"obfuscated\":true,\"color\":\"black\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_blue\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_green\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_aqua\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_red\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_purple\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"gold\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"gray\"},{\"text\":\"\\u672c\\u304c\\u6eba\\u308c\\u305f\\uff8c\\uff9e\\uff6f\\uff78\\uff8c\\uff9e\\uff6f\\uff78\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"dark_gray\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"blue\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"green\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"aqua\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"red\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"light_purple\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"yellow\"},{\"text\":\"b\",\"obfuscated\":true,\"color\":\"white\"}]")

          entity.setCustomNameVisible(true)

          val nbt = entity.serializeNBT().copy()
          nbt.put("CustomName", name)
          entity.deserializeNBT(nbt)
        }
        false
      } else true
    })
  }
}