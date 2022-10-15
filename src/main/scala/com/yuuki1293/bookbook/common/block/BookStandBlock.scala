package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BookStandBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{BaseEntityBlock, Block}
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.{CollisionContext, VoxelShape}
import net.minecraft.world.{Containers, InteractionHand, InteractionResult}

class BookStandBlock(properties: BlockBehaviour.Properties) extends BaseEntityBlock(properties) {
  protected val SHAPE: VoxelShape = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D)

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

  override def onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pIsMoving: Boolean): Unit = {
    if (pState.getBlock != pNewState.getBlock) {
      val be = pLevel.getBlockEntity(pPos)

      be match {
        case entity: BookStandBlockEntity => Containers.dropContents(pLevel, pPos, entity.getItems)
      }
    }

    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
  }

  override def getShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos, pContext: CollisionContext): VoxelShape = {
    SHAPE
  }
}