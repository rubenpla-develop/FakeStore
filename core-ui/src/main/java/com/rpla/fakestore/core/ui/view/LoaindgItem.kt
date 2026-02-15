package com.rpla.fakestore.core.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpla.fakestore.core.ui.theme.FakeStoreTheme
import com.rpla.fakestore.core.ui.theme.PinkA400
import com.rpla.fakestore.core.ui.view.testtags.TestTags.LOADING_ITEM

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .wrapContentSize(Alignment.Center)
                .testTag(
                    LOADING_ITEM,
                ),
        color = PinkA400,
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview_LoadingItem() {
    FakeStoreTheme {
        LoadingItem()
    }
}
