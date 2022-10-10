package com.yuuki1293.bookbook.client.screen

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.yuuki1293.bookbook.common.inventory.EnergyMenu
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.{Component, TextComponent}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

import scala.jdk.CollectionConverters._

abstract class AbstractEnergyContainerScreen[A <: AbstractContainerMenu with EnergyMenu](pMenu: A, pPlayerInventory: Inventory, pTitle: Component)
  extends AbstractContainerScreen(pMenu, pPlayerInventory, pTitle) {

  protected val TEXTURE: ResourceLocation
  protected var gaugeLeft = 176
  protected var gaugeTop = 0
  protected var gaugeBgLeft = 152
  protected var gaugeBgTop = 10
  protected var gaugeWidth = 16
  protected var gaugeHeight = 50

  override def render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float): Unit = {
    if (isHoverGauge(pMouseX, pMouseY)) {
      val text: List[Component] = List(new TextComponent(s"${menu.getEnergyProportion}/${menu.getMaxEnergy} RF"))
      renderComponentTooltip(pPoseStack, text.asJava, pMouseX, pMouseY)
    }

    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
  }

  override def renderBg(pPoseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int): Unit = {
    RenderSystem.setShader(() => GameRenderer.getPositionTexShader)
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F)
    RenderSystem.setShaderTexture(0, TEXTURE)
    val left = leftPos
    val top = topPos

    blit(pPoseStack, left, top, 0, 0, imageWidth, imageHeight)

    val proportion = (menu.getEnergyProportion.toDouble * (gaugeHeight.toDouble / 100.0D)).toInt
    blit(pPoseStack, left + gaugeBgLeft, top + gaugeBgTop + gaugeHeight - proportion, gaugeLeft, gaugeTop + gaugeHeight - proportion, gaugeWidth, proportion)
  }

  protected def isHoverGauge(x: Int, y: Int): Boolean = {
    if (x < gaugeLeft) return false
    if (x > gaugeLeft + gaugeWidth) return false
    if (y > gaugeTop) return false
    if (y < gaugeTop + gaugeHeight) return false
    true
  }
}
