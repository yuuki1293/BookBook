package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block.entity.{BookFurnaceBlockEntity, BookGeneratorBlockEntity}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object BlockEntities extends AbstractRegister[BlockEntityType[_]] {
  val REGISTER: DeferredRegister[BlockEntityType[_]] = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BookBook.MODID)

  val BOOK_FURNACE: RegistryObject[BlockEntityType[BookFurnaceBlockEntity]] = REGISTER.register("book_furnace_block_entity", () => BlockEntityType.Builder.of(new BookFurnaceBlockEntity(_, _), Blocks.BOOK_FURNACE.get()).build(null))
  val BOOK_GENERATOR: RegistryObject[BlockEntityType[BookGeneratorBlockEntity]] = REGISTER.register("book_generator_block_entity", () => BlockEntityType.Builder.of(new BookGeneratorBlockEntity(_, _), Blocks.BOOK_GENERATOR.get()).build(null))
}
