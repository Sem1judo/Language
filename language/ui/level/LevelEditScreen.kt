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


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import sem.ua.language.LanguageTopAppBar
import sem.ua.language.R
import sem.ua.language.ui.AppViewModelProvider
import sem.ua.language.ui.navigation.NavigationDestination
import sem.ua.language.ui.theme.LanguageTheme

object LevelEditDestination : NavigationDestination {
    override val route = "level_edit"
    override val titleRes = R.string.level_update_title
    const val levelIdArg = "levelId"
    val routeWithArgs = "$route/{$levelIdArg}"

}

@Composable
fun LevelEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LevelEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            LanguageTopAppBar(
                title = stringResource(LevelEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        LevelEditBody(
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateLevel()
                    navigateBack()
                }
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteLevel()
                    navigateBack()
                }
            },
            levelUiState = viewModel.levelUiState,
            onItemValueChange = viewModel::updateUiState,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun LevelEditBody(
    onSaveClick: () -> Unit, onDelete: () -> Unit, levelUiState: LevelUiState,
    onItemValueChange: (LevelDetails) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LevelEntryBody(
            levelUiState = levelUiState,
            onItemValueChange = onItemValueChange,
            onSaveClick = {
                onSaveClick()
            }
        )
        DeleteLevel(
            onDelete = {
                onDelete()
            },
        )
    }
}

@Composable
private fun DeleteLevel(
    onDelete: () -> Unit, modifier: Modifier = Modifier
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
    {
        OutlinedButton(
            shape = MaterialTheme.shapes.small,
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
    }
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDelete()
            },
            onDeleteCancel = { deleteConfirmationRequired = false }
        )
    }

}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        shape = MaterialTheme.shapes.small.copy(
            ZeroCornerSize
        ),
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ItemEditRoutePreview() {
    LanguageTheme {
        LevelEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}
