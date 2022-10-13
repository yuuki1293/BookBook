package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.recipe.BookCraftingTableRecipe
import net.minecraft.world.Container
import net.minecraft.world.item.crafting.{Recipe, RecipeType}
import net.minecraft.world.level.Level

import java.util.Optional

object RecipeTypes {
  val BOOK_CRAFTING_TABLE: RecipeType[BookCraftingTableRecipe] = new RecipeType[BookCraftingTableRecipe]() {
    override def tryMatch[C <: Container](pRecipe: Recipe[C], pLevel: Level, pContainer: C): Optional[BookCraftingTableRecipe] = {
      if (pRecipe.matches(pContainer, pLevel))
        Optional.of(pRecipe.asInstanceOf[BookCraftingTableRecipe])
      else
        Optional.empty()
    }
  }
}
