package com.rpla.fakestore.feature.profile.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rpla.fakestore.core.coroutines.dispatcher.IODispatcher
import com.rpla.fakestore.core.domain.usecase.favorites.ObserveFavoritesCountUseCase
import com.rpla.fakestore.core.ui.view.viewmodel.BaseViewModel
import com.rpla.fakestore.feature.profile.domain.GetProfileRequest
import com.rpla.fakestore.feature.profile.domain.model.Profile
import com.rpla.fakestore.feature.profile.domain.usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val getProfileUseCase: GetProfileUseCase,
        private val observeFavoritesCountUseCase: ObserveFavoritesCountUseCase,
        @IODispatcher private val dispatcher: CoroutineDispatcher,
    ) : BaseViewModel<ProfileIntent, ProfileAction, ProfileState>() {
        private val profileFlow = MutableStateFlow<Profile?>(null)
        private var favoritesCount: Int = 0

        private var observeFavoritesJob: Job? = null
        private var renderJob: Job? = null

        private var loadedOnce = false

        override fun createInitialState(): ProfileState = ProfileState.Initial

        override fun mapIntentToAction(intent: ProfileIntent): ProfileAction =
            when (intent) {
                ProfileIntent.LoadProfile -> ProfileAction.LoadProfile
                ProfileIntent.Retry -> ProfileAction.LoadProfile
            }

        override fun handleAction(action: ProfileAction) {
            when (action) {
                ProfileAction.LoadProfile -> load()
            }
        }

        private fun load() {
            startObserversIfNeeded()

            if (loadedOnce && profileFlow.value != null) {
                setState(ProfileState.Data(profileFlow.value!!, favoritesCount))
                return
            }

            viewModelScope.launch {
                setState(ProfileState.Loading)

                val result =
                    withContext(dispatcher) {
                        getProfileUseCase(GetProfileRequest(DEFAULT_USER_ID))
                    }

                val profile = result.data
                if (profile != null) {
                    loadedOnce = true
                    profileFlow.value = profile
                    setState(ProfileState.Data(profile = profile, favoritesCount = favoritesCount))
                } else {
                    setState(ProfileState.Error(result.error?.toString() ?: "Unknown error"))
                }
            }
        }

        private fun startObserversIfNeeded() {
            if (observeFavoritesJob == null) {
                observeFavoritesJob =
                    viewModelScope.launch {
                        observeFavoritesCountUseCase()
                            .collectLatest { count ->
                                favoritesCount = count
                                val profile = profileFlow.value
                                if (profile != null) {
                                    setState(ProfileState.Data(profile = profile, favoritesCount = favoritesCount))
                                }
                            }
                    }
            }

            if (renderJob == null) {
                renderJob =
                    viewModelScope.launch {
                        profileFlow
                            .filterNotNull()
                            .map { profile -> ProfileState.Data(profile = profile, favoritesCount = favoritesCount) }
                            .collectLatest { setState(it) }
                    }
            }
        }

        companion object {
            private const val DEFAULT_USER_ID = 8
        }
    }
