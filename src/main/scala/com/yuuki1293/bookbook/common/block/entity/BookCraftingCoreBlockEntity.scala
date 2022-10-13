package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.register.BlockEntities
import com.yuuki1293.bookbook.common.util.Ticked
import net.minecraft.core.{BlockPos, NonNullList}
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState

class BookCraftingCoreBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_CRAFTING_CORE.get(), worldPosition, blockState)
    with WorldlyContainer with Ticked {
}
