package com.dukaansaathi

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dukaansaathi.ui.DukaanTheme
import com.dukaansaathi.ui.Tab
import com.dukaansaathi.ui.screens.AccountScreen
import com.dukaansaathi.ui.screens.AccountScreen
import com.dukaansaathi.ui.screens.AddDistributorScreen
import com.dukaansaathi.ui.screens.CartScreen
import com.dukaansaathi.ui.screens.CatalogueScreen
import com.dukaansaathi.ui.screens.HomeScreen
import com.dukaansaathi.ui.screens.OrderPlacedScreen
import com.dukaansaathi.ui.screens.OrdersScreen
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

    val tabs = listOf("home", "catalogue", "orders", "account")
    fun AnimatedContentTransitionScope<NavBackStackEntry>.dir(): Int? {
        val from = tabs.indexOf(initialState.destination.route)
        val to = tabs.indexOf(targetState.destination.route)
        return if (from >= 0 && to >= 0 && from != to) (if (to > from) 1 else -1) else null
    }
    val slide = tween<IntOffset>(300)
    NavHost(
        nav,
        startDestination = "login",
        enterTransition = {
            when (dir()) {
                1 -> slideInHorizontally(slide) { it }
                -1 -> slideInHorizontally(slide) { -it }
                else -> fadeIn(tween(250))
            }
        },
        exitTransition = {
            when (dir()) {
                1 -> slideOutHorizontally(slide) { -it }
                -1 -> slideOutHorizontally(slide) { it }
                else -> fadeOut(tween(250))
            }
        },
        popEnterTransition = {
            when (dir()) {
                1 -> slideInHorizontally(slide) { it }
                -1 -> slideInHorizontally(slide) { -it }
                else -> fadeIn(tween(250))
            }
        },
        popExitTransition = {
            when (dir()) {
                1 -> slideOutHorizontally(slide) { -it }
                -1 -> slideOutHorizontally(slide) { it }
                else -> fadeOut(tween(250))
            }
        },
    ) {
        composable("login") { PhoneScreen(vm) { nav.navigate("pin") } }
        composable("home") {
            HomeScreen(vm, onTab = nav::goTab, onAddDistributor = { nav.navigate("add") }, onOpenCart = { nav.navigate("cart") })
        }
        composable("catalogue") {
            CatalogueScreen(vm, onTab = nav::goTab, onAddDistributor = { nav.navigate("add") }, onOpenCart = { nav.navigate("cart") })
        }
        composable("cart") {
            CartScreen(
                vm,
                onBack = { nav.popBackStack() },
                onOpenCatalogue = { nav.goTab(Tab.Catalogue) },
                onPlaced = { id -> nav.navigate("placed/$id") { popUpTo("cart") { inclusive = true } } },
            )
        }
        composable("placed/{orderId}") { entry ->
            OrderPlacedScreen(
                vm,
                orderId = entry.arguments?.getString("orderId").orEmpty(),
                onDone = { nav.navigate("home") { popUpTo("home") { inclusive = true } } },
                onEdit = { nav.goTab(Tab.Orders) },
            )
        }
        composable("orders") { OrdersScreen(vm, onTab = nav::goTab) }
        composable("account") {
            AccountScreen(
                vm,
                onTab = nav::goTab,
                onLogout = {
                    vm.signOut()
                    nav.navigate("login") { popUpTo(0) }
                },
            )
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
