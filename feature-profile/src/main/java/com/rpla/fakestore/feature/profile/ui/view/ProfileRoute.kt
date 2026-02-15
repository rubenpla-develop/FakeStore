package com.rpla.fakestore.feature.profile.ui.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rpla.fakestore.feature.profile.ui.viewmodel.ProfileIntent
import com.rpla.fakestore.feature.profile.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileRoute(
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dispatchIntent(ProfileIntent.LoadProfile)
    }

    ProfileScreen(
        paddingValues = paddingValues,
        state = state,
        onRetry = { viewModel.dispatchIntent(ProfileIntent.Retry) },
    )
}
