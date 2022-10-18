package com.yuuki1293.bookbook.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

import scala.annotation.unused

abstract class BaseBookBlock {
  def getEnchantPowerBonus(@unused state: BlockState, @unused level: LevelReader, @unused pos: BlockPos): Float = 1
}
