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

package sem.ua.language.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import sem.ua.language.R
import sem.ua.language.data.Level
import sem.ua.language.ui.AppViewModelProvider
import sem.ua.language.ui.languageTheme.LanguageNevvTheme
import sem.ua.language.ui.navigation.NavigationDestination
import sem.ua.language.ui.theme.LanguageTheme
import java.lang.Exception
import java.util.*


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun LanguageTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    if (canNavigateBack) {
        TopAppBar(
            title = { Text(title) },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        )
    } else {
        TopAppBar(title = { Text(title) }, modifier = modifier)
    }
}

@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar = {
            LanguageTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.level_entry_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.levelList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Level>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Divider()
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_level_description),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            LevelItemList(levelList = itemList, onLevelClick = { onItemClick(it.id) })
        }
    }
}

@Composable
fun LevelItem(
    level: Level, modifier: Modifier = Modifier,
    onLevelClick: (Level) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colors.primary) {
        Column(
            modifier
                .padding(8.dp)
                .fillMaxWidth()

        ) {
            Card(
                elevation = 4.dp,
                modifier = modifier
                    .clickable { onLevelClick(level) }
            )
            {
                Column(modifier.padding(8.dp)) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = level.name, style = MaterialTheme.typography.h5)
                        Spacer(Modifier.weight(1f))
                        LevelItemButton(expanded = expanded,
                            onClick = { expanded = !expanded })
                    }
                    if (expanded) {
                        Text(
                            text = level.description, modifier = Modifier
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.duration).uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.subtitle2,
                            modifier = modifier.background(MaterialTheme.colors.primary.copy(alpha = 0.1f))
                        )
                        Text(
                            text = level.duration.uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.subtitle2,
                            modifier = modifier.background(MaterialTheme.colors.primary.copy(alpha = 0.1f))
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun LevelItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            tint = MaterialTheme.colors.secondary,
            contentDescription = stringResource(R.string.expand_button_content_description)
        )
    }
}

@Composable
fun LevelItemList(
    levelList: List<Level>,
    onLevelClick: (Level) -> Unit,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(levelList, key = { it.id }) {
            LevelItem(
                level = it, onLevelClick = onLevelClick
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenLevelItemPreview() {
    LanguageNevvTheme {
        LevelItem(
            Level(
                1,
                "Beginner",
                "Otherwise known as a “super-beginner”, at A1 level English you have very limited knowledge of the language. However, you will still be able to manage everyday situations with commonly-used expressions and vocabulary",
                "100 hours"
            ),
            onLevelClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenLevelItemListPreview() {
    LanguageNevvTheme {
        LevelItemList(
            listOf(
                Level(
                    1,
                    "Beginner",
                    "Otherwise known as a “super-beginner”, at A1 level English you have very limited knowledge of the language. However, you will still be able to manage everyday situations with commonly-used expressions and vocabulary",
                    "100 hours "
                ),
                Level(
                    2,
                    "Pre-Intermediate",
                    "At A2 proficiency—or “Elementary” level—you can take part in everyday small talk and express your opinion, but still in very simple ways, and only on familiar topics. At this stage, you will start to really explore the past and future tenses",
                    " 180-200 hours "
                ),
                Level(
                    3,
                    "Intermediate",
                    "The step between A2 and B1 is a big one, and it means you’ve achieved a degree of confidence in English. This is when you can go into clothing stores and restaurants and won’t have any trouble making requests from the staff",
                    "350-400 hours"
                )
            ), onLevelClick = {})
    }
}

