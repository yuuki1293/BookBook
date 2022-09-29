package com.yuuki1293.bookbook.common.block.entity.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.ItemStackHandler

class InventoryBlockEntity(pType: BlockEntityType[_], pPos: BlockPos, pBlockState: BlockState, size: Int) extends BlockEntity(pType, pPos, pBlockState) {
  protected var timer: Int
  protected var requiresUpdate: Boolean

  val inventory: ItemStackHandler = createInventory()
  protected var handler: LazyOptional[ItemStackHandler] = LazyOptional.of(() => this.inventory)

  def extractItem(slot: Int)
}
