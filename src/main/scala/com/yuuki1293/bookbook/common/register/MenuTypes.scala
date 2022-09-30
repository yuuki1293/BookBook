package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.BookGeneratorMenu
import net.minecraft.core.Registry
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.{DeferredRegister, RegistryObject}

object MenuTypes {
  val MENU_TYPE: DeferredRegister[MenuType[_]] = DeferredRegister.create(Registry.MENU_REGISTRY, BookBook.MODID)

  val BOOK_GENERATOR: RegistryObject[MenuType[_]] = MENU_TYPE.register("book_generator", () => new MenuType[_](new BookGeneratorMenu(_, _)))
}