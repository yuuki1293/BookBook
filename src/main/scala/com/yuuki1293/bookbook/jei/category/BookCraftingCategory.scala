package com.yuuki1293.bookbook.jei.category

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import com.yuuki1293.bookbook.common.register.Blocks
import com.yuuki1293.bookbook.jei.category.BookCraftingCategory.{TEXTURE, UID}
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class BookCraftingCategory(helper: IGuiHelper) extends IRecipeCategory[BookCraftingRecipe] {
  private final val background = helper.createDrawable(TEXTURE, 0, 0, 140, 171)
  private final val icon =
    helper.createDrawableIngredient(
      VanillaTypes.ITEM_STACK,
      new ItemStack(Blocks.BOOK_CRAFTING_CORE.get()))

  override def getTitle: Component =
    new TranslatableComponent("jei.category.bookbook.book_crafting")

  override def getBackground: IDrawable = background

  override def getIcon: IDrawable = icon

  override def getUid: ResourceLocation = UID

  override def getRecipeClass: Class[_ <: BookCraftingRecipe] = {
    getClass[BookCraftingRecipe]
  }
}

object BookCraftingCategory {
  private final val TEXTURE = new ResourceLocation(BookBook.MOD_ID, "textures/gui/jei/book_crafting.png")
  private final val UID = new ResourceLocation(BookBook.MOD_ID, "book_crafting")
}