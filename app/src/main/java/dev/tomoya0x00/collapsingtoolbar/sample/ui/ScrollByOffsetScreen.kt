package dev.tomoya0x00.collapsingtoolbar.sample.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar.CollapsingToolbarState
import dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar.LazyColumnWithToolbar
import dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar.rememberCollapsingToolbarState

@Composable
fun ScrollByOffsetScreen(
    onBack: () -> Unit,
) {
    val toolbarState: CollapsingToolbarState = rememberCollapsingToolbarState(
        minHeightDp = ToolbarMinHeight,
        maxHeightDp = ToolbarMaxHeight,
    )

    val collapsed: Boolean by remember {
        derivedStateOf {
            toolbarState.collapseFraction >= 1.0f
        }
    }

    LazyColumnWithToolbar(
        title = {
            Text(
                text = "very long title very long title very long title very long title very long title",
                maxLines = if (collapsed) 1 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back screen")
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
        },
        toolbarState = toolbarState,
    ) {
        items(100) { index ->
            Text(
                text = "I'm item $index",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

private val ToolbarMinHeight = 48.dp
private val ToolbarMaxHeight = 160.dp
