package com.dukaansaathi.core.model

import androidx.compose.ui.graphics.Color

enum class DistributorStatus { Active, Pending }
enum class Tone { Ok, Plain, Wait }

data class Distributor(
    val id: String,
    val name: String,
    val initial: String,
    val color: Color,
    val status: DistributorStatus,
    val note: String,
    val tone: Tone,
)

enum class Stock { In, Low, Out }

enum class Motif { Round, Square, Butter, Drop, Sandwich, TwoTone, Cake }

/** How a product's pack is drawn: wrapper colour, band colour, the picture on it. */
data class Art(val pack: Color, val accent: Color, val motif: Motif, val accent2: Color = Color.White)

data class Product(
    val id: String,
    val distributorId: String,
    val name: String,
    val size: String, // pack weight, e.g. "35 g"
    val bundlePrice: Int, // before GST; a bundle is 12 packets
    val stock: Stock,
    val left: Int? = null,
    val offer: String? = null,
    val pktsPerPeti: Int = 144, // most petis are 12 bundles; some are smaller
    val art: Art,
) {
    val bundlesPerPeti: Int get() = pktsPerPeti / PCS_PER_BUNDLE
    val petiPrice: Int get() = bundlePrice * bundlesPerPeti
    fun bundles(qty: Qty): Int = qty.peti * bundlesPerPeti + qty.bundle
    fun pcs(qty: Qty): Int = bundles(qty) * PCS_PER_BUNDLE
}

enum class OrderStatus(val label: String, val step: Int) {
    Placed("Placed", 1),
    OnTheWay("On the way", 4),
    Delivered("Delivered", 5),
}

data class Order(
    val id: String,
    val distributorId: String,
    val number: String,
    val date: String,
    val amount: Int,
    val status: OrderStatus,
)

/** Shops order only in whole petis and bundles, never single pieces. A bundle is 12 packets. */
data class Qty(val peti: Int = 0, val bundle: Int = 0) {
    val isEmpty: Boolean get() = peti == 0 && bundle == 0
    fun label(): String = listOfNotNull(
        peti.takeIf { it > 0 }?.let { "$it peti" },
        bundle.takeIf { it > 0 }?.let { "$it bundle" },
    ).joinToString(" + ")
}

data class Line(val product: Product, val qty: Qty, val total: Int, val pcs: Int)

data class Totals(val lines: List<Line>, val subtotal: Int, val gst: Int, val total: Int, val pcs: Int)

const val PCS_PER_BUNDLE = 12
const val GST_RATE = 0.18
const val OFFER_THRESHOLD = 6000
