package com.mst.controldist.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private lateinit var auth: FirebaseAuth

// ...
// Initialize Firebase Auth
    auth = Firebase.auth

//    private val auth: FirebaseAuth = Firebase.auth
//    var showAlert by mutableStateOf(false)
//
//    fun login(email: String, password: String, onSuccess: () -> Unit){
//        viewModelScope.launch {
//            try {
//                auth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful){
//                            onSuccess()
//                        }else{
//                            Log.d("ERROR EN FIREBASE","Usuario y contrasena incorrectos")
//                            showAlert = true
//                        }
//                    }
//            }catch (e: Exception){
//                Log.d("ERROR EN JETPACK", "ERROR: ${e.localizedMessage}")
//            }
//        }
    }