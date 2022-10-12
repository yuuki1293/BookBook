package com.yuuki1293.bookbook.common.recipe

import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.{Ingredient, Recipe, RecipeSerializer, RecipeType}
import net.minecraft.world.level.Level

class BookCraftingTableRecipe(pId: ResourceLocation, pOutput: ItemStack, pRecipeItems: NonNullList[Ingredient])
  extends Recipe[SimpleContainer] {

  override def matches(pContainer: SimpleContainer, pLevel: Level): Boolean = ???

  override def assemble(pContainer: SimpleContainer): ItemStack = ???

  override def canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = ???

  override def getResultItem: ItemStack = ???

  override def getId: ResourceLocation = ???

  override def getSerializer: RecipeSerializer[_] = ???

  override def getType: RecipeType[_] = ???
}
