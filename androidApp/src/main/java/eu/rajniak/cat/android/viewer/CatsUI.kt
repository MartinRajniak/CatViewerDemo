package eu.rajniak.cat.android.viewer

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
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
import eu.rajniak.cat.android.ui.components.DropdownMenu
import eu.rajniak.cat.android.ui.components.DropdownMenuItem
import eu.rajniak.cat.android.ui.components.TopAppBar
import eu.rajniak.cat.android.ui.theme.CatViewerDemoTheme
import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.CategoryModel
import eu.rajniak.cat.data.FakeData
import eu.rajniak.cat.data.MimeTypeModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun CatsUI(viewModel: CatsViewModel) {
    val cats by viewModel.cats.collectAsState(listOf())
    val categories by viewModel.categories.collectAsState(listOf())
    val mimeTypes by viewModel.mimeTypes.collectAsState(listOf())
    CatsUI(
        cats = cats,
        categories = categories,
        onCategoryChecked = { categoryId, checked ->
            viewModel.onCategoryChecked(categoryId, checked)
        },
        mimeTypes = mimeTypes,
        onMimeTypeChecked = { mimeTypeId, checked ->
            viewModel.onMimeTypeChecked(mimeTypeId, checked)
        },
        onScrolledToTheEnd = { viewModel.onScrolledToTheEnd() }
    )
}

@Composable
fun CatsUI(
    cats: List<Cat>,
    categories: List<CategoryModel>,
    onCategoryChecked: (Int, Boolean) -> Unit,
    mimeTypes: List<MimeTypeModel>,
    onMimeTypeChecked: (Int, Boolean) -> Unit,
    onScrolledToTheEnd: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    contentPadding = rememberInsetsPaddingValues(
                        insets = LocalWindowInsets.current.statusBars,
                        applyStart = true,
                        applyTop = true,
                        applyEnd = true,
                    ),
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
                        Filter(
                            categories = categories,
                            onCategoryChecked = onCategoryChecked,
                            mimeTypes = mimeTypes,
                            onMimeTypeChecked = onMimeTypeChecked
                        )
                    },
                )
            },
            bottomBar = {
                Spacer(
                    Modifier
                        .navigationBarsHeight()
                        .fillMaxWidth()
                )
            },
        ) { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                CatsList(
                    cats = cats,
                    onScrolledToTheEnd = onScrolledToTheEnd
                )
            }
        }
    }
}

@Composable
private fun Filter(
    categories: List<CategoryModel>,
    onCategoryChecked: (Int, Boolean) -> Unit,
    mimeTypes: List<MimeTypeModel>,
    onMimeTypeChecked: (Int, Boolean) -> Unit,
) {
    DropdownMenu(
        icon = {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Filter",
            )
        }
    ) {
        CategoryFilter(
            categories = categories,
            onCategoryChecked = onCategoryChecked
        )

        Divider()

        MimeTypeFilter(
            mimeTypes = mimeTypes,
            onMimeTypeChecked = onMimeTypeChecked
        )
    }
}

// TODO: since cat can have multiple categories,
//  one category can be enabled while other disabled,
//  current behaviour of filtering such item
//  might not be intuitive (improve UI)
@Composable
private fun CategoryFilter(
    categories: List<CategoryModel>,
    onCategoryChecked: (Int, Boolean) -> Unit,
) {
    categories.forEach { category ->
        DropdownMenuItem {
            Checkbox(
                checked = category.enabled,
                onCheckedChange = { checked ->
                    onCategoryChecked(category.id, checked)
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(category.name)
        }
    }
}

@Composable
private fun MimeTypeFilter(
    mimeTypes: List<MimeTypeModel>,
    onMimeTypeChecked: (Int, Boolean) -> Unit,
) {
    mimeTypes.forEach { mimeType ->
        DropdownMenuItem {
            Checkbox(
                checked = mimeType.enabled,
                onCheckedChange = { checked ->
                    onMimeTypeChecked(mimeType.id, checked)
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(mimeType.name)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatsList(
    cats: List<Cat>,
    onScrolledToTheEnd: () -> Unit
) {
    val listState = rememberLazyListState()

    LazyVerticalGrid(
        state = listState,
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        itemsIndexed(cats) { _, cat ->
            CatItem(cat)
        }
        item {
            CircularProgressIndicator(
                // TODO: specify size instead to make progress smaller
                modifier = Modifier.padding(48.dp)
            )
        }
    }

    if (cats.isNotEmpty()) {
        // TODO: use pagination 3 library (once we have support from shared library)
        LaunchedEffect(listState) {
            snapshotFlow { listState.isScrolledToTheEnd() }
                .distinctUntilChanged()
                .filter { it == true }
                .collect {
                    onScrolledToTheEnd()
                }
        }
    }
}

fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

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
            cats = FakeData.cats,
            categories = listOf(),
            onCategoryChecked = { _, _ -> },
            mimeTypes = listOf(),
            onMimeTypeChecked = { _, _ -> },
            onScrolledToTheEnd = { }
        )
    }
}
