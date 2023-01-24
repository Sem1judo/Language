package sem.ua.language.ui.level

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import sem.ua.language.data.LevelRepository

class LevelEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val levelRepository: LevelRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var levelUiState by mutableStateOf(LevelUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[LevelEditDestination.levelIdArg])

    init {
        viewModelScope.launch {
            levelUiState = levelRepository.getLevelStream(itemId)
                .filterNotNull()
                .first()
                .toLevelUiState(true)
        }
    }

    /**
     * Deletes the item from the [ItemsRepository]'s data source.
     */
    suspend fun deleteLevel() {
        levelRepository.deleteLevel(levelUiState.levelDetails.toLevel())
    }

    /**
     * Update the level in the [LevelRepository]'s data source
     */
    suspend fun updateLevel() {
        if (validateInput(levelUiState.levelDetails)) {
            levelRepository.updateLevel(levelUiState.levelDetails.toLevel())
        }
    }

    /**
     * Updates the [levelUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(levelDetails: LevelDetails) {
        levelUiState =
            LevelUiState(levelDetails = levelDetails, isEntryValid = validateInput(levelDetails))
    }

    private fun validateInput(uiState: LevelDetails = levelUiState.levelDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank() && duration.isNotBlank()
        }
    }
}
