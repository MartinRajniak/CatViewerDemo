package eu.rajniak.cat.android.viewer

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import eu.rajniak.cat.CatsViewModel
import eu.rajniak.cat.android.R
import eu.rajniak.cat.android.ui.theme.CatViewerDemoTheme
import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.FakeData

@Composable
fun ViewerUI(viewModel: CatsViewModel) {
    val cats by viewModel.cats.collectAsState()
    ViewerUI(
        cats = cats
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewerUI(
    cats: List<Cat>
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        itemsIndexed(cats) { _, cat ->
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
        ViewerUI(
            cats = FakeData.cats
        )
    }
}