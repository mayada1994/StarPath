package com.mayada1994.starpath.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.ObjectMap
import com.mayada1994.starpath.ecs.component.ItemComponent.ItemType
import ktx.collections.GdxSet
import kotlin.reflect.KClass

class Event {

    sealed class GameEvent {
        object PlayerDeath : GameEvent() {
            var points = 0

            override fun toString() = "PlayerDeath(points=$points)"
        }

        object CollectBonus : GameEvent() {
            lateinit var player: Entity
            var type: ItemType = ItemType.None
            var points = 0

            override fun toString() = "CollectBonus(player=$player, type=$type, score=$points)"
        }
    }

    interface GameEventListener {
        fun onEvent(event: GameEvent)
    }

    class GameEventManager {
        private val listeners = ObjectMap<KClass<out GameEvent>, GdxSet<GameEventListener>>()

        fun addListener(type: KClass<out GameEvent>, listener: GameEventListener) {
            var eventListeners = listeners[type]
            if (eventListeners == null) {
                eventListeners = GdxSet()
                listeners.put(type, eventListeners)
            }
            eventListeners.add(listener)
        }

        fun removeListener(type: KClass<out GameEvent>, listener: GameEventListener) {
            val eventListeners = listeners[type]
            eventListeners?.remove(listener)
        }

        fun removeListener(listener: GameEventListener) {
            listeners.values().forEach { it.remove(listener) }
        }

        fun dispatchEvent(type: GameEvent) {
            listeners[type::class]?.forEach { it.onEvent(type) }
        }
    }

}