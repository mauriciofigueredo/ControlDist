package com.mst.controldist.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CamViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    fun signOut(){
        auth.signOut()
    }
}