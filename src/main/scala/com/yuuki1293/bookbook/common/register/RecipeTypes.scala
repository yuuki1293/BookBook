package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import net.minecraft.world.Container
import net.minecraft.world.item.crafting.{Recipe, RecipeType}
import net.minecraft.world.level.Level

import java.util.Optional

object RecipeTypes {
  val BOOK_CRAFTING: RecipeType[BookCraftingRecipe] = new RecipeType[BookCraftingRecipe]() {
    override def tryMatch[C <: Container](pRecipe: Recipe[C], pLevel: Level, pContainer: C): Optional[BookCraftingRecipe] = {
      if (pRecipe.matches(pContainer, pLevel))
        Optional.of(pRecipe.asInstanceOf[BookCraftingRecipe])
      else
        Optional.empty()
    }
  }
}
