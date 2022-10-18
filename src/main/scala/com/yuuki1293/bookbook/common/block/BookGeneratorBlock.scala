package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.BookGeneratorBlock._
import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity
import com.yuuki1293.bookbook.common.util.Ticked
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.properties.{BlockStateProperties, BooleanProperty, DirectionProperty}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState, StateDefinition}
import net.minecraft.world.level.block.{BaseEntityBlock, Block, HorizontalDirectionalBlock, RenderShape}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{Containers, InteractionHand, InteractionResult, MenuProvider}

class BookGeneratorBlock(properties: BlockBehaviour.Properties)
  extends BaseBookContainerBlock[BookGeneratorBlockEntity](properties) {
  registerDefaultState(
    stateDefinition.any()
      .setValue(LIT, java.lang.Boolean.FALSE)
  )

  override def getTicker[A <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[A]): BlockEntityTicker[A] = {
    if (pLevel.isClientSide)
      null
    else
      (_, _, _, blockEntity) => blockEntity.asInstanceOf[Ticked].tick()
  }

  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new BookGeneratorBlockEntity(pPos, pState)
  }

  override def createBlockStateDefinition(pBuilder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(pBuilder)
    pBuilder.add(FACING, LIT)
  }

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = {
    if (pLevel.isClientSide)
      InteractionResult.SUCCESS
    else {
      openContainer(pLevel, pPos, pPlayer)
      InteractionResult.CONSUME
    }
  }
}

object BookGeneratorBlock {
  final val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
  final val LIT: BooleanProperty = BlockStateProperties.LIT
}