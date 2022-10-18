package com.yuuki1293.bookbook.common.register

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

import scala.annotation.unused

trait AbstractRegister[A] {
  protected val REGISTER: DeferredRegister[A]

  @unused
  def registry(implicit eventBus: IEventBus): Unit = REGISTER.register(eventBus)
}
