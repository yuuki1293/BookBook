package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block.entity.BlockEntityBookFurnace
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object BlockEntities {
  val BLOCK_ENTITIES: DeferredRegister[BlockEntityType[_]] = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BookBook.MODID)

  val BOOK_FURNACE: RegistryObject[BlockEntityType[BlockEntityBookFurnace]] = BLOCK_ENTITIES.register("book_furnace_block_entity", () => BlockEntityType.Builder.of(new BlockEntityBookFurnace(_, _), Blocks.BOOK_FURNACE.get()).build(null))

  def registry(eventBus: IEventBus): Unit = {
    BLOCK_ENTITIES.register(eventBus)
  }
}
