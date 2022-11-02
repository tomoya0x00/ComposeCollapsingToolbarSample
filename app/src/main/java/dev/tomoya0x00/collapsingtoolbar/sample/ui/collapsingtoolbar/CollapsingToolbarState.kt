package dev.tomoya0x00.collapsingtoolbar.sample.ui.collapsingtoolbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Stable
class CollapsingToolbarState(
    val minHeight: Int,
    val maxHeight: Int,
    offset: Float = (maxHeight - minHeight).toFloat(),
) {
    private val maxOffset: Float = (maxHeight - minHeight).toFloat()
    private var _offset: Float by mutableStateOf(offset.coerceIn(0f, maxOffset))
    private var _collapseFraction: Float by mutableStateOf(calcCollapseFraction())

    // how much the toolbar extends from its collapsed state
    val offset: Float
        get() = _offset

    private fun calcCollapseFraction(): Float = 1.0f - (_offset / maxOffset)

    // fraction of collapse progress, 1 -> fully collapsed, 0 -> fully expanded
    val collapseFraction: Float
        get() = _collapseFraction

    var navigationIconWidth by mutableStateOf(0)

    var actionsWidth by mutableStateOf(0)

    /**
     * consume a offset and return a consumed offset
     */
    fun consumeScrollOffset(available: Offset): Offset {
        val prevOffset = _offset
        _offset = (_offset + available.y).coerceIn(0f, maxOffset)
        _collapseFraction = calcCollapseFraction()
        val consumed = _offset - prevOffset
        return Offset(0f, consumed)
    }

    companion object {
        val Saver = run {
            val minHeightKey = "MinHeight"
            val maxHeightKey = "MaxHeight"
            val offsetKey = "Offset"
            mapSaver(
                save = {
                    mapOf(
                        minHeightKey to it.minHeight,
                        maxHeightKey to it.maxHeight,
                        offsetKey to it.offset,
                    )
                },
                restore = {
                    CollapsingToolbarState(
                        minHeight = it[minHeightKey] as Int,
                        maxHeight = it[maxHeightKey] as Int,
                        offset = it[offsetKey] as Float,
                    )
                }
            )
        }
    }
}

@Composable
fun rememberCollapsingToolbarState(
    minHeightDp: Dp,
    maxHeightDp: Dp,
): CollapsingToolbarState {
    val minHeight = with(LocalDensity.current) {
        minHeightDp.roundToPx()
    }
    val maxHeight = with(LocalDensity.current) {
        maxHeightDp.roundToPx()
    }
    return rememberSaveable(saver = CollapsingToolbarState.Saver) {
        CollapsingToolbarState(
            minHeight = minHeight,
            maxHeight = maxHeight,
        )
    }
}
