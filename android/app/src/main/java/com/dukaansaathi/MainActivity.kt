package com.dukaansaathi

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dukaansaathi.ui.DukaanTheme
import com.dukaansaathi.ui.Tab
import com.dukaansaathi.ui.screens.AddDistributorScreen
import com.dukaansaathi.ui.screens.HomeScreen
import com.dukaansaathi.ui.screens.PhoneScreen
import com.dukaansaathi.ui.screens.PinScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(AndroidColor.WHITE, AndroidColor.WHITE),
        )
        setContent { DukaanTheme { AppRoot() } }
    }
}

@Composable
fun AppRoot() {
    val vm: AppViewModel = viewModel()
    val nav = rememberNavController()

    NavHost(nav, startDestination = "login") {
        composable("login") { PhoneScreen(vm) { nav.navigate("pin") } }
        composable("home") {
            HomeScreen(vm, onTab = nav::goTab, onAddDistributor = { nav.navigate("add") }, onOpenCart = { nav.navigate("cart") })
        }
        composable("add") { AddDistributorScreen(onBack = { nav.popBackStack() }) }
        composable("pin") {
            PinScreen(
                vm,
                onBack = { nav.popBackStack() },
                onSignedIn = { nav.navigate("home") { popUpTo("login") { inclusive = true } } },
            )
        }
    }
}

/** Switch between the four bottom tabs without stacking copies. */
fun NavController.goTab(tab: Tab) {
    navigate(tab.route) {
        popUpTo("home") { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
