package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.custom.WaterproofedBookRecipe
import net.minecraft.core.Registry
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.{DeferredRegister, RegistryObject}


object Recipe {
  val RECIPE: DeferredRegister[RecipeSerializer[_]] = DeferredRegister.create(Registry.RECIPE_SERIALIZER_REGISTRY, BookBook.MODID)

  val WATERPROOFED_BOOK: RegistryObject[_] = RECIPE.register("waterproofed_book", () => WaterproofedBookRecipe.getSerializer)
  def registry(eventBus: IEventBus): Unit = {
    RECIPE.register(eventBus)
  }
}
