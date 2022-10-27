package com.yuuki1293.bookbook.jei

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import com.yuuki1293.bookbook.common.register.RecipeTypes
import com.yuuki1293.bookbook.jei.IBookBookPlugin.UID
import com.yuuki1293.bookbook.jei.category.BookCraftingCategory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.{IRecipeCategoryRegistration, IRecipeRegistration}
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.SimpleContainer

import scala.annotation.unused

@JeiPlugin
@unused
class IBookBookPlugin extends IModPlugin {
  override def getPluginUid: ResourceLocation = UID

  override def registerCategories(registration: IRecipeCategoryRegistration): Unit = {
    implicit val helper: IGuiHelper = registration.getJeiHelpers.getGuiHelper

    registration.addRecipeCategories(BookCraftingCategory.apply)
  }

  override def registerRecipes(registration: IRecipeRegistration): Unit = {
    val level = Option(Minecraft.getInstance().level)

    level match {
      case Some(level) =>
        val manager = level.getRecipeManager
        registration.addRecipes(
          new RecipeType(BookCraftingCategory.UID, classOf[BookCraftingRecipe]),
          manager.getAllRecipesFor[SimpleContainer, BookCraftingRecipe](RecipeTypes.BOOK_CRAFTING)
        )
      case None =>
    }
  }
}

object IBookBookPlugin {
  final val UID: ResourceLocation = new ResourceLocation(BookBook.MOD_ID, "jei_plugin")
}