package com.dukaansaathi.feature.catalogue

import com.dukaansaathi.core.designsystem.*
import com.dukaansaathi.core.model.*
import com.dukaansaathi.domain.pricing.rupees
import com.dukaansaathi.ui.theme.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CatalogueScreen(vm: CatalogueViewModel, onTab: (Tab) -> Unit, onAddDistributor: () -> Unit, onOpenCart: () -> Unit) {
    val state by vm.collectAsState()
    LaunchedEffect(Unit) { vm.refresh() }
    var sheetOpen by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Product?>(null) }

    val distributor = state.distributor
    val query = state.query
    val filter = state.filter
    val items = state.items
    val totals = state.totals

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Ds.Gold)
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                DistributorChip(distributor, compact = true, onClick = { sheetOpen = true })
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(Icons.Rounded.Search, null, Modifier.size(26.dp), tint = Ds.Sub)
                BasicTextField(
                    value = query,
                    onValueChange = vm::setQuery,
                    modifier = Modifier.weight(1f).semantics { contentDescription = "Search products" },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = Mukta, fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Ds.Ink),
                    cursorBrush = SolidColor(Ds.Primary),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    decorationBox = { inner ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (query.isEmpty()) Text("Search", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Ds.Sub)
                            inner()
                        }
                    },
                )
            }
        }

        Row(Modifier.padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FilterChip("All", filter == CatalogueFilter.All) { vm.setFilter(CatalogueFilter.All) }
            FilterChip("Usual", filter == CatalogueFilter.Usual) { vm.setFilter(CatalogueFilter.Usual) }
            FilterChip("Offers", filter == CatalogueFilter.Offers, highlight = true) { vm.setFilter(CatalogueFilter.Offers) }
        }

        Box(Modifier.weight(1f)) {
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 96.dp),
            ) {
                items(items, key = { it.id }) { p ->
                    val qty = state.quantities[p.id] ?: Qty()
                    Row(
                        Modifier.fillMaxWidth().clickable { editing = p }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ProductArt(p.art, Modifier.size(72.dp))
                        Column(Modifier.weight(1f)) {
                            Text(p.name, fontSize = 21.sp, fontWeight = FontWeight.Bold, lineHeight = 24.sp)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(rupees(p.bundlePrice), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                Text(" / bundle", fontSize = 17.sp, color = Ds.Sub, modifier = Modifier.padding(bottom = 2.dp))
                            }
                            Row(Modifier.heightIn(min = 24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                when (p.stock) {
                                    Stock.In -> Box(Modifier.size(10.dp).clip(CircleShape).background(Ds.Green))
                                    Stock.Low -> {
                                        Box(Modifier.size(10.dp).clip(CircleShape).background(Ds.Amber))
                                        Text("${p.left} bundles left", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Ds.AmberText)
                                    }
                                    Stock.Out -> Text("Out of stock", fontSize = 17.sp, color = Ds.Sub)
                                }
                                p.offer?.let {
                                    Text(
                                        it,
                                        Modifier.clip(RoundedCornerShape(6.dp)).background(Ds.Gold).padding(horizontal = 8.dp),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                        if (qty.isEmpty) {
                            Box(
                                Modifier
                                    .heightIn(min = 48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Ds.Primary)
                                    .semantics { contentDescription = "Add ${p.name}" }
                                    .padding(horizontal = 24.dp),
                                contentAlignment = Alignment.Center,
                            ) { Text("Add", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                        } else {
                            Box(
                                Modifier
                                    .heightIn(min = 48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Ds.GoldTint)
                                    .border(2.dp, Ds.Primary, RoundedCornerShape(10.dp))
                                    .semantics { contentDescription = "${p.name}, ${qty.pcs} pieces. Change" }
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center,
                            ) { Text("${qty.pcs} pcs", color = Ds.Primary, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                    HorizontalDivider(color = Ds.Line)
                }
                if (items.isEmpty()) {
                    item { Text("Nothing found. Try another search.", Modifier.padding(vertical = 24.dp), fontSize = 20.sp, color = Ds.Sub) }
                }
            }

            if (totals.pcs > 0) {
                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Ds.Primary)
                        .padding(start = 18.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text("${totals.pcs} pcs", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Medium)
                        Text(rupees(totals.subtotal), color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        Modifier
                            .heightIn(min = 54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Ds.Gold)
                            .clickable(onClick = onOpenCart)
                            .padding(horizontal = 22.dp),
                        contentAlignment = Alignment.Center,
                    ) { Text("View cart", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }

        BottomNav(Tab.Catalogue, onTab)
    }

    editing?.let { p ->
        QuantitySheet(
            product = p,
            qty = state.quantities[p.id] ?: Qty(),
            onChange = { vm.setQty(p.id, it) },
            onDismiss = { editing = null },
        )
    }

    if (sheetOpen) {
        DistributorSheet(state.distributors, distributor.id, onSelect = vm::selectDistributor, onDismiss = { sheetOpen = false }, onAdd = {
            sheetOpen = false
            onAddDistributor()
        })
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, highlight: Boolean = false, onClick: () -> Unit) {
    val shape = RoundedCornerShape(23.dp)
    Box(
        Modifier
            .heightIn(min = 46.dp)
            .clip(shape)
            .background(if (selected) Ds.Primary else if (highlight) Ds.GoldTint else Color.White)
            .then(if (selected || highlight) Modifier else Modifier.border(2.dp, Ds.LineStrong, shape))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            fontSize = 19.sp,
            fontWeight = if (selected || highlight) FontWeight.Bold else FontWeight.SemiBold,
            color = if (selected) Color.White else Ds.Ink,
        )
    }
}
