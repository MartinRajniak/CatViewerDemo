package eu.rajniak.cat.android

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.performClick
import eu.rajniak.cat.CatsViewModel
import eu.rajniak.cat.android.ui.theme.CatViewerDemoTheme
import eu.rajniak.cat.android.viewer.CatsUI
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

// TODO: share hardcoded values so that we won't forget to change them
//  on both places
class CatsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMimeTypeFilter() {
        composeTestRule.setContent {
            CatViewerDemoTheme {
                CatsUI(CatsViewModel())
            }
        }

        // TODO: this is flaky because it might take longer than 5s to load
        //  and also we might get pictures without jpgs
        //  (running re-runs help with this)
        // Make sure there are some jpg pictures loaded
        composeTestRule.waitForAssertion() {
            composeTestRule
                .onAllNodesWithContentDescription("Cat")
                .assertAny(hasTestTag("jpg"))
        }

        composeTestRule.onNode(hasTestTag("DropdownIconButton")).performClick()
        // Filter out jpg
        composeTestRule.onNode(hasTestTag("2")).performClick()

        // Make sure there no jpgs anymore
        composeTestRule.waitForAssertion() {
            composeTestRule
                .onAllNodesWithContentDescription("Cat")
                .assertAll(!hasTestTag("jpg"))
        }
    }
}

fun ComposeTestRule.waitForAssertion(timeoutMillis: Long = TimeUnit.MINUTES.toMillis(1), assertion: () -> Unit) {
    waitUntil(timeoutMillis) {
        try {
            assertion.invoke()
            true
        } catch (error: AssertionError) {
            false
        }
    }
}
