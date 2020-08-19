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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.qwaey.eventpicker.R
import com.qwaey.eventpicker.databinding.FragmentSignInBinding
import com.qwaey.eventpicker.utils.getViewModel
import com.qwaey.eventpicker.utils.observeNullable

class HoldStartFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: HoldStartViewModel
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        initUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(it, gso)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.handleHoldStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.handleSignInResult(data)
        }
    }

    private fun initUI() {
        with(viewModel) {
            userLiveData.observeNullable(this@HoldStartFragment) {
                findNavController().navigate(R.id.action_hold_start_fragment_to_main_fragment)
            }

            errorMessageLiveData.observeNullable(this@HoldStartFragment) { text ->
                view?.let {
                    Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.signInButton.setOnClickListener {
            googleSignInClient?.let {
                val signInIntent = it.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}