package com.dukaansaathi.feature.account

import com.dukaansaathi.core.designsystem.*
import com.dukaansaathi.core.model.*
import com.dukaansaathi.domain.pricing.rupees
import com.dukaansaathi.ui.theme.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
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

/** Placeholder until ledger, payments and returns are designed. */
@Composable
fun AccountScreen(vm: AccountViewModel, onTab: (Tab) -> Unit, onLogout: () -> Unit) {
    val state by vm.collectAsState()
    vm.collectSideEffect { if (it is AccountSideEffect.LoggedOut) onLogout() }
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
                Text("+91 ${state.phone.take(5)} ${state.phone.drop(5)}", fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(Modifier.weight(1f).padding(horizontal = 24.dp, vertical = 22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("My distributors", fontFamily = Yatra, fontSize = 26.sp)
            Column {
                state.distributors.forEach { d ->
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
            OutlineButton("Log out", vm::logOut)
        }

        BottomNav(Tab.Account, onTab)
    }
}
