package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object RecipeSerializers extends AbstractRegister[RecipeSerializer[_]] {
  override protected val REGISTER: DeferredRegister[RecipeSerializer[_]] =
    DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BookBook.MOD_ID)

  val BOOK_CRAFTING_SERIALIZER: RegistryObject[RecipeSerializer[BookCraftingRecipe]] =
    REGISTER.register("book_crafting", () => BookCraftingRecipe.Serializer)
}
