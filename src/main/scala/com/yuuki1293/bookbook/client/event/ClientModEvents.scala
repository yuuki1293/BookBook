package com.yuuki1293.bookbook.client.event

import com.yuuki1293.bookbook.client.renderer.BookStandRenderer
import com.yuuki1293.bookbook.client.screen.{BookCapacitorScreen, BookCraftingCoreScreen, BookGeneratorScreen}
import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.{BookCapacitorMenu, BookCraftingCoreMenu, BookGeneratorMenu}
import com.yuuki1293.bookbook.common.register.{BlockEntities, Blocks, MenuTypes}
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.{ItemBlockRenderTypes, RenderType}
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

import scala.annotation.unused

@Mod.EventBusSubscriber(modid = BookBook.MOD_ID, bus = Bus.MOD, value = Array(Dist.CLIENT))
@unused
object ClientModEvents {
  @SubscribeEvent
  def clientSetup(@unused event: FMLClientSetupEvent): Unit = {
    MenuScreens.register(MenuTypes.BOOK_GENERATOR.get(), (menu: BookGeneratorMenu, inv, title) => new BookGeneratorScreen(menu, inv, title))
    MenuScreens.register(MenuTypes.BOOK_CAPACITOR.get(), (menu: BookCapacitorMenu, inv, title) => new BookCapacitorScreen(menu, inv, title))
    MenuScreens.register(MenuTypes.BOOK_CRAFTING_CORE.get(), (menu: BookCraftingCoreMenu, inv, title) => new BookCraftingCoreScreen(menu, inv, title))

    ItemBlockRenderTypes.setRenderLayer(Blocks.BOOKSHELF_FRAME.get(), RenderType.cutout())

    BlockEntityRenderers.register(BlockEntities.BOOK_STAND.get(), new BookStandRenderer(_))
  }
}
