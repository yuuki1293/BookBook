package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import com.yuuki1293.bookbook.common.register.RecipeSerializers.BOOK_CRAFTING
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber
class RecipeSerializers {
  @SubscribeEvent
  def onRegisterSerializers(event: RegistryEvent.Register[RecipeSerializer[_]]): Unit = {
    val registry = event.getRegistry

    registry.register(BOOK_CRAFTING.setRegistryName(new ResourceLocation(BookBook.MOD_ID, "book_crafting")))

    Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(BookBook.MOD_ID, "book_crafting"), RecipeTypes.BOOK_CRAFTING)
  }
}

object RecipeSerializers {
  val BOOK_CRAFTING: RecipeSerializer[BookCraftingRecipe] = BookCraftingRecipe.Serializer

  def apply = new RecipeSerializers
}