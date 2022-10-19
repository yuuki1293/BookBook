package com.yuuki1293.bookbook.common.block.entity

import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.{BaseContainerBlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}
import net.minecraftforge.items.wrapper.SidedInvWrapper

abstract class BaseBookContainerBlockEntity(pType: BlockEntityType[_], pWorldPosition: BlockPos, pBlockState: BlockState)
  extends BaseContainerBlockEntity(pType, pWorldPosition, pBlockState) with IBookContainerBlockEntity {
  override def load(pTag: CompoundTag): Unit = {
    super[BaseContainerBlockEntity].load(pTag)
    super[IBookContainerBlockEntity].load(pTag)
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super[BaseContainerBlockEntity].saveAdditional(pTag)
    super[IBookContainerBlockEntity].saveAdditional(pTag)
  }

  override def stillValid(pPlayer: Player): Boolean = {
    if (level.getBlockEntity(worldPosition) != this)
      return false
    pPlayer.distanceToSqr(worldPosition.getX.toDouble + 0.5D, worldPosition.getY.toDouble + 0.5D, worldPosition.getZ.toDouble + 0.5D) <= 64.0D
  }

  protected var handlers: Array[LazyOptional[IItemHandlerModifiable]] = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (!remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (side == Direction.UP)
        handlers(0).cast()
      else if (side == Direction.DOWN)
        handlers(1).cast()
      else
        handlers(2).cast()
    }
    else
      super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    for (handler <- handlers)
      handler.invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)
  }
}
