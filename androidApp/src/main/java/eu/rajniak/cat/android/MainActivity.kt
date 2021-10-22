package eu.rajniak.cat.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.rajniak.cat.Greeting
import eu.rajniak.cat.android.ui.theme.CatViewerDemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CatViewerDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingUI(Greeting().greeting())
                }
            }
        }
    }
}

@Composable
fun GreetingUI(greeting: String) {
    Text(text = greeting)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CatViewerDemoTheme {
        GreetingUI("Hello Android!")
    }
}