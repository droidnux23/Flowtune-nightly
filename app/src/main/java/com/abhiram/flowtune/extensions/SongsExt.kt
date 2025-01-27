package com.abhiram.flowtune.extensions

import com.abhiram.flowtune.db.entities.Song

fun List<Song>.getAvailableSongs(isInternetConnected: Boolean): List<Song> {
    if (isInternetConnected) {
        return this
    }
    return filter { it.song.isAvailableOffline() }
}