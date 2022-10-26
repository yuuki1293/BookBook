package com.yuuki1293.bookbook.jei.category

import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.resources.ResourceLocation

class BookCraftingCategory extends IRecipeCategory[BookCraftingRecipe] {
  override def getTitle: Component =
    new TranslatableComponent("jei.category.bookbook.book_crafting")

  override def getBackground: IDrawable = ???

  override def getIcon: IDrawable = ???

  override def getUid: ResourceLocation = ???

  override def getRecipeClass: Class[_ <: BookCraftingRecipe] = ???
}
