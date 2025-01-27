package com.abhiram.flowtune.playback.queues

import com.abhiram.flowtune.models.MediaMetadata

object EmptyQueue : Queue {
    override val preloadItem: MediaMetadata? = null
    override val playlistId: String? = null
    override suspend fun getInitialStatus() = Queue.Status(null, emptyList(), -1)
    override fun hasNextPage() = false
    override suspend fun nextPage() = emptyList<MediaMetadata>()
}