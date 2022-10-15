package com.yuuki1293.bookbook.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.yuuki1293.bookbook.common.block.entity.BookStandBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context

class BookStandRenderer(context: Context) extends BlockEntityRenderer[BookStandBlockEntity](context) {
  override def render(pBlockEntity: BookStandBlockEntity, pPartialTick: Float, pPoseStack: PoseStack, pBufferSource: MultiBufferSource, pPackedLight: Int, pPackedOverlay: Int): Unit = ???
}
