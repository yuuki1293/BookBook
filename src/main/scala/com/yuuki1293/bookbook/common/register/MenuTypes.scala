package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.{BookCapacitorMenu, BookGeneratorMenu}
import net.minecraft.core.Registry
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.{DeferredRegister, RegistryObject}

object MenuTypes extends AbstractRegister[MenuType[_]] {
  val REGISTER: DeferredRegister[MenuType[_]] = DeferredRegister.create(Registry.MENU_REGISTRY, BookBook.MOD_ID)

  val BOOK_GENERATOR: RegistryObject[MenuType[BookGeneratorMenu]] = REGISTER.register("book_generator_menu_type", () => new MenuType[BookGeneratorMenu](new BookGeneratorMenu(_, _)))
  val BOOK_CAPACITOR: RegistryObject[MenuType[BookCapacitorMenu]] = REGISTER.register("book_capacitor_menu_type", () => new MenuType[BookCapacitorMenu](new BookCapacitorMenu(_, _)))
}
