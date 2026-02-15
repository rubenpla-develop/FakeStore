package com.rpla.fakestore.feature.profile.ui.viewmodel

import com.rpla.fakestore.core.ui.view.viewmodel.ViewAction

sealed interface ProfileAction : ViewAction {
    data object LoadProfile : ProfileAction
}
