package com.dukaansaathi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dukaansaathi.AppViewModel
import com.dukaansaathi.distributors
import com.dukaansaathi.rupees
import com.dukaansaathi.ui.Avatar
import com.dukaansaathi.ui.Ds
import com.dukaansaathi.ui.OutlineButton
import com.dukaansaathi.ui.PrimaryButton
import com.dukaansaathi.ui.Yatra
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class StepState { Done, Now, Todo }

private data class Step(val name: String, val note: String, val state: StepState)

@Composable
fun OrderPlacedScreen(vm: AppViewModel, orderId: String, onDone: () -> Unit, onEdit: () -> Unit) {
    val order = vm.orders.firstOrNull { it.id == orderId } ?: return
    val distributor = distributors.first { it.id == order.distributorId }
    val placedAt = remember { SimpleDateFormat("h:mm a", Locale.ENGLISH).format(Date()).uppercase() }
    val steps = listOf(
        Step("Placed", placedAt, StepState.Done),
        Step("Confirmed", "Waiting", StepState.Now),
        Step("Packed", "", StepState.Todo),
        Step("On the way", "", StepState.Todo),
        Step("Delivered", "Tue 25 Nov", StepState.Todo),
    )

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Ds.Green)
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 44.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Check, null, Modifier.size(42.dp), tint = Ds.Green)
            }
            Text("Order placed", color = Color.White, fontFamily = Yatra, fontSize = 40.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Avatar(distributor.initial, distributor.color, 34.dp)
                    Text(order.number, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Text(rupees(order.amount), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }

        Column(Modifier.weight(1f).padding(start = 24.dp, end = 24.dp, top = 22.dp)) {
            steps.forEachIndexed { i, step ->
                val last = i == steps.lastIndex
                Row(
                    Modifier.height(IntrinsicSize.Min).heightIn(min = if (last) 0.dp else 60.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(Modifier.width(30.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        StepNode(step.state)
                        if (!last) {
                            Box(
                                Modifier
                                    .weight(1f)
                                    .width(4.dp)
                                    .background(if (step.state == StepState.Done) Ds.Green else Ds.Line),
                            )
                        }
                    }
                    Column(Modifier.padding(bottom = 10.dp)) {
                        Text(
                            step.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (step.state == StepState.Todo) Ds.Sub else Ds.Ink,
                            lineHeight = 26.sp,
                        )
                        if (step.note.isNotEmpty()) {
                            Text(
                                step.note,
                                fontSize = 18.sp,
                                fontWeight = if (step.state == StepState.Now) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (step.state == StepState.Now) Ds.AmberText else Ds.Sub,
                            )
                        }
                    }
                }
            }
        }

        Column(
            Modifier.navigationBarsPadding().padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PrimaryButton("Done", onDone)
            OutlineButton("Edit order", onEdit)
        }
    }
}

@Composable
private fun StepNode(state: StepState) {
    when (state) {
        StepState.Done -> Box(Modifier.size(30.dp).clip(CircleShape).background(Ds.Green), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Check, null, Modifier.size(18.dp), tint = Color.White)
        }
        StepState.Now -> Box(
            Modifier
                .size(30.dp)
                .border(2.dp, Ds.Gold, CircleShape)
                .padding(2.dp)
                .clip(CircleShape)
                .background(Ds.GoldTint)
                .padding(4.dp)
                .clip(CircleShape)
                .background(Ds.Gold),
        )
        StepState.Todo -> Box(Modifier.size(30.dp).border(4.dp, Ds.LineStrong, CircleShape).background(Color.White, CircleShape))
    }
}
