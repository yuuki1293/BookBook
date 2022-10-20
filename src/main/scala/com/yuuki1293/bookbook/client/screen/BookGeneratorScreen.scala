package com.yuuki1293.bookbook.client.screen

import com.mojang.blaze3d.vertex.PoseStack
import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.BookGeneratorMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class BookGeneratorScreen(pMenu: BookGeneratorMenu, pPlayerInventory: Inventory, pTitle: Component) extends AbstractEnergyContainerScreen(pMenu, pPlayerInventory, pTitle) {
  val TEXTURE = new ResourceLocation(BookBook.MOD_ID, "textures/gui/container/book_generator.png")

  override def init(): Unit = {
    super.init()
    gaugeTop = 14
  }

  override def render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float): Unit = {
    renderBackground(pPoseStack)
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
    renderTooltip(pPoseStack, pMouseX, pMouseY)
  }

  override def renderBg(pPoseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int): Unit = {
    super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY)

    renderFire(pPoseStack)
  }

  protected def renderFire(pPoseStack: PoseStack): Unit = {
    val left = leftPos
    val top = topPos

    if (menu.isBurn) {
      val progress = menu.getBurnProgress
      blit(pPoseStack, left + 153, top + 62 + 12 - progress, 176, 12 - progress, 14, progress + 1)
    }
  }
}

object BookGeneratorScreen {
  def apply(menu: BookGeneratorMenu, playerInventory: Inventory, title: Component) =
    new BookGeneratorScreen(menu, playerInventory, title)
}