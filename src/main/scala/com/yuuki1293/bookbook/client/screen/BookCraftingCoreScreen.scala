package com.yuuki1293.bookbook.client.screen

import com.mojang.blaze3d.vertex.PoseStack
import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.BookCraftingCoreMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class BookCraftingCoreScreen(pMenu: BookCraftingCoreMenu, pPlayerInventory: Inventory, pTitle: Component)
  extends AbstractEnergyContainerScreen(pMenu, pPlayerInventory, pTitle) {
  override val TEXTURE: ResourceLocation = new ResourceLocation(BookBook.MOD_ID, "textures/gui/container/book_crafting_core.png")

  override def render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float): Unit = {
    renderBackground(pPoseStack)
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
    renderTooltip(pPoseStack, pMouseX, pMouseY)
  }
}

object BookCraftingCoreScreen {
  def apply(menu: BookCraftingCoreMenu, playerInventory: Inventory, title: Component) =
    new BookCraftingCoreScreen(menu, playerInventory, title)
}