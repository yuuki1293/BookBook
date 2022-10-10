package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class BookCapacitorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BlockEntity(BlockEntities.BOOK_CAPACITOR.get(), worldPosition, blockState) {

}
