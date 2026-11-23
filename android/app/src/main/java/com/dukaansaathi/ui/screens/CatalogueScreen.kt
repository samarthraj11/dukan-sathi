package com.dukaansaathi.ui.screens

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
import com.dukaansaathi.AppViewModel
import com.dukaansaathi.Stock
import com.dukaansaathi.products
import com.dukaansaathi.rupees
import com.dukaansaathi.ui.BottomNav
import com.dukaansaathi.ui.Ds
import com.dukaansaathi.ui.DistributorChip
import com.dukaansaathi.ui.DistributorSheet
import com.dukaansaathi.ui.Mukta
import com.dukaansaathi.ui.ProductArt
import com.dukaansaathi.ui.Stepper
import com.dukaansaathi.ui.Tab
import com.dukaansaathi.usualOrder

private enum class Filter { All, Usual, Offers }

@Composable
fun CatalogueScreen(vm: AppViewModel, onTab: (Tab) -> Unit, onAddDistributor: () -> Unit, onOpenCart: () -> Unit) {
    var sheetOpen by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(Filter.All) }

    val distributor = vm.distributor
    val usualIds = usualOrder[distributor.id].orEmpty().keys
    val items = products
        .filter { it.distributorId == distributor.id }
        .filter {
            when (filter) {
                Filter.All -> true
                Filter.Usual -> it.id in usualIds
                Filter.Offers -> it.offer != null
            }
        }
        .filter { it.name.contains(query.trim(), ignoreCase = true) }
    val totals = vm.totals()

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
                    onValueChange = { query = it },
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
            FilterChip("All", filter == Filter.All) { filter = Filter.All }
            FilterChip("Usual", filter == Filter.Usual) { filter = Filter.Usual }
            FilterChip("Offers", filter == Filter.Offers, highlight = true) { filter = Filter.Offers }
        }

        Box(Modifier.weight(1f)) {
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 96.dp),
            ) {
                items(items, key = { it.id }) { p ->
                    val qty = vm.qty(p.id)
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ProductArt(p.art, Modifier.size(72.dp))
                        Column(Modifier.weight(1f)) {
                            Text(p.name, fontSize = 21.sp, fontWeight = FontWeight.Bold, lineHeight = 24.sp)
                            Text(rupees(p.price), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Row(Modifier.heightIn(min = 24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                when (p.stock) {
                                    Stock.In -> Box(Modifier.size(10.dp).clip(CircleShape).background(Ds.Green))
                                    Stock.Low -> {
                                        Box(Modifier.size(10.dp).clip(CircleShape).background(Ds.Amber))
                                        Text("${p.left} left", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Ds.AmberText)
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
                        if (qty > 0) {
                            Stepper(qty, p.name) { vm.setQty(p.id, it) }
                        } else {
                            Box(
                                Modifier
                                    .heightIn(min = 48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Ds.Primary)
                                    .clickable { vm.setQty(p.id, 1) }
                                    .semantics { contentDescription = "Add ${p.name}" }
                                    .padding(horizontal = 24.dp),
                                contentAlignment = Alignment.Center,
                            ) { Text("Add", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                    HorizontalDivider(color = Ds.Line)
                }
                if (items.isEmpty()) {
                    item { Text("Nothing found. Try another search.", Modifier.padding(vertical = 24.dp), fontSize = 20.sp, color = Ds.Sub) }
                }
            }

            if (totals.cartons > 0) {
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
                        Text("${totals.cartons} cartons", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Medium)
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

    if (sheetOpen) {
        DistributorSheet(vm, onDismiss = { sheetOpen = false }, onAdd = {
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
