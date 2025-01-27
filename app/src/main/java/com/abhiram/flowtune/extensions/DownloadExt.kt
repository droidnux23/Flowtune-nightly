package com.abhiram.flowtune.extensions

import androidx.media3.exoplayer.offline.Download

fun Download.isAvailableOffline(): Boolean {
    return this.state == Download.STATE_COMPLETED
}