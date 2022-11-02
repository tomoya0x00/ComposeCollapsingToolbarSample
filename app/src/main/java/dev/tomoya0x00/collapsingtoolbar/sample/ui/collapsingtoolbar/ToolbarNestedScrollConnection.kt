package dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar

import androidx.compose.animation.SplineBasedFloatDecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ToolbarNestedScrollConnection(
    private val isReachedTop: () -> Boolean,
    private val toolbarState: CollapsingToolbarState,
    private val coroutineScope: CoroutineScope,
    private val density: Density,
) : NestedScrollConnection {

    private var job: Job? = null

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        if (isReachedTop().not()) {
            return Offset.Zero
        }

        return toolbarState.consumeScrollOffset(available)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (isReachedTop().not()) {
            return Offset.Zero
        }

        return toolbarState.consumeScrollOffset(available)
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity,
    ): Velocity {
        if (isReachedTop().not() || toolbarState.collapseFraction <= 0.0 || available.y <= 0) {
            return Velocity.Zero
        }

        // expand toolbar after fling down
        job?.cancel()
        job = coroutineScope.launch {
            var lastValue = 0f
            animateDecay(
                initialValue = 0f,
                initialVelocity = available.y,
                animationSpec = SplineBasedFloatDecayAnimationSpec(density),
            ) { value, _ ->
                if (toolbarState.collapseFraction <= 0.0) {
                    job?.cancel()
                }

                val delta = value - lastValue
                toolbarState.consumeScrollOffset(Offset(x = 0f, y = delta))
                lastValue = value
            }
        }

        return available
    }
}