package com.yuuki1293.bookbook.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}

class BookShelfBlock(properties: BlockBehaviour.Properties) extends Block(properties) {
  override def getEnchantPowerBonus(state: BlockState, level: LevelReader, pos: BlockPos): Float = 1
}
