package com.mst.controldist.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun HomeView(navController: NavController){

    Column(modifier = Modifier.padding(top = 50.dp, start = 20.dp)) {
        Text("Home")
    }

}