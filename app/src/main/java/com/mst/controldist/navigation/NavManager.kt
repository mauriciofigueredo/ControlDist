package com.mst.controldist.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mst.controldist.viewModels.CamViewModel
import com.mst.controldist.viewModels.LoginViewModel
import com.mst.controldist.views.Blank
import com.mst.controldist.views.HomeView
import com.mst.controldist.views.TabsView


@Composable
fun NavManager(loginVM: LoginViewModel, camVM: CamViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Blank" ){

        composable("Blank"){
            Blank(navController)
        }
        composable("Tabs"){
            TabsView(navController, loginVM)
        }
        composable("Home"){
            HomeView(navController,camVM )
        }

    }
}