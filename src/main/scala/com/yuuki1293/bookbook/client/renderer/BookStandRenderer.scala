package com.yuuki1293.bookbook.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import com.yuuki1293.bookbook.common.block.entity.BookStandBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context
import net.minecraft.world.item.BlockItem

import scala.annotation.unused

class BookStandRenderer(@unused context: Context) extends BlockEntityRenderer[BookStandBlockEntity] {
  override def render(pBlockEntity: BookStandBlockEntity,
                      pPartialTick: Float,
                      pPoseStack: PoseStack,
                      pBufferSource: MultiBufferSource,
                      pPackedLight: Int,
                      pPackedOverlay: Int): Unit = {
    val minecraft = Minecraft.getInstance()
    val itemStack = pBlockEntity.getItem

    if (!itemStack.isEmpty) {
      pPoseStack.pushPose()
      pPoseStack.translate(0.5D, 1.0D, 0.5D)
      val scale = if (itemStack.getItem.isInstanceOf[BlockItem]) 0.9F else 0.65F
      pPoseStack.scale(scale, scale, scale)
      val tick = System.currentTimeMillis() / 800.0D
      pPoseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D)
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees(((tick * 40.0D) % 360).toFloat))
      minecraft.getItemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GROUND, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0)
      pPoseStack.popPose()
    }
  }
}
