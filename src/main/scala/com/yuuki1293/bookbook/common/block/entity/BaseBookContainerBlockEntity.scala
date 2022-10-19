package com.yuuki1293.bookbook.common.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BaseContainerBlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState

abstract class BaseBookContainerBlockEntity(pType: BlockEntityType[_], pWorldPosition: BlockPos, pBlockState: BlockState)
  extends BaseContainerBlockEntity(pType, pWorldPosition, pBlockState) with IBookContainerBlockEntity {
  override var level: Level = super[BaseContainerBlockEntity].level
  override var worldPosition: BlockPos = super[BaseContainerBlockEntity].worldPosition
  override def load(pTag: CompoundTag): Unit = {
    super[BaseContainerBlockEntity].load(pTag)
    super[IBookContainerBlockEntity].load(pTag)
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super[BaseContainerBlockEntity].saveAdditional(pTag)
    super[IBookContainerBlockEntity].saveAdditional(pTag)
  }
}
