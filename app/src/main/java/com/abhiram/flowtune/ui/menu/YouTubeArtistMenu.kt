package com.abhiram.flowtune.ui.menu

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.abhiram.flowtune.LocalDatabase
import com.abhiram.flowtune.LocalPlayerConnection
import com.abhiram.flowtune.R
import com.abhiram.flowtune.db.entities.ArtistEntity
import com.abhiram.flowtune.playback.queues.YouTubeQueue
import com.abhiram.flowtune.ui.component.GridMenu
import com.abhiram.flowtune.ui.component.GridMenuItem
import com.abhiram.flowtune.ui.component.YouTubeListItem
import com.zionhuang.innertube.models.ArtistItem

@Composable
fun YouTubeArtistMenu(
    artist: ArtistItem,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val database = LocalDatabase.current
    val playerConnection = LocalPlayerConnection.current ?: return
    val libraryArtist by database.artist(artist.id).collectAsState(initial = null)

    YouTubeListItem(
        item = artist,
        trailingContent = {
            IconButton(
                onClick = {
                    database.query {
                        val libraryArtistLocal = libraryArtist
                        if (libraryArtistLocal != null) {
                            update(libraryArtistLocal.artist.toggleLike())
                        } else {
                            insert(
                                ArtistEntity(
                                    id = artist.id,
                                    name = artist.title,
                                    channelId = artist.channelId,
                                    thumbnailUrl = artist.thumbnail,
                                ).toggleLike()
                            )
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(if (libraryArtist?.artist?.bookmarkedAt != null) R.drawable.favorite else R.drawable.favorite_border),
                    tint = if (libraryArtist?.artist?.bookmarkedAt != null) MaterialTheme.colorScheme.error else LocalContentColor.current,
                    contentDescription = null
                )
            }

        }
    )

    HorizontalDivider()

    GridMenu(
        contentPadding = PaddingValues(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 8.dp + WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
        )
    ) {
        artist.radioEndpoint?.let { watchEndpoint ->
            GridMenuItem(
                icon = Icons.Rounded.Radio,
                title = R.string.start_radio
            ) {
                playerConnection.playQueue(YouTubeQueue(watchEndpoint))
                onDismiss()
            }
        }
        artist.shuffleEndpoint?.let { watchEndpoint ->
            GridMenuItem(
                icon = Icons.Rounded.Shuffle,
                title = R.string.shuffle
            ) {
                playerConnection.playQueue(YouTubeQueue(watchEndpoint))
                onDismiss()
            }
        }
        GridMenuItem(
            icon = Icons.Rounded.Share,
            title = R.string.share
        ) {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, artist.shareLink)
            }
            context.startActivity(Intent.createChooser(intent, null))
            onDismiss()
        }
    }
}
