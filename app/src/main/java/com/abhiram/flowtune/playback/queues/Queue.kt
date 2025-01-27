package com.abhiram.flowtune.playback.queues

import com.abhiram.flowtune.models.MediaMetadata

interface Queue {
    val preloadItem: MediaMetadata?
    val playlistId: String?
    suspend fun getInitialStatus(): Status
    fun hasNextPage(): Boolean
    suspend fun nextPage(): List<MediaMetadata>

    data class Status(
        val title: String?,
        val items: List<MediaMetadata>,
        val mediaItemIndex: Int,
        val position: Long = 0L,
    )
}
