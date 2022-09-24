package com.yuuki1293.bookbook.common.inventory

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.{AbstractFurnaceMenu, ContainerData, MenuType, RecipeBookType}
import net.minecraft.world.item.crafting.RecipeType

class BookFurnaceMenu(pContainerId:Int, pPlayerInventory: Inventory, pFurnaceContainer: Container, pFurnaceData: ContainerData)
  extends AbstractFurnaceMenu(MenuType.FURNACE, RecipeType.SMELTING, RecipeBookType.FURNACE, pContainerId, pPlayerInventory, pFurnaceContainer, pFurnaceData) {
}