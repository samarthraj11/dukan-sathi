package com.dukaansaathi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dukaansaathi.AppViewModel
import com.dukaansaathi.DistributorStatus
import com.dukaansaathi.distributors
import com.dukaansaathi.ui.Avatar
import com.dukaansaathi.ui.BottomNav
import com.dukaansaathi.ui.Ds
import com.dukaansaathi.ui.OutlineButton
import com.dukaansaathi.ui.Tab
import com.dukaansaathi.ui.Yatra

/** Placeholder until ledger, payments and returns are designed. */
@Composable
fun AccountScreen(vm: AppViewModel, onTab: (Tab) -> Unit, onLogout: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Ds.Gold)
                .statusBarsPadding()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(Modifier.size(60.dp).clip(CircleShape).background(Ds.Ink), contentAlignment = Alignment.Center) {
                Text("G", color = Ds.Gold, fontFamily = Yatra, fontSize = 30.sp)
            }
            Column {
                Text("Gupta General Store", fontFamily = Yatra, fontSize = 26.sp, lineHeight = 30.sp)
                Text("+91 ${vm.phone.take(5)} ${vm.phone.drop(5)}", fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(Modifier.weight(1f).padding(horizontal = 24.dp, vertical = 22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("My distributors", fontFamily = Yatra, fontSize = 26.sp)
            Column {
                distributors.filter { it.status == DistributorStatus.Active }.forEach { d ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Avatar(d.initial, d.color, 44.dp)
                        Text(d.name, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    }
                    HorizontalDivider(color = Ds.Line)
                }
            }
            Text("Ledger, payments and returns are coming soon.", fontSize = 19.sp, color = Ds.Sub)
            Spacer(Modifier.weight(1f))
            OutlineButton("Log out", onLogout)
        }

        BottomNav(Tab.Account, onTab)
    }
}
