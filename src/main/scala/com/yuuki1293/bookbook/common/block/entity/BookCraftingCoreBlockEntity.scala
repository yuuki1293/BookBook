package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookCraftingCoreMenu
import com.yuuki1293.bookbook.common.recipe.BookCraftingRecipe
import com.yuuki1293.bookbook.common.register.{BlockEntities, MenuTypes, RecipeTypes}
import com.yuuki1293.bookbook.common.util.Ticked
import net.minecraft.core.particles.{ItemParticleOption, ParticleType, ParticleTypes}
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.{ContainerHelper, SimpleContainer, WorldlyContainer}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}
import net.minecraftforge.items.wrapper.SidedInvWrapper

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class BookCraftingCoreBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_CRAFTING_CORE.get(), worldPosition, blockState)
    with WorldlyContainer with Ticked {
  private var items = NonNullList.withSize(1, ItemStack.EMPTY)
  private val recipeItems = NonNullList.withSize(81, ItemStack.EMPTY)
  private var recipe: BookCraftingRecipe = _
  private val capacity = 100000000
  private val maxReceive = 10000000

  val energyStorage: BookEnergyStorage = createEnergyStorage
  private val energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => energyStorage)
  private var progress = 0
  private var standCount = 0
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
      pIndex match {
        case 0 => getEnergy
        case 1 => getMaxEnergy
        case 2 => getProgress
        case 3 => getPowerCost
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

  private def createEnergyStorage = {
    new BookEnergyStorage(this, capacity, maxReceive, 0)
  }

  def getEnergy: Int = energyStorage.getEnergyStored

  def getMaxEnergy: Int = energyStorage.getMaxEnergyStored

  def getProgress: Int = progress

  def getPowerCost: Int = ???

  override def getDefaultName: Component = new TranslatableComponent("container.book_crafting_core")

  override def createMenu(pContainerId: Int, pInventory: Inventory): AbstractContainerMenu = {
    new BookCraftingCoreMenu(MenuTypes.BOOK_CRAFTING_CORE.get(), pContainerId, pInventory, this, dataAccess)
  }

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(0)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = true

  override def tick(): Unit = ???

  override def getContainerSize: Int = items.size()

  override def isEmpty: Boolean = {
    for (item: ItemStack <- items.asScala) {
      if (!item.isEmpty)
        return false
    }
    true
  }

  override def getItem(pSlot: Int): ItemStack = items.get(pSlot)

  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ContainerHelper.removeItem(items, pSlot, pAmount)

  override def removeItemNoUpdate(pSlot: Int): ItemStack = ContainerHelper.takeItem(items, pSlot)

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = {
    items.set(pSlot, pStack)
    if (pStack.getCount > getMaxStackSize) {
      pStack.setCount(getMaxStackSize)
    }
  }

  override def stillValid(pPlayer: Player): Boolean = {
    if (level.getBlockEntity(worldPosition) != this) {
      false
    } else {
      pPlayer.distanceToSqr(worldPosition.getX.toDouble + 0.5D, worldPosition.getY.toDouble + 0.5D, worldPosition.getZ.toDouble + 0.5D) <= 64.0D
    }
  }

  override def clearContent(): Unit = items.clear()

  var handlers: Array[LazyOptional[IItemHandlerModifiable]] = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)

  override def getCapability[A](cap: Capability[A], side: Direction): LazyOptional[A] = {
    if (cap == CapabilityEnergy.ENERGY)
      energy.cast()
    else if (!remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (side == Direction.UP)
        handlers(0).cast()
      else if (side == Direction.DOWN)
        handlers(1).cast()
      else
        handlers(2).cast()
    }
    else
      super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    energy.invalidate()
    for (handler <- handlers)
      handler.invalidate()
  }

  override def reviveCaps(): Unit = {
    super.reviveCaps()
    handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    items = NonNullList.withSize(getContainerSize, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(pTag, items)
    energyStorage.setEnergy(pTag.getInt("Energy"))
    progress = pTag.getInt("Progress")
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("Energy", getEnergy)
    pTag.putInt("Progress", getProgress)
    ContainerHelper.saveAllItems(pTag, items)
  }

  def getStandWithItems: Map[BlockPos, ItemStack] = {
    val stands = mutable.HashMap[BlockPos, ItemStack]()
    val level = getLevel

    var standCount = 0
    if (level != null) {
      val pos = getBlockPos
      val positions = BlockPos.betweenClosedStream(pos.offset(-4, 0, -4), pos.offset(4, 0, 4))

      positions.forEach(
        aoePos => {
          val be = level.getBlockEntity(aoePos)

          be match {
            case stand: BookStandBlockEntity =>
              val stack = stand.getItem(0)
              standCount += 1
              if (!stack.isEmpty) {
                stands.put(aoePos.immutable(), stack)
              }
            case _ =>
          }
        })
    }

    this.standCount = standCount
    stands.toMap
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

    if (!flag)
      return

    recipeItems.set(0, items.get(0))

    for (i <- stacks.indices) {
      recipeItems.set(i + 1, stacks(i))
    }
  }

  private def process(recipe: BookCraftingRecipe): Boolean = {
    val powerRate = recipe.getPowerRate
    val difference = recipe.getPowerCost - progress

    val extract = Math.min(powerRate, difference)

    val extracted = energyStorage.extractEnergy(extract, simulate = false)
    progress += extracted

    progress >= recipe.getPowerCost
  }

  private def spawnItemParticles(standPos: BlockPos, itemStack: ItemStack): Unit = {
    if (getLevel == null || getLevel.isClientSide)
      return

    val level = getLevel.asInstanceOf[ServerLevel]
    val pos = getBlockPos

    val x = standPos.getX + (level.getRandom.nextDouble() * 0.2D) + 0.4D
    val y = standPos.getY + (level.getRandom.nextDouble() * 0.2D) + 1.4D
    val z = standPos.getZ + (level.getRandom.nextDouble() * 0.2D) + 1.4D

    val offsetX = pos.getX - standPos.getX
    val offsetY = 0.25D
    val offsetZ = pos.getX - standPos.getZ

    level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), x, y, z, 0, offsetX, offsetY, offsetZ, 0.18D)
  }
}

object BookCraftingCoreBlockEntity {
  final val SLOT = 0
  final val DATA_ENERGY_STORED = 0
  final val DATA_MAX_ENERGY = 1
  final val DATA_PROGRESS = 2
  final val DATA_POWER_COST = 3

  def tick(level: Level, pos: BlockPos, state: BlockState, craftingCore: BookCraftingCoreBlockEntity): Unit = {
    val standsWithItems = craftingCore.getStandWithItems
    val stacks = standsWithItems.values.toList

    craftingCore.updateRecipeInventory(stacks)

    val recipeItemContainer = new SimpleContainer(craftingCore.recipeItems.asScala.toSeq: _*)
    if (craftingCore.haveItemChanged && (craftingCore.recipe == null || !craftingCore.recipe.matches(recipeItemContainer, level))) {
      craftingCore.recipe = level.getRecipeManager.getRecipeFor(RecipeTypes.BOOK_CRAFTING, recipeItemContainer, level).orElse(null)
    }

    if (!level.isClientSide) {
      if (craftingCore.recipe != null) {
        if (craftingCore.energyStorage.getEnergyStored > 0) {
          val done = craftingCore.process(craftingCore.recipe)

          if (done) {
            for (standPos <- standsWithItems.keySet) {
              val be = level.getBlockEntity(standPos)

              be match {
                case stand: BookStandBlockEntity => {
                  stand.removeItem(0, 1)
                }
              }
            }

            craftingCore.setItem(0, craftingCore.recipe.assemble(recipeItemContainer))
            craftingCore.progress = 0
          } else {
            for (standPos <- standsWithItems.keySet) {
              val be = level.getBlockEntity(standPos)

              be match {
                case stand: BookStandBlockEntity => {
                  val itemStack = stand.getItem(0)

                  craftingCore.spawnItemParticles(standPos, itemStack)
                }
              }
            }
          }
        }
      } else {
        craftingCore.progress = 0
      }
    }
  }
}