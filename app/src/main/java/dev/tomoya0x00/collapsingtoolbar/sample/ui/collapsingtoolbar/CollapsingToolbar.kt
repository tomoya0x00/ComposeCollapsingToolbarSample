package dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun CollapsingToolbar(
    modifier: Modifier,
    toolbarState: CollapsingToolbarState,
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    actions: @Composable RowScope.() -> Unit = {},
) {
    val toolbarMaxHeight = with(LocalDensity.current) {
        toolbarState.maxHeight.toDp()
    }
    val toolbarMinHeight = with(LocalDensity.current) {
        toolbarState.minHeight.toDp()
    }

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
    ) {
        Box(modifier = modifier.height(toolbarMaxHeight)) {
            Background(
                backgroundColor = backgroundColor,
                toolbarState = toolbarState,
            )
            ToolbarWithoutTitle(
                toolbarMinHeight = toolbarMinHeight,
                navigationIcon = navigationIcon,
                actions = actions,
                toolbarState = toolbarState,
            )
            ToolbarTitle(
                toolbarMinHeight = toolbarMinHeight,
                title = title,
                toolbarState = toolbarState,
            )
        }
    }
}

@Composable
private fun Background(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    toolbarState: CollapsingToolbarState,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                onDrawBehind {
                    drawRect(
                        color = backgroundColor,
                        size = Size(
                            width = size.width,
                            height = toolbarState.minHeight + toolbarState.offset,
                        ),
                    )
                }
            },
    )
}

@Composable
private fun ToolbarWithoutTitle(
    modifier: Modifier = Modifier,
    toolbarMinHeight: Dp,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    toolbarState: CollapsingToolbarState,
) {
    Box(
        modifier = modifier
            .height(toolbarMinHeight)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .onSizeChanged {
                    toolbarState.navigationIconWidth = it.width
                },
        ) {
            navigationIcon?.invoke()
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onSizeChanged {
                    toolbarState.actionsWidth = it.width
                },
        ) {
            actions()
        }
    }
}

@Composable
private fun ToolbarTitle(
    modifier: Modifier = Modifier,
    toolbarMinHeight: Dp,
    title: @Composable () -> Unit,
    toolbarState: CollapsingToolbarState,
) {
    val almostCollapsed: Boolean by remember {
        derivedStateOf {
            toolbarState.offset < toolbarState.minHeight
        }
    }

    Row(
        modifier = modifier
            .height(toolbarMinHeight)
            .graphicsLayer {
                translationX = toolbarState.navigationIconWidth * toolbarState.collapseFraction
                translationY = toolbarState.offset
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Layout(content = title) { measurables, constraints ->
            val maxWidth = if (almostCollapsed) {
                constraints.maxWidth - toolbarState.navigationIconWidth - toolbarState.actionsWidth
            } else {
                constraints.maxWidth
            }
            var height = 0
            val placeables = measurables.map { measurable ->
                measurable.measure(
                    constraints.copy(
                        maxWidth = maxWidth,
                    ),
                ).also { placeable ->
                    height = maxOf(height, placeable.height)

                }
            }

            layout(
                width = constraints.maxWidth,
                height = height,
            ) {
                placeables.forEach { placeable ->
                    placeable.placeRelative(
                        x = 0,
                        y = 0,
                    )
                }
            }
        }
    }
}
