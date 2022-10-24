package com.yuuki1293.bookbook.common.block.entity

import cats.effect.IO
import com.yuuki1293.bookbook.common.block.entity.BookCraftingCoreBlockEntity._
import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookCraftingCoreMenu
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import com.yuuki1293.bookbook.common.register.{BlockEntities, MenuTypes, RecipeTypes}
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.{Container, SimpleContainer}
import net.minecraftforge.common.util.LazyOptional

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class BookCraftingCoreBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseBookContainerBlockEntity(BlockEntities.BOOK_CRAFTING_CORE.get(), worldPosition, blockState) {
  override var items: NonNullList[ItemStack] = NonNullList.withSize(2, ItemStack.EMPTY)
  private var recipeItems = NonNullList.withSize(81, ItemStack.EMPTY)
  private var recipe: Option[BookCraftingRecipe] = None
  private val capacity = 100000000
  private val maxReceive = 10000000

  val energyStorage: BookEnergyStorage = BookEnergyStorage(capacity, maxReceive, 10000000)(this)
  private var energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => energyStorage)
  private var progress = 0
  private var haveItemChanged = true
  protected val dataAccess: ContainerData = new ContainerData {
    /**
     * @param pIndex 0 - [[getEnergy]]<br>
     *               1 - [[getMaxEnergy]]<br>
     *               2 - [[getProgress]]<br>
     *               3 - [[getPowerCost]]
     * @return
     */
    override def get(pIndex: Int): Int = {
      (pIndex match {
        case DATA_ENERGY_STORED => getEnergy
        case DATA_MAX_ENERGY => getMaxEnergy
        case DATA_PROGRESS => getProgress
        case DATA_POWER_COST => getPowerCost
        case _ => throw new UnsupportedOperationException("Unable to get index: " + pIndex)
      }).unsafeRunSync()
    }

    /**
     * Invalid
     */
    @Deprecated
    override def set(pIndex: Int, pValue: Int): Unit = {
      throw new UnsupportedOperationException("Unable to get index: " + pIndex)
    }

    override def getCount: Int = 4
  }

  def getEnergy: IO[Int] = IO(energyStorage.getEnergyStored)

  def getMaxEnergy: IO[Int] = IO(energyStorage.getMaxEnergyStored)

  def getProgress: IO[Int] = IO(progress)

  def getPowerCost: IO[Int] = IO {
    recipe.map(_.getPowerCost).getOrElse(0)
  }

  override def getDefaultName: Component = new TranslatableComponent("container.bookbook.book_crafting_core")

  override def createMenu(pContainerId: Int, pInventory: Inventory): AbstractContainerMenu = {
    BookCraftingCoreMenu(
      MenuTypes.BOOK_CRAFTING_CORE.get(),
      pContainerId,
      pInventory,
      this,
      dataAccess)
  }

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(SLOT_INPUT, SLOT_OUTPUT)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = {
    pIndex match {
      case SLOT_INPUT => true
      case SLOT_OUTPUT => false
      case _ => false
    }
  }

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = {
    pIndex match {
      case SLOT_INPUT => false
      case SLOT_OUTPUT => true
      case _ => false
    }
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    energy.invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    energy = LazyOptional.of(() => energyStorage)
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    for {
      _ <- energyStorage.setEnergy(pTag.getInt("Energy"))
    } yield ()
    progress = pTag.getInt("Progress")
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    for {
      energy <- getEnergy
      progress <- getProgress
      _ <- IO {
        pTag.putInt("Energy", energy)
        pTag.putInt("Progress", progress)
      }
    } yield ()
  }

  def getStandWithItems: IO[Map[BlockPos, ItemStack]] = IO {
    val stands = mutable.HashMap[BlockPos, ItemStack]()
    val level = Option(getLevel)

    level match {
      case Some(level) =>
        val pos = getBlockPos
        val positions = BlockPos.betweenClosedStream(
          pos.offset(-4, 0, -4),
          pos.offset(4, 0, 4))

        positions.forEach(
          aoePos => {
            val be = level.getBlockEntity(aoePos)

            be match {
              case stand: BookStandBlockEntity =>
                val stack = stand.getItem(0)
                if (!stack.isEmpty) {
                  stands.put(aoePos.immutable(), stack)
                }
              case _ =>
            }
          })
        stands.toMap
      case None =>
        stands.toMap
    }
  }

  def updateRecipeInventory(stacks: List[ItemStack]): IO[Unit] = IO {
    var flag = false

    flag = recipeItems.size() != stacks.length + 1 || !ItemStack.isSame(recipeItems.get(0), items.get(0))

    if (!flag) {
      for (i <- stacks.indices) {
        if (!flag && !ItemStack.isSame(recipeItems.get(i + 1), stacks(i))) {
          flag = true
        }
      }
    }

    haveItemChanged = flag

    if (flag) {
      recipeItems = NonNullList.withSize(stacks.length + 1, ItemStack.EMPTY)

      recipeItems.set(0, items.get(SLOT_INPUT))

      for (i <- stacks.indices) {
        recipeItems.set(i + 1, stacks(i))
      }
    }
  }

  def updateRecipe(stacks: List[ItemStack], recipeContainer: SimpleContainer): IO[Unit] = IO {
    for (_ <- updateRecipeInventory(stacks)) yield ()

    if (haveItemChanged && (recipe.isEmpty || !recipe.exists(_.matches(recipeContainer, level)))) {
      recipe =
        Option(level
          .getRecipeManager
          .getRecipeFor(RecipeTypes.BOOK_CRAFTING, recipeContainer, level)
          .orElse(null))
    }
  }

  private def process(recipe: BookCraftingRecipe): IO[Boolean] = IO {
    val powerRate = recipe.getPowerRate
    val difference = recipe.getPowerCost - progress

    val extract = Math.min(powerRate, difference)

    val extracted = energyStorage.extractEnergy(extract, simulate = false)
    progress += extracted

    progress >= recipe.getPowerCost
  }
}

object BookCraftingCoreBlockEntity extends BlockEntityTicker[BookCraftingCoreBlockEntity] {
  final val SLOT_INPUT = 0
  final val SLOT_OUTPUT = 1
  final val DATA_ENERGY_STORED = 0
  final val DATA_MAX_ENERGY = 1
  final val DATA_PROGRESS = 2
  final val DATA_POWER_COST = 3

  def apply(worldPosition: BlockPos, blockState: BlockState) =
    new BookCraftingCoreBlockEntity(worldPosition, blockState)

  override def tick(level: Level, pos: BlockPos, state: BlockState, craftingCore: BookCraftingCoreBlockEntity): Unit = {
    var flag = false

    for {
      standsWithItems <- craftingCore.getStandWithItems
      stacks = standsWithItems.values.toList
      recipeItemContainer = new SimpleContainer(craftingCore.recipeItems.asScala.toSeq: _*)
      _ <- craftingCore.updateRecipe(stacks, recipeItemContainer)
      _ <- IO {
        if (!level.isClientSide) {
          craftingCore.recipe match {
            case Some(recipe) =>
              if (craftingCore.energyStorage.getEnergyStored > 0) {
                var done = craftingCore.progress >= recipe.getPowerCost

                if (!done) {
                  for {
                    d <- craftingCore.process(recipe)
                    _ <- IO(done = d)
                  } yield ()
                }

                for {
                  result <- assemble(recipe, recipeItemContainer, craftingCore, SLOT_OUTPUT)
                  _ <- IO {
                    if (done && result) {
                      craftingCore.removeItem(SLOT_INPUT, 1)

                      for (standPos <- standsWithItems.keySet) {
                        val be = level.getBlockEntity(standPos)

                        be match {
                          case stand: BookStandBlockEntity =>
                            stand.removeItem(1)
                        }
                      }

                      craftingCore.progress = 0

                      flag = true
                    }
                  }
                } yield ()

                if (flag) {
                  craftingCore.setChanged()

                  for {
                    pos <- standsWithItems.keySet
                    be <- Option(level.getBlockEntity(pos))
                  } {
                    be.setChanged()
                    level.sendBlockUpdated(pos, be.getBlockState, be.getBlockState, 2)
                  }
                }
              }
            case None =>
              craftingCore.progress = 0
          }
        }
      }
    } yield ()
  }

  /**
   * containerのslotとrecipeのgetResultItemを比較してレシピを格納可能なら格納する
   *
   * @param recipe          レシピ
   * @param recipeContainer レシピが格納されているコンテナ
   * @param container       結果を格納するべきコンテナ
   * @param slot            containerのスロット
   * @return 結果が格納できた場合true、失敗した場合falseを返す
   */
  protected def assemble[A <: Container, B <: Container](recipe: Recipe[A], recipeContainer: A, container: B, slot: Int): IO[Boolean] = IO {
    val containerItem = container.getItem(slot)
    val total = containerItem.getCount + recipe.getResultItem.getCount

    if (containerItem.isEmpty) {
      container.setItem(slot, recipe.assemble(recipeContainer))
      true
    }
    else if (ItemStack.isSameItemSameTags(containerItem, recipe.getResultItem)
      && total <= containerItem.getMaxStackSize) {
      containerItem.setCount(total)
      true
    } else false
  }
}