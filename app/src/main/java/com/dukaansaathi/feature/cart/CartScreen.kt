package com.dukaansaathi.feature.cart

import com.dukaansaathi.core.designsystem.*
import com.dukaansaathi.core.model.*
import com.dukaansaathi.domain.pricing.rupees
import com.dukaansaathi.ui.theme.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun CartScreen(vm: CartViewModel, onBack: () -> Unit, onOpenCatalogue: () -> Unit, onPlaced: (String) -> Unit) {
    val state by vm.collectAsState()
    LaunchedEffect(Unit) { vm.refresh() }
    vm.collectSideEffect { if (it is CartSideEffect.OrderPlaced) onPlaced(it.orderId) }
    val distributor = state.distributor
    val t = state.totals
    val hasOffer = state.hasOffer
    val toGo = state.toGo
    var editing by remember { mutableStateOf<Product?>(null) }
    CartQuantitySheet(vm, editing, t.lines) { editing = null }

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
                            Text("${line.qty.label()}, ${line.pcs} pcs", fontSize = 18.sp, color = Ds.Sub, lineHeight = 22.sp)
                            Text(rupees(line.total), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            Modifier
                                .heightIn(min = 48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(2.dp, Ds.Primary, RoundedCornerShape(10.dp))
                                .clickable { editing = line.product }
                                .semantics { contentDescription = "Change ${line.product.name}" }
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center,
                        ) { Text("Change", color = Ds.Primary, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
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
                            if (toGo > 0) "Add ${rupees(toGo)} more, get 1 bundle free" else "1 bundle free added",
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
                onClick = vm::placeOrder,
                height = 70.dp,
                fontSize = 26.sp,
            )
        }
    }
}

@Composable
private fun CartQuantitySheet(vm: CartViewModel, product: Product?, lines: List<Line>, onDismiss: () -> Unit) {
    if (product == null) return
    QuantitySheet(
        product = product,
        qty = lines.firstOrNull { it.product.id == product.id }?.qty ?: Qty(),
        onChange = { vm.setQty(product.id, it) },
        onDismiss = onDismiss,
    )
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
