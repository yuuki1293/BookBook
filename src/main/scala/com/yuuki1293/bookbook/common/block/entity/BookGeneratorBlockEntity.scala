package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.energy.EnergyStorage

class BookGeneratorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_GENERATOR.get(), worldPosition, blockState)
    with EnergyStorage(1000, 10, 0) {

  var litTime: Int
  var litDuration: Int
  protected val dataAccess: ContainerData = new ContainerData {
    override def get(pIndex: Int): Int = {
      pIndex match {
        case 0 => litTime
        case 1 => litDuration
        case _ => 0
      }
    }

    override def set(pIndex: Int, pValue: Int): Unit = {
      pIndex match {
        case 0 => litTime = pValue
        case 1 => litDuration = pValue
      }
    }

    override def getCount: Int = 2
  }

  override def getDefaultName: Component = new TranslatableComponent("container.block_generator")
  
}
