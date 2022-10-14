package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BookStandBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{Block, EntityBlock}
import net.minecraft.world.phys.BlockHitResult

class BookStandBlock(properties: BlockBehaviour.Properties) extends Block(properties) with EntityBlock {
  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = new BookStandBlockEntity(pPos, pState)

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = {
    val be = pLevel.getBlockEntity(pPos)

    be match {
      case stand: BookStandBlockEntity =>
        val input = stand.getItem
        val held = pPlayer.getItemInHand(pHand)

        if (input.isEmpty && !held.isEmpty) {
          stand.setItem(held)
          pPlayer.setItemInHand(pHand, ItemStack.EMPTY)
        } else if (!input.isEmpty) {
          val item = new ItemEntity(pLevel, pPlayer.getX, pPlayer.getY, pPlayer.getZ, input)

          item.setNoPickUpDelay()
          pLevel.addFreshEntity(item)
          stand.setItem(ItemStack.EMPTY)
        }
      case _ =>
    }

    InteractionResult.SUCCESS
  }
}