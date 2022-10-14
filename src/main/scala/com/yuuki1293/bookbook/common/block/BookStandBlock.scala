package com.yuuki1293.bookbook.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{Block, EntityBlock}

class BookStandBlock(properties: BlockBehaviour.Properties) extends Block(properties) with EntityBlock {
  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = ???
}
