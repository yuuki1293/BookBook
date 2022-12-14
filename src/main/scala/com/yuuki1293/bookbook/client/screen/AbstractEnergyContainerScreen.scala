package com.yuuki1293.bookbook.client.screen

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.yuuki1293.bookbook.common.inventory.EnergyMenu
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

//noinspection VarCouldBeVal
abstract class AbstractEnergyContainerScreen[A <: AbstractContainerMenu with EnergyMenu](pMenu: A, pPlayerInventory: Inventory, pTitle: Component)
  extends AbstractContainerScreen(pMenu, pPlayerInventory, pTitle) {

  protected val TEXTURE: ResourceLocation
  protected var gaugeLeft = 176
  protected var gaugeTop = 0
  protected var gaugeBgLeft = 152
  protected var gaugeBgTop = 10
  protected var gaugeWidth = 16
  protected var gaugeHeight = 50
  protected var gaugeTextLeft = 0
  protected var gaugeTextTop = 0

  override def init(): Unit = {
    super.init()
    titleLabelX = (imageWidth - font.width(title)) / 2
    gaugeTextTop = -font.lineHeight
  }

  override def render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float): Unit = {
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
    renderGauge(pPoseStack)
  }

  override def renderBg(pPoseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int): Unit = {
    RenderSystem.setShaderTexture(0, TEXTURE)

    val left = leftPos
    val top = topPos

    blit(pPoseStack, left, top, 0, 0, imageWidth, imageHeight)
  }

  override def renderLabels(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int): Unit = {
    super.renderLabels(pPoseStack, pMouseX, pMouseY)
    renderGaugeLabel(pPoseStack)
  }

  protected def renderGauge(pPoseStack: PoseStack): Unit = {
    RenderSystem.setShaderTexture(0, TEXTURE)
    val left = leftPos
    val top = topPos

    val proportion = (menu.getEnergyProportion.toDouble * (gaugeHeight.toDouble / 100.0D)).toInt
    blit(pPoseStack, left + gaugeBgLeft, top + gaugeBgTop + gaugeHeight - proportion, gaugeLeft, gaugeTop + gaugeHeight - proportion, gaugeWidth, proportion)
  }

  protected def renderGaugeLabel(pPoseStack: PoseStack): Unit = {
    val energyStored = menu.getEnergyStored
    val capacity = menu.getMaxEnergy

    font.draw(pPoseStack, s"$energyStored/$capacity RF", gaugeTextLeft.toFloat, gaugeTextTop.toFloat, 0xffffff)
  }
}
