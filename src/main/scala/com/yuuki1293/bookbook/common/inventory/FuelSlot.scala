package com.yuuki1293.bookbook.common.inventory

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.common.ForgeHooks

class FuelSlot(pContainer: Container, pSlot: Int, pX: Int, pY: Int) extends Slot(pContainer, pSlot, pX, pY) {
  override def mayPlace(pStack: ItemStack): Boolean = ForgeHooks.getBurnTime(pStack, RecipeType.SMELTING) > 0
}