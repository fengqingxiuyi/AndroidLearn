package com.example.utils.other

import androidx.annotation.VisibleForTesting
import java.lang.ref.WeakReference
import java.util.*

/**
 *  description : 活动缓存 通过弱引用来缓存数据
 */
class ActiveResources<Key, Value> {

  @VisibleForTesting
  val activeEngineResources: MutableMap<Key, ResourceWeakReference<Key, Value>> = HashMap()

  @Synchronized
  fun activate(key: Key, resource: Value) {
    val toPut = ResourceWeakReference(
      key, resource
    )
    val removed = activeEngineResources.put(key, toPut)
    removed?.clear()
  }

  @Synchronized
  fun deactivate(key: Key) {
    val removed = activeEngineResources.remove(key)
    removed?.clear()
  }

  @Synchronized
  operator fun get(key: Key): Value? {
    val activeRef = activeEngineResources[key] ?: return null
    val active = activeRef.get()
    if (active == null) {
      cleanupActiveReference(activeRef)
    }
    return active
  }

  private fun cleanupActiveReference(ref: ResourceWeakReference<Key, Value>) {
    synchronized(this) {
      activeEngineResources.remove(ref.key)
    }
  }

  fun clear() {
    synchronized(this) {
      activeEngineResources.clear()
    }
  }

  fun values() = synchronized(this) {
    activeEngineResources.values
  }

  @VisibleForTesting
  class ResourceWeakReference<Key, Value> constructor(
    val key: Key,
    referent: Value,
  ) :
    WeakReference<Value>(referent)

}