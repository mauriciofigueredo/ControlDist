package com.mst.controldist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mst.controldist.navigation.NavManager
import com.mst.controldist.ui.theme.ControlDistTheme
import com.mst.controldist.viewModels.CamViewModel
import com.mst.controldist.viewModels.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val loginVM : LoginViewModel by viewModels()
        val camVM : CamViewModel by viewModels()
        setContent {
            ControlDistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavManager(loginVM, camVM)
                }
            }
        }
    }
}

//TODO
//Mantener sesion iniciada
//crear tabla de caminantes
//obtener y mostrar listado de caminantes
//permitir la selecion del caminante para la toma de la foto

