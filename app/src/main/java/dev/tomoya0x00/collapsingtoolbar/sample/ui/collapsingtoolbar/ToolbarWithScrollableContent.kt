package dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolbarWithScrollableContent(
    modifier: Modifier = Modifier,
    toolbarState: CollapsingToolbarState,
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    stickyContent: (@Composable () -> Unit)? = null,
    isReachedTop: () -> Boolean,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val nestedScrollConnection = remember(
        isReachedTop,
        toolbarState,
        scope,
        density,
    ) {
        ToolbarNestedScrollConnection(
            isReachedTop = isReachedTop,
            toolbarState = toolbarState,
            coroutineScope = scope,
            density = density,
        )
    }

    val toolbarMinHeight = with(LocalDensity.current) {
        toolbarState.minHeight.toDp()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        CollapsingToolbar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            toolbarState = toolbarState,
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            backgroundColor = Color.Green,
            contentColor = Color.Black,
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .padding(bottom = toolbarMinHeight)
                .graphicsLayer {
                    translationY = toolbarState.minHeight + toolbarState.offset
                },
        ) {
            stickyContent?.invoke()

            content()
        }
    }
}
