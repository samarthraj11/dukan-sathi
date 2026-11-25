package com.dukaansaathi.feature.login

import com.dukaansaathi.core.designsystem.*
import com.dukaansaathi.core.model.*
import com.dukaansaathi.ui.theme.*
import androidx.compose.runtime.getValue
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private enum class Lang { English, Hindi }

@Composable
fun PhoneScreen(vm: PhoneViewModel, onContinue: (String) -> Unit) {
    val state by vm.collectAsState()
    vm.collectSideEffect { if (it is PhoneSideEffect.OpenPin) onContinue(it.phone) }
    var lang by remember { mutableStateOf(Lang.English) }
    val ready = state.ready

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        // Scrolls when the keyboard is open, so the button below stays reachable.
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 20.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                LangTab("English", lang == Lang.English, 20.sp) { lang = Lang.English }
                LangTab("हिन्दी", lang == Lang.Hindi, 21.sp) { lang = Lang.Hindi }
            }

            // Rubber-stamp logo: a double border drawn as two thin ones.
            Box(
                Modifier
                    .padding(top = 36.dp)
                    .border(1.5.dp, Ds.Stamp, RoundedCornerShape(6.dp))
                    .padding(3.dp)
                    .border(1.5.dp, Ds.Stamp, RoundedCornerShape(4.dp))
                    .padding(horizontal = 15.dp, vertical = 8.dp),
            ) {
                Column {
                    Text("Dukaan Saathi", color = Ds.Stamp, fontFamily = Yatra, fontSize = 36.sp, maxLines = 1, softWrap = false)
                    Text("दुकान साथी", color = Ds.Stamp, fontFamily = Yatra, fontSize = 30.sp)
                }
            }

            Column(Modifier.padding(top = 32.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Order from your distributor. No calls, no paper slips.", fontSize = 26.sp, fontWeight = FontWeight.SemiBold, lineHeight = 34.sp)
                Text("फ़ोन और पर्ची की ज़रूरत नहीं।", fontSize = 23.sp, fontWeight = FontWeight.Medium, color = Ds.Sub, lineHeight = 32.sp)
            }

            Text("Your mobile number", Modifier.padding(top = 32.dp, bottom = 8.dp), fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            PhoneField(state.phone, vm::setPhone, "98765 43210") { vm.continueToPin() }
            Text("Next you will set a 6-digit PIN. No OTP needed.", Modifier.padding(top = 8.dp), fontSize = 18.sp, color = Ds.Sub, lineHeight = 25.sp)
        }

        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 32.dp)) {
            PrimaryButton("Continue", vm::continueToPin, enabled = ready, height = 68.dp, fontSize = 25.sp)
            Text("Distributor? Use the number registered with us.", Modifier.padding(top = 14.dp), fontSize = 18.sp, color = Ds.Sub, lineHeight = 25.sp)
        }
    }
}

@Composable
private fun LangTab(label: String, selected: Boolean, size: androidx.compose.ui.unit.TextUnit, onClick: () -> Unit) {
    Box(
        Modifier
            .heightIn(min = 48.dp)
            .clickable(onClick = onClick)
            .drawBehind {
                if (selected) drawRect(Ds.Primary, Offset(0f, this.size.height - 3.dp.toPx()), Size(this.size.width, 3.dp.toPx()))
            }
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, fontSize = size, fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold, color = if (selected) Ds.Ink else Ds.Sub)
    }
}
