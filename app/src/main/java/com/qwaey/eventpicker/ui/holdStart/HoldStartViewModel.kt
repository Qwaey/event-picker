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

package com.qwaey.eventpicker.ui.holdStart

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.qwaey.eventpicker.model.repository.PreferencesRepository
import timber.log.Timber

class HoldStartViewModel(
    private val prefRepository: PreferencesRepository
) : ViewModel() {

    val userLiveData = MutableLiveData<FirebaseUser>()
    val errorMessageLiveData = MutableLiveData<String>()

    private val auth: FirebaseAuth by lazy(LazyThreadSafetyMode.NONE) {
        Firebase.auth
    }

    fun handleHoldStart() {
        userLiveData.postValue(auth.currentUser)
    }

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            Timber.d("firebaseAuthWithGoogle: ${account?.id}")
            account?.idToken?.let {
                firebaseAuthWithGoogle(it)
            }
        } catch (e: ApiException) {
            Timber.w(e)
            errorMessageLiveData.postValue("Google sign in failed")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.w("signInWithCredential: success")
                    auth.currentUser?.let { user ->
                        prefRepository.saveUser(user)
                        userLiveData.postValue(user)
                    }
                } else {
                    Timber.w(task.exception, "signInWithCredential: failure")
                    errorMessageLiveData.postValue("Authentication Failed.")
                }
            }
    }
}