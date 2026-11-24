package com.dukaansaathi.app.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.dukaansaathi.app.AppContainer
import com.dukaansaathi.core.designsystem.Tab
import com.dukaansaathi.feature.account.presentation.AccountScreen
import com.dukaansaathi.feature.account.presentation.AccountViewModel
import com.dukaansaathi.feature.adddistributor.presentation.AddDistributorScreen
import com.dukaansaathi.feature.adddistributor.presentation.AddDistributorViewModel
import com.dukaansaathi.feature.cart.presentation.CartScreen
import com.dukaansaathi.feature.cart.presentation.CartViewModel
import com.dukaansaathi.feature.catalogue.presentation.CatalogueScreen
import com.dukaansaathi.feature.catalogue.presentation.CatalogueViewModel
import com.dukaansaathi.feature.home.presentation.HomeScreen
import com.dukaansaathi.feature.home.presentation.HomeViewModel
import com.dukaansaathi.feature.login.presentation.PhoneScreen
import com.dukaansaathi.feature.login.presentation.PhoneViewModel
import com.dukaansaathi.feature.login.presentation.PinScreen
import com.dukaansaathi.feature.login.presentation.PinViewModel
import com.dukaansaathi.feature.orders.presentation.OrdersScreen
import com.dukaansaathi.feature.orders.presentation.OrdersViewModel
import com.dukaansaathi.feature.placed.presentation.OrderPlacedScreen
import com.dukaansaathi.feature.placed.presentation.OrderPlacedViewModel

private const val NAV_ANIMATION_MS = 300

private fun fade(): ContentTransform = fadeIn(tween(250)) togetherWith fadeOut(tween(250))

/** Full-width slide between tabs: a tab to the right enters from the right, one to the left from the left. */
private fun tabSlide(direction: Int): ContentTransform =
    slideInHorizontally(tween(NAV_ANIMATION_MS)) { it * direction } togetherWith slideOutHorizontally(tween(NAV_ANIMATION_MS)) { -it * direction }

private data object LoginRoute
private data class PinRoute(val phone: String)
private data object HomeRoute
private data object CatalogueRoute
private data object OrdersRoute
private data object AccountRoute
private data object CartRoute
private data class PlacedRoute(val orderId: String)
private data object AddDistributorRoute

private fun Tab.route(): Any = when (this) {
    Tab.Home -> HomeRoute
    Tab.Catalogue -> CatalogueRoute
    Tab.Orders -> OrdersRoute
    Tab.Account -> AccountRoute
}

// Position of a route in the bottom tab order, or null for screens that are not tabs.
private fun tabIndexOf(route: Any): Int? = when (route) {
    HomeRoute -> Tab.Home.ordinal
    CatalogueRoute -> Tab.Catalogue.ordinal
    OrdersRoute -> Tab.Orders.ordinal
    AccountRoute -> Tab.Account.ordinal
    else -> null
}

@Composable
fun DukaanApp() {
    val repository = AppContainer.repository
    val backStack = remember { mutableStateListOf<Any>(if (AppContainer.isSignedIn()) HomeRoute else LoginRoute) }
    val activity = LocalContext.current.findActivity()
    // +1 / -1 while switching to a tab on the right / left of the current one, else 0.
    val tabDirection = remember { IntArray(1) }

    fun back() { tabDirection[0] = 0; if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
    fun open(route: Any) { tabDirection[0] = 0; backStack.add(route) }
    fun reset(route: Any) { tabDirection[0] = 0; backStack.clear(); backStack.add(route) }
    fun openTab(tab: Tab) {
        val current = backStack.lastOrNull()?.let(::tabIndexOf)
        tabDirection[0] = when {
            current == null || current == tab.ordinal -> 0
            tab.ordinal > current -> 1
            else -> -1
        }
        backStack.clear(); backStack.add(tab.route())
    }

    // Tabs sit alone on the stack: back goes to Home, and back on Home leaves the app.
    BackHandler {
        val top = backStack.last()
        when {
            backStack.size > 1 -> back()
            top == HomeRoute || top == LoginRoute -> activity?.finish()
            else -> openTab(Tab.Home)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = ::back,
        // Switching bottom tabs slides toward the side the tab sits on; everything else fades.
        transitionSpec = { if (tabDirection[0] != 0) tabSlide(tabDirection[0]) else fade() },
        popTransitionSpec = { if (tabDirection[0] != 0) tabSlide(tabDirection[0]) else fade() },
        predictivePopTransitionSpec = { if (tabDirection[0] != 0) tabSlide(tabDirection[0]) else fade() },
        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator(), rememberViewModelStoreNavEntryDecorator()),
        entryProvider = entryProvider {
            entry<LoginRoute> {
                val vm = viewModel { PhoneViewModel() }
                PhoneScreen(vm) { phone -> open(PinRoute(phone)) }
            }
            entry<PinRoute> { route ->
                val vm = viewModel(key = "pin-${route.phone}") { PinViewModel(route.phone, repository) }
                PinScreen(vm, onBack = ::back, onSignedIn = { reset(HomeRoute) })
            }
            entry<HomeRoute> {
                val vm = viewModel { HomeViewModel(repository) }
                HomeScreen(vm, onTab = ::openTab, onAddDistributor = { open(AddDistributorRoute) }, onOpenCart = { open(CartRoute) })
            }
            entry<CatalogueRoute> {
                val vm = viewModel { CatalogueViewModel(repository) }
                CatalogueScreen(vm, onTab = ::openTab, onAddDistributor = { open(AddDistributorRoute) }, onOpenCart = { open(CartRoute) })
            }
            entry<OrdersRoute> {
                val vm = viewModel { OrdersViewModel(repository) }
                OrdersScreen(vm, onTab = ::openTab)
            }
            entry<AccountRoute> {
                val vm = viewModel { AccountViewModel(repository) }
                AccountScreen(vm, onTab = ::openTab, onLogout = { reset(LoginRoute) })
            }
            entry<CartRoute> {
                val vm = viewModel { CartViewModel(repository) }
                CartScreen(
                    vm,
                    onBack = ::back,
                    onOpenCatalogue = { openTab(Tab.Catalogue) },
                    onPlaced = { id -> backStack.removeAt(backStack.lastIndex); open(PlacedRoute(id)) },
                )
            }
            entry<PlacedRoute> { route ->
                val vm = viewModel(key = "placed-${route.orderId}") { OrderPlacedViewModel(route.orderId, repository) }
                OrderPlacedScreen(vm, onDone = { reset(HomeRoute) }, onEdit = { openTab(Tab.Orders) })
            }
            entry<AddDistributorRoute> {
                val vm = viewModel { AddDistributorViewModel() }
                AddDistributorScreen(vm, onBack = ::back)
            }
        },
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
