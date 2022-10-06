package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.BookGeneratorBlock._
import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.properties.{BlockStateProperties, BooleanProperty, DirectionProperty}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState, StateDefinition}
import net.minecraft.world.level.block.{Block, EntityBlock, HorizontalDirectionalBlock}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{Containers, InteractionHand, InteractionResult, MenuProvider}

class BookGeneratorBlock(properties: BlockBehaviour.Properties) extends Block(properties) with EntityBlock {
  this.registerDefaultState(
    this.stateDefinition.any()
      .setValue(LIT, java.lang.Boolean.FALSE)
  )

  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    if (pLevel.isClientSide)
      null
    else
      (_, _, _, blockEntity) => blockEntity.asInstanceOf[BookGeneratorBlockEntity].tick()
  }

  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new BookGeneratorBlockEntity(pPos, pState)
  }

  override def createBlockStateDefinition(pBuilder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(pBuilder)
    pBuilder.add(FACING, LIT)
  }

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = if (pLevel.isClientSide)
    InteractionResult.SUCCESS
  else {
    this.openContainer(pLevel, pPos, pPlayer)
    InteractionResult.CONSUME
  }

  override def onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pIsMoving: Boolean): Unit = {
    if (!pState.is(pNewState.getBlock)) {
      val blockEntity = pLevel.getBlockEntity(pPos)
      blockEntity match {
        case entity: BookGeneratorBlockEntity =>
          if (pLevel.isInstanceOf[ServerLevel]) {
            Containers.dropContents(pLevel, pPos, entity)
          }
      }

      pLevel.updateNeighbourForOutputSignal(pPos, this)
    }

    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
  }

  protected def openContainer(pLevel: Level, pPos: BlockPos, pPlayer: Player): Unit = {
    val blockEntity = pLevel.getBlockEntity(pPos)
    if (blockEntity.isInstanceOf[BookGeneratorBlockEntity]) {
      pPlayer.openMenu(blockEntity.asInstanceOf[MenuProvider])
    }
  }
}

object BookGeneratorBlock {
  final val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
  final val LIT: BooleanProperty = BlockStateProperties.LIT
}