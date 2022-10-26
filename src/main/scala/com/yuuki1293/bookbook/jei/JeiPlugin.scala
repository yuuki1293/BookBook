package com.yuuki1293.bookbook.jei

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.jei.JeiPlugin.UID
import com.yuuki1293.bookbook.jei.category.BookCraftingCategory
import mezz.jei.api.IModPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.{IRecipeCategoryRegistration, IRecipeRegistration}
import net.minecraft.resources.ResourceLocation

@JeiPlugin
class JeiPlugin extends IModPlugin {
  override def getPluginUid: ResourceLocation = UID

  override def registerCategories(registration: IRecipeCategoryRegistration): Unit = {
    implicit val helper: IGuiHelper = registration.getJeiHelpers.getGuiHelper

    registration.addRecipeCategories(BookCraftingCategory.apply)
  }

  override def registerRecipes(registration: IRecipeRegistration): Unit = {
  }
}

object JeiPlugin {
  final val UID: ResourceLocation = new ResourceLocation(BookBook.MOD_ID, "jei_plugin")
}