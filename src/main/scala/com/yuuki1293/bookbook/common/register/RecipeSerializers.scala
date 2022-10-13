package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.BookCraftingTableRecipe
import com.yuuki1293.bookbook.common.register.RecipeSerializers.BOOK_CRAFTING_TABLE
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

    registry.register(BOOK_CRAFTING_TABLE.setRegistryName(new ResourceLocation(BookBook.MOD_ID, "book_crafting_table")))

    Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(BookBook.MOD_ID, "book_crafting_table"), RecipeTypes.BOOK_CRAFTING_TABLE)
  }
}

object RecipeSerializers {
  val BOOK_CRAFTING_TABLE: RecipeSerializer[BookCraftingTableRecipe] = BookCraftingTableRecipe.Serializer
}