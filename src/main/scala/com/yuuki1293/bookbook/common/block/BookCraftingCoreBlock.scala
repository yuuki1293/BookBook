package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BookCraftingCoreBlockEntity
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.{BaseEntityBlock, RenderShape}
import net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{Containers, InteractionHand, InteractionResult, MenuProvider}

class BookCraftingCoreBlock(pProperties: BlockBehaviour.Properties)
  extends BaseBookContainerBlock[BookCraftingCoreBlockEntity](pProperties) {
  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity =
    new BookCraftingCoreBlockEntity(pPos, pState)

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = {
    if (pLevel.isClientSide)
      InteractionResult.SUCCESS
    else {
      openContainer(pLevel, pPos, pPlayer)
      InteractionResult.CONSUME
    }
  }

  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    createTickerHelper(pBlockEntityType, BlockEntities.BOOK_CRAFTING_CORE.get(), BookCraftingCoreBlockEntity.tick)
  }
}
