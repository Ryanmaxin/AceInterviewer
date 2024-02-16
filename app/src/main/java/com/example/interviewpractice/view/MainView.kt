package com.example.interviewpractice.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.interviewpractice.Greeting
import com.example.interviewpractice.GreetingPreview
import com.example.interviewpractice.R
import com.example.interviewpractice.controller.UserController
import com.example.interviewpractice.model.Model
import com.example.interviewpractice.view.HomeScreen
import com.example.interviewpractice.viewmodel.auth.LoginViewModel
import com.example.interviewpractice.viewmodel.MainViewModel
import com.example.interviewpractice.viewmodel.auth.RegisterViewModel
import com.example.interviewpractice.view.auth.Loading
import com.example.interviewpractice.view.auth.LoginScreen
import com.example.interviewpractice.view.auth.RegisterScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
//@Preview
fun MainView(registerViewModel: RegisterViewModel, loginViewModel: LoginViewModel, mainViewModel: MainViewModel, controller: UserController) {
    val registerVM by remember { mutableStateOf(registerViewModel) }
    val loginVM by remember { mutableStateOf(loginViewModel) }
    val mainVM by remember { mutableStateOf(mainViewModel) }

    var loggingIn by remember {mutableStateOf(false)}

    // NavController //////////////////////////////////////////////////////////

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(viewModel = loginVM,
            controller = controller, onNavigateToHome = { navController.navigate("home")},
            onNavigateToRegister = { navController.navigate("register")},
            onSwitch = { loggingIn = !loggingIn }) }

        composable("home") { HomeScreen(controller = controller) }

        composable("register") { RegisterScreen(viewModel = registerVM, controller = controller) {
        }}
    }

    //////////////////////////////////////////////////////////////////////////


//    if (mainVM.loading) {
//        //Auth is loading
//        Loading()
//    }
//    else if (mainVM.user != null) {
//        //If user is signed in
//        HomeScreen(controller = controller)
//
//        //REMOVE GREETING, THIS IS JUST TO SHOW USER EMAIL (FOR DEBUGGING PURPOSES)
//        Greeting(mainVM.user!!.email!!)
//    }
//    else {
//        //No user is logged in
//        if (loggingIn) {
//            LoginScreen(viewModel = loginVM, controller = controller) {loggingIn = !loggingIn}
//        }
//        else {
//            RegisterScreen(viewModel = registerVM, controller = controller) {loggingIn = !loggingIn}
//        }
//
//
//    }
}
