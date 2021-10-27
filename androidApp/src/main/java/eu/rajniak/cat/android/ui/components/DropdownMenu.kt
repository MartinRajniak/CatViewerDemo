package eu.rajniak.cat.android.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/**
 * Version of [androidx.compose.material.DropdownMenu] with icon.
 */
@Composable
fun DropdownMenu(
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(
        onClick = { showMenu = true },
        modifier = Modifier.testTag("DropdownIconButton")
    ) {
        icon()
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        content()
    }
}
