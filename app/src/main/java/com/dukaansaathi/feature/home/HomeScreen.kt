package com.dukaansaathi.feature.home

import com.dukaansaathi.core.designsystem.*
import com.dukaansaathi.core.model.*
import com.dukaansaathi.domain.pricing.rupees
import com.dukaansaathi.ui.theme.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(vm: HomeViewModel, onTab: (Tab) -> Unit, onAddDistributor: () -> Unit, onOpenCart: () -> Unit) {
    val state by vm.collectAsState()
    LaunchedEffect(Unit) { vm.refresh() }
    vm.collectSideEffect { if (it is HomeSideEffect.OpenCart) onOpenCart() }
    var sheetOpen by remember { mutableStateOf(false) }
    val distributor = state.distributor
    val active = state.activeOrder
    val last = state.lastOrder
    val usual = state.usual

    Column(Modifier.fillMaxSize()) {
        // Header band: who I am, who I order from, and where my order is.
        Column(
            Modifier
                .fillMaxWidth()
                .background(Ds.Gold)
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Ds.Ink)
                        .semantics { contentDescription = "Gupta General Store" },
                    contentAlignment = Alignment.Center,
                ) { Text("G", color = Ds.Gold, fontFamily = Yatra, fontSize = 26.sp) }
                DistributorChip(distributor, onClick = { sheetOpen = true })
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (active != null) {
                    Text("Order ${active.number}", fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                    Text(active.status.label, fontFamily = Yatra, fontSize = 38.sp, lineHeight = 42.sp)
                    Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        repeat(5) { n ->
                            Box(
                                Modifier
                                    .weight(1f)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(if (n + 1 <= active.status.step) Ds.Ink else Color.White),
                            )
                        }
                    }
                } else {
                    Text(distributor.name, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                    Text("All delivered", fontFamily = Yatra, fontSize = 38.sp, lineHeight = 42.sp)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (active == null) "Ready to order" else if (active.status == OrderStatus.Placed) "Waiting to confirm" else "Arriving by 4 PM",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Box(
                        Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Ds.Ink)
                            .clickable { }
                            .semantics { contentDescription = "Call ${distributor.name}" },
                        contentAlignment = Alignment.Center,
                    ) { Icon(Icons.Rounded.Phone, null, Modifier.size(24.dp), tint = Color.White) }
                }
            }
        }

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            val againEnabled = last.pcs > 0
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 76.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (againEnabled) Ds.Primary else Ds.Soft)
                    .clickable(enabled = againEnabled, onClick = vm::orderAgain)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Order again", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text("${last.pcs} pcs, ${rupees(last.total)}", color = Color.White.copy(alpha = 0.9f), fontSize = 18.sp)
                }
                Icon(Icons.Rounded.Repeat, null, Modifier.size(34.dp), tint = Color.White)
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Monday usual", fontFamily = Yatra, fontSize = 26.sp)
                    OutlineButton(
                        "Add all",
                        onClick = vm::addAllUsual,
                        modifier = Modifier.width(104.dp),
                        height = 46.dp,
                        fontSize = 19.sp,
                    )
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Always three slots, so one or two items keep the same tile width.
                    repeat(3) { slot ->
                        val item = usual.getOrNull(slot)
                        if (item == null) {
                            Spacer(Modifier.weight(1f))
                        } else {
                            val (product, qty) = item
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box {
                                    ProductArt(product.art, Modifier.fillMaxWidth())
                                    Box(
                                        Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 6.dp, y = (-8).dp)
                                            .heightIn(min = 34.dp)
                                            .widthMin34()
                                            .border(3.dp, Color.White, RoundedCornerShape(17.dp))
                                            .clip(RoundedCornerShape(17.dp))
                                            .background(Ds.Primary),
                                        contentAlignment = Alignment.Center,
                                    ) { Text("${qty.pcs}", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold) }
                                }
                                Text(product.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
                            }
                        }
                    }
                }
            }
        }

        BottomNav(Tab.Home, onTab)
    }

    if (sheetOpen) {
        DistributorSheet(state.distributors, distributor.id, onSelect = vm::selectDistributor, onDismiss = { sheetOpen = false }, onAdd = {
            sheetOpen = false
            onAddDistributor()
        })
    }
}

private fun Modifier.widthMin34(): Modifier = this.then(Modifier.defaultMinSize(minWidth = 34.dp))
