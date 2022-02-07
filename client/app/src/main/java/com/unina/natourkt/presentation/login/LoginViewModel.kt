package com.unina.natourkt.presentation.login

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.unina.natourkt.data.remote.repository.data.AmplifyAuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val amplify: AmplifyAuthDataSource,
) : ViewModel() {


}