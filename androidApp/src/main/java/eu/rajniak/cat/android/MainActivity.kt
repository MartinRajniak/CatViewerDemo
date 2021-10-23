package eu.rajniak.cat.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import eu.rajniak.cat.CatsViewModel
import eu.rajniak.cat.android.ui.theme.CatViewerDemoTheme
import eu.rajniak.cat.android.viewer.CatsUI

class MainActivity : ComponentActivity() {

    private val viewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CatViewerDemoTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    CatsUI(viewModel)
                }
            }
        }
    }
}
