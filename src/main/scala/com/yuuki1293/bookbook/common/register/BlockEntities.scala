package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block.entity.{BookCapacitorBlockEntity, BookCraftingCoreBlockEntity, BookFurnaceBlockEntity, BookGeneratorBlockEntity, BookStandBlockEntity}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object BlockEntities extends AbstractRegister[BlockEntityType[_]] {
  val REGISTER: DeferredRegister[BlockEntityType[_]] = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BookBook.MOD_ID)

  val BOOK_FURNACE: RegistryObject[BlockEntityType[BookFurnaceBlockEntity]] = REGISTER.register("book_furnace_block_entity", () => BlockEntityType.Builder.of(BookFurnaceBlockEntity(_, _), Blocks.BOOK_FURNACE.get()).build(null))
  val BOOK_GENERATOR: RegistryObject[BlockEntityType[BookGeneratorBlockEntity]] = REGISTER.register("book_generator_block_entity", () => BlockEntityType.Builder.of(BookGeneratorBlockEntity(_, _), Blocks.BOOK_GENERATOR.get()).build(null))
  val BOOK_CAPACITOR: RegistryObject[BlockEntityType[BookCapacitorBlockEntity]] = REGISTER.register("book_capacitor_block_entity", () => BlockEntityType.Builder.of(BookCapacitorBlockEntity(_, _), Blocks.BOOK_CAPACITOR.get()).build(null))
  val BOOK_CRAFTING_CORE: RegistryObject[BlockEntityType[BookCraftingCoreBlockEntity]] = REGISTER.register("book_crafting_core_block_entity", () => BlockEntityType.Builder.of(BookCraftingCoreBlockEntity(_, _), Blocks.BOOK_CRAFTING_CORE.get()).build(null))
  val BOOK_STAND: RegistryObject[BlockEntityType[BookStandBlockEntity]] = REGISTER.register("book_stand_block_entity", () => BlockEntityType.Builder.of(BookStandBlockEntity(_, _), Blocks.BOOK_STAND.get()).build(null))
}
