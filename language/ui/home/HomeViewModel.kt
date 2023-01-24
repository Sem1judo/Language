package sem.ua.language.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sem.ua.language.data.Level
import sem.ua.language.data.LevelRepository

/**
 * View Model to retrieve all items in the Room database.
 */
class HomeViewModel(levelRepository: LevelRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of Levels are retrieved from [LevelRepository] and mapped to
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        levelRepository.getAllLevelsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val levelList: List<Level> = listOf())
