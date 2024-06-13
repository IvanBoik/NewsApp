package com.boiko.newsapp.presentation.music_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.boiko.newsapp.R
import com.boiko.newsapp.data.remote.dto.Song
import com.boiko.newsapp.presentation.Dimens

@Composable
fun MusicScreen(
    viewModel: MusicViewModel
) {
    val songs = viewModel.state.value.songs
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                top = Dimens.MediumPadding1,
                start = Dimens.MediumPadding1,
                end = Dimens.MediumPadding1
            )
    ) {
        Text(
            text = "Music",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.text_title)
        )

        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SongsList(songs = songs) {
                selectedSong = it
            }
            selectedSong?.let {
                MediaPlayerCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Transparent),
                    it
                )
            }
        }
    }
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onClick: (Song) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding1),
        contentPadding = PaddingValues(all = Dimens.ExtraSmallPadding)
    ) {
        items(count = songs.size) {
            val song = songs[it]
            SongCard(song = song, onClick = { onClick(song) })
        }
    }
}

@Composable
fun SongCard(
    song: Song,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(song.imageURL).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Dimens.ArticleCardSize)
                .clip(MaterialTheme.shapes.medium)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .height(Dimens.ArticleCardSize)
        ) {
            Text(
                text = song.name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = colorResource(id = R.color.text_title)
            )
            Text(
                text = song.author,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = colorResource(id = R.color.body)
            )
        }
    }
}

@Composable
fun MediaPlayerCard(modifier: Modifier = Modifier, song: Song) {
    var songState by remember { mutableStateOf(true) }
    if(songState) {
        SongHelper.playStream(song.audioURL)
    } else {
        SongHelper.pauseStream()
    }

    Card(modifier = modifier, elevation = CardDefaults.cardElevation(4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = song.imageURL,
                contentDescription = "Song thumbnail",
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.author,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = if (songState) {
                    Icons.Filled.Pause
                } else {
                    Icons.Filled.PlayArrow
                },
                contentDescription = "Play/Pause",
                modifier = Modifier.clickable {
                    songState = !songState
                })
        }
    }

    DisposableEffect(song.audioURL){
        onDispose {
            songState = false
            SongHelper.releasePlayer()
        }
    }

}