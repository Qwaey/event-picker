/*
 * Copyright 2020 Andrey Kitaev. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qwaey.eventpicker.di

import com.qwaey.eventpicker.model.repository.PreferencesRepository
import com.qwaey.eventpicker.ui.holdStart.HoldStartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module(override = true) {
    single { PreferencesRepository() }
}

val viewModelModule = module {
    viewModel { HoldStartViewModel(get()) }
}