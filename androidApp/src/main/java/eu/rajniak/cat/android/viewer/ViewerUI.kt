package eu.rajniak.cat.android.viewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import eu.rajniak.cat.android.R
import eu.rajniak.cat.android.data.Cat
import eu.rajniak.cat.android.data.FakeData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewerUI() {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        itemsIndexed(FakeData.cats) { _, cat ->
            CatItem(cat)
        }
    }
}

@Composable
fun CatItem(cat: Cat) {
    Image(
        painter = rememberImagePainter(
            data = cat.url,
            onExecute = { _, _ -> true },
            builder = {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                transformations(RoundedCornersTransformation())
            }
        ),
        modifier = Modifier.size(128.dp),
        contentDescription = null // TODO: we can fetch something interesting about the image
    )
}
