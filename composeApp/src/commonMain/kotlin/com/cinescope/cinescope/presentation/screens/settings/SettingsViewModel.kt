package com.cinescope.cinescope.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.model.UserProfile
import com.cinescope.cinescope.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val userProfile: UserProfile = UserProfile(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SettingsViewModel(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                userProfileRepository.getUserProfile().collect { profile ->
                    _uiState.update {
                        it.copy(
                            userProfile = profile,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        userProfile = UserProfile(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            try {
                userProfileRepository.updateUserName(name)
            } catch (e: Exception) {
                // Ignore errors for now
            }
        }
    }

    fun updateProfilePicture(path: String) {
        viewModelScope.launch {
            try {
                userProfileRepository.updateUserProfilePicture(path)
            } catch (e: Exception) {
                // Ignore errors for now
            }
        }
    }
}
