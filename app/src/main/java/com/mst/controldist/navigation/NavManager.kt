package com.mst.controldist.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mst.controldist.viewModels.LoginViewModel
import com.mst.controldist.views.HomeView
import com.mst.controldist.views.TabsView


@Composable
fun NavManager(loginVM: LoginViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Tabs" ){

        composable("Tabs"){
            TabsView(navController, loginVM)
        }
        composable("Home"){
            HomeView(navController)
        }

    }
}