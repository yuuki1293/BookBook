package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.custom.WaterproofedBookRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.registries.IForgeRegistry


object Recipe {
  def registry(pRegistry: IForgeRegistry[RecipeSerializer[_]]): Unit = {
    register(pRegistry, getId("waterproofed_book"), WaterproofedBookRecipe.getSerializer)
  }

  private def getId(id: String): ResourceLocation = new ResourceLocation(BookBook.MODID, id)

  private def register(pRegistry: IForgeRegistry[RecipeSerializer[_]], id: ResourceLocation, serializer: RecipeSerializer[_]): Unit = {
    serializer.setRegistryName(id)
    pRegistry.register(serializer)
  }
}
