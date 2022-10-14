package com.yuuki1293.bookbook.client.screen

import com.yuuki1293.bookbook.common.inventory.BookCraftingCoreMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class BookCraftingCoreScreen(pMenu: BookCraftingCoreMenu, pPlayerInventory: Inventory, pTitle: Component)
  extends AbstractEnergyContainerScreen(pMenu, pPlayerInventory, pTitle) {
  override protected val TEXTURE: ResourceLocation = new ResourceLocation("textures/gui/container/book_crafting_core.png")
}
