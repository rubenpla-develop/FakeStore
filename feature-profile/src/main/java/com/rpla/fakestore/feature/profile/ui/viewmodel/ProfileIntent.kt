package com.rpla.fakestore.feature.profile.ui.viewmodel

import com.rpla.fakestore.core.ui.view.viewmodel.ViewIntent

sealed interface ProfileIntent : ViewIntent {
    data object LoadProfile : ProfileIntent

    data object Retry : ProfileIntent
}
