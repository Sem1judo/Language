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

package sem.ua.language.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import sem.ua.language.LanguageApplication
import sem.ua.language.ui.home.HomeViewModel
import sem.ua.language.ui.level.LevelEditViewModel
import sem.ua.language.ui.level.LevelEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Language app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for LevelEditViewModel
        initializer {
            LevelEditViewModel(
                this.createSavedStateHandle(),
                languageApplication().container.levelRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            LevelEntryViewModel(languageApplication().container.levelRepository)
        }
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(languageApplication().container.levelRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [LanguageApplication].
 */
fun CreationExtras.languageApplication(): LanguageApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LanguageApplication)