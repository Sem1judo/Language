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


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import sem.ua.language.LanguageTopAppBar
import sem.ua.language.R
import sem.ua.language.ui.AppViewModelProvider
import sem.ua.language.ui.navigation.NavigationDestination
import sem.ua.language.ui.theme.LanguageTheme

object LevelEntryDestination : NavigationDestination {
    override val route = "level_entry"
    override val titleRes = R.string.level_entry_title
}

@Composable
fun LevelEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: LevelEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            LanguageTopAppBar(
                title = stringResource(LevelEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        LevelEntryBody(
            levelUiState = viewModel.levelUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveLevel()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun LevelEntryBody(
    levelUiState: LevelUiState,
    onItemValueChange: (LevelDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        LevelInputForm(levelDetails = levelUiState.levelDetails, onValueChange = onItemValueChange)
        Button(
            shape = MaterialTheme.shapes.small,
            onClick = onSaveClick,
            enabled = levelUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun LevelInputForm(
    levelDetails: LevelDetails,
    modifier: Modifier = Modifier,
    onValueChange: (LevelDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = levelDetails.name,
            onValueChange = { onValueChange(levelDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.level_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = levelDetails.description,
            onValueChange = { onValueChange(levelDetails.copy(description = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.level_description_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = levelDetails.duration,
            onValueChange = { onValueChange(levelDetails.copy(duration = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.duration_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LevelEntryScreenPreview() {
    LanguageTheme {
        LevelEntryBody(
            levelUiState = LevelUiState(
                LevelDetails(
                    name = "Level name",
                    description = "fghfafh tyhg gfghgff",
                    duration = "55  hour"
                )
            ),
            onItemValueChange = {},
            onSaveClick = {}
        )
    }
}

