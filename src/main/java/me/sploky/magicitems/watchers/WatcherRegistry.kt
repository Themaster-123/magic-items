package me.sploky.magicitems.watchers

class WatcherRegistry {
    companion object {
        val watchers = mutableListOf<Watcher>();

        fun registerWatcher(watcher: Watcher) {
            watchers.add(watcher)
            watcher.register()
        }
    }


}