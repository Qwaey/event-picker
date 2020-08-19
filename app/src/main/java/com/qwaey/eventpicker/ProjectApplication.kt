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

package com.qwaey.eventpicker

import android.app.Application
import com.qwaey.eventpicker.di.repositoryModule
import com.qwaey.eventpicker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        startKoin {
            androidContext(this@ProjectApplication)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }

    companion object {

        lateinit var appInstance: ProjectApplication
            private set
    }
}