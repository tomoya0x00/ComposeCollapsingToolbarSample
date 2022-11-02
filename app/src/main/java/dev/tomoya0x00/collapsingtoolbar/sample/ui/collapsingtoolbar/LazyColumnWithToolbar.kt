package dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LazyColumnWithToolbar(
    // region params for LazyColumn
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    // endregion

    toolbarState: CollapsingToolbarState = rememberCollapsingToolbarState(
        minHeightDp = ToolbarMinHeight,
        maxHeightDp = ToolbarMaxHeight,
    ),
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    stickyContent: (@Composable () -> Unit)? = null,

    content: LazyListScope.() -> Unit,
) {
    ToolbarWithScrollableContent(
        modifier = modifier,
        toolbarState = toolbarState,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        stickyContent = stickyContent,
        isReachedTop = { listState.isReachedTop() },
    ) {
        LazyColumn(
            state = listState,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            content = content,
        )
    }
}

private fun LazyListState.isReachedTop() =
    firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0

private val ToolbarMinHeight = 48.dp
private val ToolbarMaxHeight = 160.dp
