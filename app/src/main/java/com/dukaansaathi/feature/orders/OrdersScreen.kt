package com.dukaansaathi.feature.orders

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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

/** Orders from every distributor in one list, each tagged with who and how much. */
@Composable
fun OrdersScreen(vm: OrdersViewModel, onTab: (Tab) -> Unit) {
    val state by vm.collectAsState()
    LaunchedEffect(Unit) { vm.refresh() }
    val filter = state.filter
    val active = state.active
    val monthTotal = state.monthTotal
    val perDistributor = state.perDistributor
    val shown = state.shown

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Ds.Gold)
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("Orders", fontFamily = Yatra, fontSize = 36.sp)
            Text("This month", Modifier.padding(top = 8.dp), fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
            Text(rupees(monthTotal), fontFamily = Yatra, fontSize = 40.sp, lineHeight = 44.sp)
            Row(
                Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .semantics { contentDescription = perDistributor.joinToString { "${it.first.name} ${rupees(it.second)}" } },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                perDistributor.forEach { (d, total) ->
                    Box(Modifier.weight(total.toFloat()).height(14.dp).clip(RoundedCornerShape(7.dp)).background(d.color))
                }
            }
            Row(Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                perDistributor.forEach { (d, total) ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.size(12.dp).clip(CircleShape).background(d.color))
                        Text("${d.name.substringBefore(' ')} ${rupees(total)}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Row(Modifier.padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 6.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            val allShape = RoundedCornerShape(23.dp)
            Box(
                Modifier
                    .heightIn(min = 46.dp)
                    .clip(allShape)
                    .background(if (filter == "all") Ds.Primary else Color.White)
                    .then(if (filter == "all") Modifier else Modifier.border(2.dp, Ds.LineStrong, allShape))
                    .clickable { vm.setFilter(ALL_DISTRIBUTORS) }
                    .padding(horizontal = 22.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("All", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = if (filter == "all") Color.White else Ds.Ink)
            }
            active.forEach { d ->
                val selected = filter == d.id
                Row(
                    Modifier
                        .heightIn(min = 46.dp)
                        .clip(allShape)
                        .border(2.dp, if (selected) Ds.Primary else Ds.LineStrong, allShape)
                        .clickable { vm.setFilter(d.id) }
                        .padding(start = 6.dp, end = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Avatar(d.initial, d.color, 32.dp)
                    Text(d.name.substringBefore(' '), fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 24.dp)) {
            items(shown, key = { it.id }) { o ->
                val d = state.distributors.first { it.id == o.distributorId }
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Avatar(d.initial, d.color, 50.dp)
                    Column(Modifier.weight(1f)) {
                        Text(d.name, fontSize = 21.sp, fontWeight = FontWeight.Bold, lineHeight = 25.sp)
                        Text("${o.number}, ${if (o.date == "Today") "today" else o.date}", fontSize = 17.sp, color = Ds.Sub)
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(rupees(o.amount), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        val (fill, ink) = when (o.status) {
                            OrderStatus.Placed -> Ds.GoldTint to Ds.Ink
                            OrderStatus.OnTheWay -> Ds.GreenTint to Ds.GreenText
                            OrderStatus.Delivered -> Ds.Soft to Ds.Sub
                        }
                        Text(
                            o.status.label,
                            Modifier.clip(RoundedCornerShape(6.dp)).background(fill).padding(horizontal = 10.dp),
                            color = ink,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                HorizontalDivider(color = Ds.Line)
            }
        }

        BottomNav(Tab.Orders, onTab)
    }
}
