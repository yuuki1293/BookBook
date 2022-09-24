package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.inventory.BookFurnaceMenu
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.block.state.BlockState

class BookFurnaceBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends AbstractFurnaceBlockEntity(BlockEntities.BOOK_FURNACE.get(), worldPosition, blockState, RecipeType.SMELTING) {

  override protected def getDefaultName: Component = new TranslatableComponent("container.book_furnace")

  override protected def createMenu(pContainerId: Int, pPlayer: Inventory): AbstractContainerMenu = {
    new BookFurnaceMenu(pContainerId, pPlayer, this, this.dataAccess)
  }
}