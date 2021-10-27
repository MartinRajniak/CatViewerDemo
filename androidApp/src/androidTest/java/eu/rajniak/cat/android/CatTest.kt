package eu.rajniak.cat.android

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

// TODO: share hardcoded values so that we won't forget to change them
//  on both places
// TODO: flaky because mime type selection is persisted
class CatTest {

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
        //  and also we might get pictures without gifs
        // Make sure there are some gif pictures loaded
        composeTestRule.waitForAssertion(5000) {
            composeTestRule
                .onAllNodesWithContentDescription("Cat")
                .assertAny(hasTestTag("gif"))
        }

        composeTestRule.waitForAssertion(5000) {
            composeTestRule.onNode(hasTestTag("DropdownIconButton")).performClick()
            composeTestRule.onNode(hasTestTag("1")).performClick()
        }

        // Make sure there no gifs anymore
        composeTestRule.waitForAssertion(5000) {
            composeTestRule
                .onAllNodesWithContentDescription("Cat")
                .assertAny(!hasTestTag("gif"))
        }
    }
}

fun ComposeTestRule.waitForAssertion(timeoutMillis: Long = 60_000L, assertion: () -> Unit) {
    waitUntil(timeoutMillis) {
        try {
            assertion.invoke()
            true
        } catch (error: AssertionError) {
            false
        }
    }
}
