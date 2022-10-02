package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.BookGeneratorMenu
import net.minecraft.core.Registry
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.{DeferredRegister, RegistryObject}

object MenuTypes {
  val MENU_TYPE: DeferredRegister[MenuType[_]] = DeferredRegister.create(Registry.MENU_REGISTRY, BookBook.MODID)

  val BOOK_GENERATOR: RegistryObject[MenuType[BookGeneratorMenu]] = MENU_TYPE.register("book_generator_menu_type", () => new MenuType[BookGeneratorMenu](new BookGeneratorMenu(_, _)))

  def registry(eventBus: IEventBus): Unit = MENU_TYPE.register(eventBus)
}
