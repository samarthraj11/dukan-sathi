package com.dukaansaathi.core.designsystem

import com.dukaansaathi.ui.theme.Ds
import com.dukaansaathi.ui.theme.Mukta
import com.dukaansaathi.ui.theme.Yatra
import com.dukaansaathi.core.model.Distributor
import com.dukaansaathi.core.model.DistributorStatus
import com.dukaansaathi.core.model.Tone
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Bottom sheet: pick which distributor the shop orders from. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorSheet(
    distributors: List<Distributor>,
    selectedId: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
    ) {
        Column(
            Modifier
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Order from", fontFamily = Yatra, fontSize = 30.sp, modifier = Modifier.padding(bottom = 2.dp))

            distributors.forEach { d ->
                val selected = d.id == selectedId
                val pending = d.status == DistributorStatus.Pending
                val shape = RoundedCornerShape(14.dp)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(if (selected) Ds.GoldTint else Color.White)
                        .then(
                            when {
                                selected -> Modifier.border(2.5.dp, Ds.Primary, shape)
                                pending -> Modifier.drawBehind {
                                    drawRoundRect(
                                        Ds.LineStrong,
                                        cornerRadius = CornerRadius(14.dp.toPx()),
                                        style = Stroke(2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))),
                                    )
                                }
                                else -> Modifier.border(2.dp, Ds.Line, shape)
                            },
                        )
                        .clickable(enabled = !pending) {
                            onSelect(d.id)
                            onDismiss()
                        }
                        .semantics { this.selected = selected }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Avatar(d.initial, d.color, 54.dp, faded = pending)
                    Column(Modifier.weight(1f)) {
                        Text(d.name, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = if (pending) Ds.Sub else Ds.Ink)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            when (d.tone) {
                                Tone.Ok -> Box(Modifier.size(10.dp).clip(CircleShape).background(Ds.Green))
                                Tone.Wait -> Icon(Icons.Outlined.Schedule, null, Modifier.size(20.dp), tint = Ds.AmberText)
                                Tone.Plain -> {}
                            }
                            Text(
                                d.note,
                                fontSize = 18.sp,
                                fontWeight = if (d.tone == Tone.Wait) FontWeight.SemiBold else FontWeight.Medium,
                                color = when (d.tone) {
                                    Tone.Wait -> Ds.AmberText
                                    Tone.Plain -> Ds.Sub
                                    Tone.Ok -> Ds.Ink
                                },
                            )
                        }
                    }
                    if (selected) {
                        Box(Modifier.size(30.dp).clip(CircleShape).background(Ds.Primary), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Check, null, Modifier.size(18.dp), tint = Color.White)
                        }
                    } else if (!pending) {
                        Box(Modifier.size(30.dp).border(3.dp, Ds.LineStrong, CircleShape))
                    }
                }
            }

            OutlineButton(
                "Add distributor",
                onClick = onAdd,
                height = 60.dp,
                modifier = Modifier.padding(top = 4.dp),
                leading = { Icon(Icons.Rounded.Add, null, Modifier.padding(end = 10.dp).size(26.dp)) },
            )
        }
    }
}
