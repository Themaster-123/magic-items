package me.sploky.magicitems.registries

import me.sploky.magicitems.watchers.Watcher

class WatcherRegistry {
    companion object {
        private val watchers = mutableListOf<Watcher>()

        fun registerWatcher(watcher: Watcher) {
            watchers.add(watcher)
            watcher.register()
        }
    }


}