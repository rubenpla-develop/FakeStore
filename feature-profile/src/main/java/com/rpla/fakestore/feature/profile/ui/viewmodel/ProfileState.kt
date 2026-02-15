package com.rpla.fakestore.feature.profile.ui.viewmodel

import com.rpla.fakestore.core.ui.view.viewmodel.ViewState
import com.rpla.fakestore.feature.profile.domain.model.Profile

sealed interface ProfileState : ViewState {
    data object Initial : ProfileState

    data object Loading : ProfileState

    data class Error(
        val message: String,
    ) : ProfileState

    data class Data(
        val profile: Profile,
        val favoritesCount: Int,
    ) : ProfileState
}
