package com.yuuki1293.bookbook

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import com.yuuki1293.bookbook.common.register.Items
import net.minecraft.world.item
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.SimpleContainer
import org.scalatest.flatspec.AnyFlatSpec

import scala.io.Source

class RecipeSpec extends AnyFlatSpec {
  "BookCraftingRecipe" should "recognize recipes correctly" in {
    val json = GsonHelper.parse(
      Source.fromResource("data/bookbook/recipes/book_crafting/unstable_book.json").reader()
    )
    val recipe = BookCraftingRecipe.Serializer.fromJson(
      new ResourceLocation(BookBook.MOD_ID, "book_crafting_recipe"),
      json
    )

    assert(recipe.getPowerCost == 1000)
    assert(recipe.getResultItem.sameItem(Items.UNSTABLE_BOOK.get().getDefaultInstance))

    val recipeContainer = new SimpleContainer(
      Items.DROWNED_BOOK.get().getDefaultInstance,
      item.Items.COOKED_PORKCHOP.getDefaultInstance,
      item.Items.PAPER.getDefaultInstance
    )
    assert(recipe.matches(recipeContainer, null))
  }
}