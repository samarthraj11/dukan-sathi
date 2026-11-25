package com.dukaansaathi.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dukaansaathi.core.model.PCS_PER_BUNDLE
import com.dukaansaathi.core.model.Product
import com.dukaansaathi.core.model.Qty
import com.dukaansaathi.domain.pricing.rupees
import com.dukaansaathi.ui.theme.Ds
import com.dukaansaathi.ui.theme.Yatra

/** Bottom sheet to add or remove a product in petis and bundles, always showing the total pieces. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantitySheet(product: Product, qty: Qty, onChange: (Qty) -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
    ) {
        Column(
            Modifier
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                ProductArt(product.art, Modifier.size(64.dp))
                Column {
                    Text(product.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
                    Text(
                        "${product.size}, ${rupees(product.bundlePrice)} a bundle of $PCS_PER_BUNDLE",
                        fontSize = 18.sp,
                        color = Ds.Sub,
                    )
                }
            }
            HorizontalDivider(color = Ds.Line)

            UnitRow(
                title = "Peti",
                note = "${product.bundlesPerPeti} bundles, ${product.pktsPerPeti} pcs",
                price = rupees(product.petiPrice),
                count = qty.peti,
                product = product,
                onCount = { onChange(qty.copy(peti = it.coerceAtLeast(0))) },
            )
            UnitRow(
                title = "Bundle",
                note = "$PCS_PER_BUNDLE pcs",
                price = rupees(product.bundlePrice),
                count = qty.bundle,
                product = product,
                onCount = { onChange(qty.copy(bundle = it.coerceAtLeast(0))) },
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Ds.GoldTint)
                    .padding(horizontal = 18.dp, vertical = 14.dp),
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text("Total", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text("${product.pcs(qty)} pcs", fontFamily = Yatra, fontSize = 38.sp, lineHeight = 42.sp)
                }
                Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        if (qty.isEmpty) "Nothing added yet" else qty.label(),
                        fontSize = 18.sp,
                        color = Ds.Sub,
                    )
                    Text(rupees(product.bundlePrice * product.bundles(qty)), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            PrimaryButton("Done", onDismiss, height = 62.dp)
        }
    }
}

@Composable
private fun UnitRow(title: String, note: String, price: String, count: Int, product: Product, onCount: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("$note, $price", fontSize = 17.sp, color = Ds.Sub, lineHeight = 22.sp)
        }
        Stepper(count, product.name, title.lowercase(), onCount)
    }
}
