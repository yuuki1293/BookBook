package com.yuuki1293.bookbook.common.register

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

trait AbstractRegister[T] {
  protected val REGISTER: DeferredRegister[T]

  def registry(implicit eventBus: IEventBus): Unit = REGISTER.register(eventBus)
}
