package dev.tomoya0x00.collapsingtoolbar.sample.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tomoya0x00.collapsingtoolbar.sample.ui.theme.ComposeCollapsingToolbarSampleTheme

@Composable
fun InitialScreen(
    onNavigateToTopContentPadding: () -> Unit,
    onNavigateToScrollByOffset: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SimpleTextButton(
                text = "TopContentPadding",
                onClick = onNavigateToTopContentPadding,
            )

            Spacer(modifier = Modifier.size(32.dp))

            SimpleTextButton(
                text = "ScrollByOffset",
                onClick = onNavigateToScrollByOffset,
            )
        }
    }
}

@Composable
private fun SimpleTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun PreviewInitialScreen() {
    ComposeCollapsingToolbarSampleTheme {
        Surface {
            InitialScreen(
                onNavigateToTopContentPadding = {},
                onNavigateToScrollByOffset = {},
            )

        }
    }
}