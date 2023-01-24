/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sem.ua.language.ui.level

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import sem.ua.language.data.Level
import sem.ua.language.data.LevelRepository

/**
 * View Model to validate and insert levels in the Room database.
 */
class LevelEntryViewModel(private val levelRepository: LevelRepository) : ViewModel() {

    /**
     * Holds current level ui state
     */
    var levelUiState by mutableStateOf(LevelUiState())
        private set

    /**
     * Updates the [levelUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(levelDetails: LevelDetails) {
        levelUiState =
            LevelUiState(levelDetails = levelDetails, isEntryValid = validateInput(levelDetails))
    }

    /**
     * Inserts an [Level] in the Room database
     */
    suspend fun saveLevel() {
        if (validateInput()) {
            levelRepository.insertLevel(levelUiState.levelDetails.toLevel())
        }
    }

    private fun validateInput(uiState: LevelDetails = levelUiState.levelDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank() && duration.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Level.
 */
data class LevelUiState(
    val levelDetails: LevelDetails = LevelDetails(),
    val isEntryValid: Boolean = false
)

data class LevelDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val duration: String = "0 Hour",
)

/**
 * Extension function to convert [LevelUiState] to [Level].
 */
fun LevelDetails.toLevel(): Level = Level(
    id = id,
    name = name,
    description = description,
    duration = duration
)

/**
 * Extension function to convert [Level] to [LevelUiState]
 */
fun Level.toLevelUiState(isEntryValid: Boolean = false): LevelUiState = LevelUiState(
    levelDetails = this.toLevelDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Level] to [LevelDetails]
 */
fun Level.toLevelDetails(): LevelDetails = LevelDetails(
    id = id,
    name = name,
    description = description,
    duration = duration
)

