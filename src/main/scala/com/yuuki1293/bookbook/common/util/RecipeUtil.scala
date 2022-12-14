package com.yuuki1293.bookbook.common.util

import net.minecraft.world.Container
import net.minecraft.world.item.crafting.Recipe

object RecipeUtil {
  /**
   * containerのslotとrecipeのgetResultItemを比較してレシピを格納可能なら格納する
   *
   * @param recipe          レシピ
   * @param recipeContainer レシピが格納されているコンテナ
   * @param container       結果を格納するべきコンテナ
   * @param slot            containerのスロット
   * @return 結果が格納できた場合true、失敗した場合falseを返す
   */
  def assemble[A <: Container, B <: Container](recipe: Recipe[A], recipeContainer: A, container: B, slot: Int): Boolean = {
    val result = recipe.assemble(recipeContainer)

    if (ContainerUtil.canPlace(result, container, slot)) {
      ContainerUtil.place(result, container, slot)
    }
    else false
  }
}
