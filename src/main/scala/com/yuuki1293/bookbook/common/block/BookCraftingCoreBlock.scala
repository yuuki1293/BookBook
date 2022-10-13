package com.yuuki1293.bookbook.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{Block, EntityBlock}
import net.minecraft.world.phys.BlockHitResult

class BookCraftingCoreBlock(pProperties: BlockBehaviour.Properties) extends Block(pProperties) with EntityBlock {
  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity =
    new BookCraftingCoreBlockEntity(pPos, pState)

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = {

  }
}
