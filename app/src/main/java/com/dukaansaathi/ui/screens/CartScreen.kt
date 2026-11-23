package com.dukaansaathi.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dukaansaathi.AppViewModel
import com.dukaansaathi.OFFER_THRESHOLD
import com.dukaansaathi.products
import com.dukaansaathi.rupees
import com.dukaansaathi.ui.Avatar
import com.dukaansaathi.ui.Ds
import com.dukaansaathi.ui.PrimaryButton
import com.dukaansaathi.ui.ProductArt
import com.dukaansaathi.ui.Stepper
import com.dukaansaathi.ui.WalletIcon
import com.dukaansaathi.ui.Yatra

@Composable
fun CartScreen(vm: AppViewModel, onBack: () -> Unit, onOpenCatalogue: () -> Unit, onPlaced: (String) -> Unit) {
    val distributor = vm.distributor
    val t = vm.totals()
    val hasOffer = products.any { it.distributorId == distributor.id && it.offer != null }
    val toGo = OFFER_THRESHOLD - t.subtotal

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 24.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(Modifier.size(52.dp).clickable(onClick = onBack), contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to catalogue", Modifier.size(32.dp))
                }
                Text("Cart", fontFamily = Yatra, fontSize = 36.sp)
            }
            Row(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Ds.GoldTint)
                    .padding(start = 6.dp, end = 14.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Avatar(distributor.initial, distributor.color, 30.dp)
                Text(distributor.name.substringBefore(' '), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (t.lines.isEmpty()) {
            Column(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Your cart is empty.", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(20.dp))
                PrimaryButton("Open catalogue", onOpenCatalogue)
            }
        } else {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                HorizontalDivider(Modifier.padding(top = 6.dp), color = Ds.Line)
                t.lines.forEach { line ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ProductArt(line.product.art, Modifier.size(60.dp))
                        Column(Modifier.weight(1f)) {
                            Text(line.product.name, fontSize = 21.sp, fontWeight = FontWeight.Bold, lineHeight = 24.sp)
                            Text(rupees(line.total), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ds.Sub)
                        }
                        Stepper(line.qty, line.product.name) { vm.setQty(line.product.id, it) }
                    }
                    HorizontalDivider(color = Ds.Line)
                }

                if (hasOffer) {
                    Row(
                        Modifier
                            .padding(top = 14.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Ds.Gold)
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(Icons.Rounded.Star, null, Modifier.size(28.dp))
                        Text(
                            if (toGo > 0) "Add ${rupees(toGo)} more, get 1 carton free" else "1 carton free added",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 25.sp,
                        )
                    }
                }

                Row(Modifier.padding(top = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("GST 18%", fontSize = 19.sp, fontWeight = FontWeight.Medium, color = Ds.Sub)
                    Text(rupees(t.gst), fontSize = 19.sp, fontWeight = FontWeight.Medium, color = Ds.Sub)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text("Total", fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    Text(rupees(t.total), fontSize = 34.sp, fontWeight = FontWeight.Bold)
                }

                Row(Modifier.padding(top = 10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InfoChip("Tue 25 Nov", Icons.Outlined.LocalShipping, Ds.GreenTint, Ds.GreenText, Modifier.weight(1f))
                    InfoChip("On delivery", WalletIcon, Ds.Soft, Ds.Ink, Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(12.dp))
            PrimaryButton(
                "Place order",
                onClick = { vm.placeOrder()?.let { onPlaced(it.id) } },
                height = 70.dp,
                fontSize = 26.sp,
            )
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    fill: androidx.compose.ui.graphics.Color,
    ink: androidx.compose.ui.graphics.Color,
    modifier: Modifier,
) {
    Row(
        modifier.clip(RoundedCornerShape(10.dp)).background(fill).padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(icon, null, Modifier.size(26.dp), tint = ink)
        Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ink)
    }
}
