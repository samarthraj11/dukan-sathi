package com.dukaansaathi.feature.adddistributor.presentation

import com.dukaansaathi.core.designsystem.*
import com.dukaansaathi.core.model.*
import com.dukaansaathi.ui.theme.*
import androidx.compose.runtime.getValue
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QrCodeScanner
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddDistributorScreen(vm: AddDistributorViewModel, onBack: () -> Unit) {
    val state by vm.collectAsState()
    val number = state.number
    val sent = state.sent

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 28.dp),
    ) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(Modifier.size(52.dp).clickable(onClick = onBack), contentAlignment = Alignment.Center) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.size(32.dp))
            }
            Text("Add distributor", fontFamily = Yatra, fontSize = 36.sp)
        }

        Text("Distributor's number", Modifier.padding(top = 28.dp, bottom = 10.dp), fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        PhoneField(number, vm::setNumber, "98100 12345") { vm.send() }

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            if (sent) "Request sent" else "Send request",
            onClick = vm::send,
            enabled = number.length == 10 && !sent,
            height = 68.dp,
        )

        Row(
            Modifier.fillMaxWidth().padding(vertical = 26.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(Modifier.weight(1f).height(2.dp).background(Ds.Line))
            Text("or", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Ds.Sub)
            Box(Modifier.weight(1f).height(2.dp).background(Ds.Line))
        }

        OutlineButton(
            "Scan their QR code",
            onClick = { },
            height = 64.dp,
            leading = { Icon(Icons.Outlined.QrCodeScanner, null, Modifier.padding(end = 12.dp).size(30.dp)) },
        )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Ds.GoldTint)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(Icons.Outlined.Info, null, Modifier.size(28.dp))
            Text(
                if (sent) "Request sent. You can order once they approve." else "They approve you first. Then you can order.",
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 26.sp,
            )
        }
    }
}
