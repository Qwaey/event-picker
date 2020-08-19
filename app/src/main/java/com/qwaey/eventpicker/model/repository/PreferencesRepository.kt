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

package com.qwaey.eventpicker.model.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseUser
import com.qwaey.eventpicker.ProjectApplication
import com.qwaey.eventpicker.utils.KeyStore

class PreferencesRepository {

    val preferences: SharedPreferences by lazy {
        ProjectApplication.appInstance.applicationContext.getSharedPreferences(
            KeyStore.SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )
    }

    fun saveUser(user: FirebaseUser) {
        preferences.edit()
            .putString(DISPLAY_NAME_KEY, user.displayName)
            .putString(EMAIL_KEY, user.email)
            .putString(PHOTO_URL_KEY, user.photoUrl.toString())
            .putString(UID_KEY, user.uid)
            .apply()
    }

    fun clearPreferences() {
        preferences.edit()
            .clear()
            .apply()
    }

    companion object {
        const val DISPLAY_NAME_KEY = "DISPLAY_NAME_KEY"
        const val EMAIL_KEY = "EMAIL_KEY"
        const val PHOTO_URL_KEY = "PHOTO_URL_KEY"
        const val UID_KEY = "UID_KEY"
    }
}