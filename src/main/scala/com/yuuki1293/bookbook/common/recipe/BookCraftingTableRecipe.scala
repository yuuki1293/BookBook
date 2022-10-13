package com.yuuki1293.bookbook.common.recipe

import com.google.gson.{JsonArray, JsonObject, JsonSyntaxException}
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.{Ingredient, Recipe, RecipeSerializer, RecipeType, ShapedRecipe}
import net.minecraft.world.level.Level
import net.minecraftforge.registries.{ForgeRegistry, ForgeRegistryEntry}

class BookCraftingTableRecipe(pId: ResourceLocation, pIngredients: NonNullList[Ingredient], pOutput: ItemStack, pPowerCost: Int)
  extends Recipe[SimpleContainer] {
  private final val recipeId = pId
  private final val output = pOutput
  private final val ingredients = pIngredients
  private final val powerCost = pPowerCost
  private final val inputsList = Array()

  override def matches(pContainer: SimpleContainer, pLevel: Level): Boolean = {
    val input = pContainer.getItem(0)
    ingredients.get(0).test(input)
  }

  override def assemble(pContainer: SimpleContainer): ItemStack = output.copy()

  override def canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

  override def getResultItem: ItemStack = output

  override def getId: ResourceLocation = recipeId

  override def getSerializer: RecipeSerializer[_] = ???

  override def getType: RecipeType[_] = ???
}

object BookCraftingTableRecipe {
  object Serializer extends ForgeRegistryEntry[RecipeSerializer[_]] with RecipeSerializer[BookCraftingTableRecipe] {
    override def fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): BookCraftingTableRecipe = {
      val inputs: NonNullList[Ingredient] = NonNullList.create()
      val input = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "input"))

      inputs.add(input)

      val ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients")
      for (i <- 0 until ingredients.size()) {
        val ingredient = Ingredient.fromJson(ingredients.get(i))
        inputs.add(ingredient)
      }

      val output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"))

      if (!pSerializedRecipe.has("powerCost"))
        throw new JsonSyntaxException("powerCost is required")

      val powerCost = GsonHelper.getAsInt(pSerializedRecipe, "powerCost")

      new BookCraftingTableRecipe(pRecipeId, inputs, output, powerCost)
    }

    override def fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): BookCraftingTableRecipe = {
      val size = pBuffer.readVarInt()
      val inputs = NonNullList.withSize(size, Ingredient.EMPTY)

      for (i <- 0 until size) {
        inputs.set(i, Ingredient.fromNetwork(pBuffer))
      }

      val output = pBuffer.readItem()
      val powerCost = pBuffer.readVarInt()

      new BookCraftingTableRecipe(pRecipeId, inputs, output, powerCost)
    }

    override def toNetwork(pBuffer: FriendlyByteBuf, pRecipe: BookCraftingTableRecipe): Unit = {
      import scala.jdk.CollectionConverters._

      pBuffer.writeVarInt(pRecipe.ingredients.size())

      pRecipe.ingredients.asScala.foreach(_.toNetwork(pBuffer))

      pBuffer.writeItem(pRecipe.output)
      pBuffer.writeVarInt(pRecipe.powerCost)
    }
  }
}