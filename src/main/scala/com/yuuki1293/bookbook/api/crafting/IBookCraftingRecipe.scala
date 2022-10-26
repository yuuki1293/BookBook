package com.yuuki1293.bookbook.api.crafting

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack

trait IBookCraftingRecipe {
  def getPowerCost: Int

  def getPowerRate: Int

  def getInputs: NonNullList[ItemStack]
}
