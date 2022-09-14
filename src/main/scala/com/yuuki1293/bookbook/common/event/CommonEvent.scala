package com.yuuki1293.bookbook.common.event

import com.yuuki1293.bookbook.common.entity.EntityItemBook
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.BookItem
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.{LogManager, Logger}

@Mod.EventBusSubscriber
class CommonEvent {
  val LOGGER: Logger = LogManager.getLogger("bookbook")

  @SubscribeEvent
  def entitySpawnEvent(event: EntityJoinWorldEvent): Unit = {
    event.getEntity match {
      case itemEntity: ItemEntity =>
        if (itemEntity.getItem.getItem.isInstanceOf[BookItem]) {
          LOGGER.info("spawn BookItem")
          val entityItemBook = new EntityItemBook(EntityType.ITEM, itemEntity.level, itemEntity.getX, itemEntity.getY, itemEntity.getZ, itemEntity.getItem)
          itemEntity.getItem.getItem.createEntity(itemEntity.level, entityItemBook, itemEntity.getItem)

          event.setCanceled(true)
        }
      case _ =>
    }
  }
}
