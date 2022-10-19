package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BookStandBlockEntity
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.{CollisionContext, VoxelShape}
import net.minecraft.world.{InteractionHand, InteractionResult}

class BookStandBlock(properties: BlockBehaviour.Properties)
  extends BaseBookContainerBlock[BookStandBlockEntity](properties) {
  protected val SHAPE: VoxelShape = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D)

  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = new BookStandBlockEntity(pPos, pState)

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = {
    var flag = false
    val be = pLevel.getBlockEntity(pPos)

    be match {
      case stand: BookStandBlockEntity =>
        val input = stand.getItem
        val held = pPlayer.getItemInHand(pHand)

        if (input.isEmpty && !held.isEmpty) {
          stand.setItem(held)
          pPlayer.setItemInHand(pHand, ItemStack.EMPTY)
          flag = true
        } else if (!input.isEmpty) {
          val item = new ItemEntity(pLevel, pPlayer.getX, pPlayer.getY, pPlayer.getZ, input)

          item.setNoPickUpDelay()
          pLevel.addFreshEntity(item)
          stand.setItem(ItemStack.EMPTY)
          flag = true
        }
      case _ =>
    }

    if(flag)
      be.setChanged()

    InteractionResult.SUCCESS
  }

  override def getShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos, pContext: CollisionContext): VoxelShape = {
    SHAPE
  }

  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    createTickerHelper(pBlockEntityType, BlockEntities.BOOK_STAND.get(), BookStandBlockEntity)
  }
}
