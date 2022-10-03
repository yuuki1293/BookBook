package com.yuuki1293.bookbook.client.event

import com.yuuki1293.bookbook.client.screen.BookGeneratorScreen
import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.BookGeneratorMenu
import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(modid = BookBook.MODID, bus = Bus.MOD, value = Array(Dist.CLIENT))
object ClientModEvents {
  @SubscribeEvent
  def clientSetup(event: FMLClientSetupEvent): Unit = {
    MenuScreens.register(MenuTypes.BOOK_GENERATOR.get(), (menu: BookGeneratorMenu, inv, title) => new BookGeneratorScreen(menu, inv, title))
  }
}