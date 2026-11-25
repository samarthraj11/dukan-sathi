package com.dukaansaathi.domain.pricing

import com.dukaansaathi.core.model.GST_RATE
import com.dukaansaathi.core.model.Line
import com.dukaansaathi.core.model.Product
import com.dukaansaathi.core.model.Qty
import com.dukaansaathi.core.model.Totals
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

/** Prices a cart ("productId" to peti and bundles) against the catalogue, lines in catalogue order. */
fun priceItems(items: Map<String, Qty>, catalogue: List<Product>): Totals {
    val lines = items.filterValues { !it.isEmpty }.mapNotNull { (id, qty) ->
        catalogue.firstOrNull { it.id == id }?.let { Line(it, qty, it.bundlePrice * qty.bundles) }
    }.sortedBy { catalogue.indexOf(it.product) }
    val subtotal = lines.sumOf { it.total }
    val gst = (subtotal * GST_RATE).roundToInt()
    return Totals(lines, subtotal, gst, subtotal + gst, lines.sumOf { it.qty.pcs })
}

private val inr = NumberFormat.getIntegerInstance(Locale("en", "IN"))

fun rupees(n: Int): String = "₹" + inr.format(n)
