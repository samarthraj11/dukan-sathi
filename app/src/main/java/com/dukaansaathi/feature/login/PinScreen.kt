package com.dukaansaathi.feature.login

import com.dukaansaathi.core.designsystem.*
import com.dukaansaathi.core.model.*
import com.dukaansaathi.ui.theme.*
import androidx.compose.runtime.getValue
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun prettyPhone(p: String) = "+91 ${p.take(5)} ${p.drop(5)}"

@Composable
fun PinScreen(vm: PinViewModel, onBack: () -> Unit, onSignedIn: () -> Unit) {
    val state by vm.collectAsState()
    vm.collectSideEffect { if (it is PinSideEffect.SignedIn) onSignedIn() }
    val pin = state.pin
    val error = state.error
    val returning = state.returning

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 28.dp),
    ) {
        Box(
            Modifier.size(52.dp).clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.size(32.dp)) }

        Text(if (returning) "Welcome back" else "Create your PIN", Modifier.padding(top = 4.dp), fontFamily = Yatra, fontSize = 42.sp, lineHeight = 46.sp)
        Text(
            if (returning) "Enter your 6-digit PIN." else "Pick 6 digits you will remember. You will enter it every time you open the app.",
            Modifier.padding(top = 8.dp),
            fontSize = 22.sp,
            color = Ds.Sub,
            lineHeight = 31.sp,
        )

        Row(
            Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .drawBehind { drawRect(Ds.Ink, Offset(0f, size.height - 2.dp.toPx()), Size(size.width, 2.dp.toPx())) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(prettyPhone(state.phone), Modifier.weight(1f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Box(Modifier.heightIn(min = 48.dp).clickable(onClick = onBack).padding(horizontal = 4.dp), contentAlignment = Alignment.Center) {
                Text(
                    "Change",
                    color = Ds.Primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }

        Spacer(Modifier.size(28.dp))
        PinBlanks(pin)

        if (error.isNotEmpty()) {
            Text(error, Modifier.padding(top = 12.dp), color = Ds.Stamp, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }

        if (returning) {
            Column(Modifier.padding(top = 14.dp)) {
                Box(Modifier.heightIn(min = 48.dp), contentAlignment = Alignment.CenterStart) {
                    Text("Forgot PIN?", color = Ds.Primary, fontSize = 22.sp, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
                }
                Text("Ask your distributor to reset it.", fontSize = 18.sp, color = Ds.Sub)
            }
        }

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            if (returning) "Login" else "Set PIN and continue",
            onClick = vm::submit,
            enabled = pin.length == PIN_LENGTH,
            height = 62.dp,
        )
        Spacer(Modifier.size(14.dp))
        Keypad(vm::onKey)
    }
}
