package eu.rajniak.cat.android.viewer

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import eu.rajniak.cat.CatsViewModel
import eu.rajniak.cat.android.R
import eu.rajniak.cat.android.ui.components.TopAppBar
import eu.rajniak.cat.android.ui.theme.CatViewerDemoTheme
import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.FakeData

@Composable
fun CatsUI(viewModel: CatsViewModel) {
    val cats by viewModel.cats.collectAsState(listOf())
    CatsUI(
        cats = cats
    )
}

@Composable
fun CatsUI(
    cats: List<Cat>
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Menu, "menu")
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            color = MaterialTheme.colors.onPrimary,
                        )
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "More",
                                modifier = Modifier
                                    .size(32.dp)
                                    .rotate(90f)
                            )
                        }
                    },
                    contentPadding = rememberInsetsPaddingValues(
                        insets = LocalWindowInsets.current.statusBars,
                        applyStart = true,
                        applyTop = true,
                        applyEnd = true,
                    ),
                )
            },
            bottomBar = {
                // We add a spacer as a bottom bar, which is the same height as
                // the navigation bar
                Spacer(
                    Modifier
                        .navigationBarsHeight()
                        .fillMaxWidth()
                )
            },
        ) { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                CatsList(cats = cats)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatsList(
    cats: List<Cat>
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        // TODO: remove list multiplier once real data is used
        itemsIndexed(List(10) { cats }.flatten()) { _, cat ->
            CatItem(cat)
        }
    }
}

@Composable
fun CatItem(cat: Cat) {
    val context = LocalContext.current
    Image(
        painter = rememberImagePainter(
            data = cat.url,
            imageLoader = ImageLoader.Builder(context).componentRegistry {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(context))
                } else {
                    add(GifDecoder())
                }
            }.build(),
            onExecute = { _, _ -> true },
            builder = {
                // cannot use crossfade - won't display gif correctly
                placeholder(R.drawable.ic_launcher_foreground)
                // cannot use modifiers - will make gif to static image
            }
        ),
        modifier = Modifier.size(128.dp),
        contentDescription = null // TODO: we can fetch something interesting about the image
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CatViewerDemoTheme {
        CatsUI(
            cats = FakeData.cats
        )
    }
}
