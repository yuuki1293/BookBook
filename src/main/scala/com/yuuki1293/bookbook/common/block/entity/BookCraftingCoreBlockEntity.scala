package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.BookCraftingCoreBlockEntity._
import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookCraftingCoreMenu
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import com.yuuki1293.bookbook.common.register.{BlockEntities, MenuTypes, RecipeTypes}
import com.yuuki1293.bookbook.common.util.RecipeUtil
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
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
     */
    override def get(pIndex: Int): Int = {
      pIndex match {
        case DATA_ENERGY_STORED => getEnergy
        case DATA_MAX_ENERGY => getMaxEnergy
        case DATA_PROGRESS => getProgress
        case DATA_POWER_COST => getPowerCost
        case _ => throw new UnsupportedOperationException("Unable to get index: " + pIndex)
      }
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

  def getEnergy: Int = energyStorage.getEnergyStored

  def getMaxEnergy: Int = energyStorage.getMaxEnergyStored

  def getPowerCost: Int = recipe.map(_.getPowerCost).getOrElse(0)

  def getProgress: Int = progress

  override def getDefaultName: Component =
    new TranslatableComponent("container.bookbook.book_crafting_core")

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
    energyStorage.setEnergy(pTag.getInt("Energy"))
    progress = pTag.getInt("Progress")

  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("Energy", getEnergy)
    pTag.putInt("Progress", progress)
  }

  def getStandWithItems: Map[BlockPos, ItemStack] = {
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

  def updateRecipeInventory(stacks: List[ItemStack]): Unit = {
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

  def updateRecipe(stacks: List[ItemStack], recipeContainer: SimpleContainer): Unit = {
    updateRecipeInventory(stacks)

    if (haveItemChanged && (recipe.isEmpty || !recipe.exists(_.matches(recipeContainer, level)))) {
      recipe =
        Option(level
          .getRecipeManager
          .getRecipeFor(RecipeTypes.BOOK_CRAFTING, recipeContainer, level)
          .orElse(null))
    }
  }

  private def process(recipe: BookCraftingRecipe): Unit = {
    val powerRate = recipe.getPowerRate
    val difference = recipe.getPowerCost - progress

    val extract = Math.min(powerRate, difference)

    val extracted = energyStorage.extractEnergy(extract, simulate = false)
    progress += extracted
  }

  def isDone: Boolean = progress >= getPowerCost

  def recipeTick(): Unit = {
    if (!level.isClientSide) {

      var hasChanged = false

      val standsWithItems = getStandWithItems
      val stacks = standsWithItems.values.toList
      val recipeItemContainer = new SimpleContainer(recipeItems.asScala.toSeq: _*)

      updateRecipe(stacks, recipeItemContainer)

      recipe match {
        case Some(recipe) =>
          if (getEnergy > 0) {
            if (!isDone)
              process(recipe)

            val result = RecipeUtil.assemble(recipe, recipeItemContainer, this, SLOT_OUTPUT)
            if (isDone && result) {
              removeItem(SLOT_INPUT, 1)

              for (standPos <- standsWithItems.keySet) {
                val be = level.getBlockEntity(standPos)

                be match {
                  case stand: BookStandBlockEntity =>
                    stand.removeItem(1)
                }
              }

              progress = 0

              hasChanged = true
            }
          }
        case None =>
          progress = 0

          if (hasChanged) {
            setChanged()
            updateStands()
          }
      }
    }
  }

  def updateStands(): Unit = {
    for {
      pos <- getStandWithItems.keySet
      be <- Option(level.getBlockEntity(pos))
    } {
      be.setChanged()
      level.sendBlockUpdated(pos, be.getBlockState, be.getBlockState, 2)
    }
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

  override def tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BookCraftingCoreBlockEntity): Unit = {
    blockEntity.recipeTick()
  }
}