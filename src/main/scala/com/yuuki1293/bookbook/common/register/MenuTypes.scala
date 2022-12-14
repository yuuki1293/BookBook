package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.{BookCapacitorMenu, BookCraftingCoreMenu, BookGeneratorMenu}
import net.minecraft.core.Registry
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.{DeferredRegister, RegistryObject}

object MenuTypes extends AbstractRegister[MenuType[_]] {
  val REGISTER: DeferredRegister[MenuType[_]] = DeferredRegister.create(Registry.MENU_REGISTRY, BookBook.MOD_ID)

  val BOOK_GENERATOR: RegistryObject[MenuType[BookGeneratorMenu]] = REGISTER.register("book_generator_menu_type", () => new MenuType[BookGeneratorMenu](BookGeneratorMenu(_, _)))
  val BOOK_CAPACITOR: RegistryObject[MenuType[BookCapacitorMenu]] = REGISTER.register("book_capacitor_menu_type", () => new MenuType[BookCapacitorMenu](BookCapacitorMenu(_, _)))
  val BOOK_CRAFTING_CORE: RegistryObject[MenuType[BookCraftingCoreMenu]] = REGISTER.register("book_crafting_core_menu_type", () => new MenuType[BookCraftingCoreMenu](BookCraftingCoreMenu(_, _)))
}
