package com.abhiram.flowtune.models

import com.abhiram.flowtune.db.entities.FormatEntity
import com.abhiram.flowtune.db.entities.Song

/**
 * For passing along song metadata
 */
data class SongTempData(val song: Song, val format: FormatEntity?)