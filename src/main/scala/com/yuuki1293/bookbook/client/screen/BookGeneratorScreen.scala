package com.yuuki1293.bookbook.client.screen

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.yuuki1293.bookbook.client.screen.BookGeneratorScreen.TEXTURE
import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.inventory.BookGeneratorMenu
import net.minecraft.client.gui.screens.inventory.{AbstractContainerScreen, MenuAccess}
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class BookGeneratorScreen(pMenu: BookGeneratorMenu, pPlayerInventory: Inventory, pTitle: Component) extends AbstractContainerScreen(pMenu, pPlayerInventory, pTitle) with MenuAccess[BookGeneratorMenu] {
  this.leftPos = 0
  this.topPos = 0

  override def render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float): Unit = {
    renderBackground(pPoseStack)
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
    this.renderTooltip(pPoseStack, pMouseX, pMouseY)
  }

  override def renderBg(pPoseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int): Unit = {
    RenderSystem.setShader(() => GameRenderer.getPositionTexShader)
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F)
    RenderSystem.setShaderTexture(0, TEXTURE)
    val left = this.leftPos
    val top = this.topPos
    this.blit(pPoseStack, left, top, 0, 0, this.imageWidth, this.imageHeight)

    if (this.menu.isBurn) {
      val progress = this.menu.getBurnProgress
      this.blit(pPoseStack, left + 153, top + 62 + 12 - progress, 176, 12 - progress, 14, progress + 1)
    }

    val proportion = this.menu.getEnergyProportion / 2
    this.blit(pPoseStack, left + 152, top + 10 + 50 - proportion, 176, 64 - proportion, 16, proportion)
  }
}

object BookGeneratorScreen {
  private final val TEXTURE = new ResourceLocation(BookBook.MODID, "textures/gui/container/book_generator.png")
}