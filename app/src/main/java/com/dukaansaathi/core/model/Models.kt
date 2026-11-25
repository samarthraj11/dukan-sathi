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

data class Art(val bg: Color, val stripe: Color, val line: Color)

data class Product(
    val id: String,
    val distributorId: String,
    val name: String,
    val bundlePrice: Int, // before GST; a peti is 12 bundles
    val stock: Stock,
    val left: Int? = null,
    val offer: String? = null,
    val art: Art,
) {
    val petiPrice: Int get() = bundlePrice * BUNDLES_PER_PETI
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

/** Shops order only in whole petis and bundles, never single pieces. */
data class Qty(val peti: Int = 0, val bundle: Int = 0) {
    val bundles: Int get() = peti * BUNDLES_PER_PETI + bundle
    val pcs: Int get() = bundles * PCS_PER_BUNDLE
    val isEmpty: Boolean get() = bundles == 0
    fun label(): String = listOfNotNull(
        peti.takeIf { it > 0 }?.let { "$it peti" },
        bundle.takeIf { it > 0 }?.let { "$it bundle" },
    ).joinToString(" + ")
}

data class Line(val product: Product, val qty: Qty, val total: Int)

data class Totals(val lines: List<Line>, val subtotal: Int, val gst: Int, val total: Int, val pcs: Int)

const val PCS_PER_BUNDLE = 12
const val BUNDLES_PER_PETI = 12
const val GST_RATE = 0.18
const val OFFER_THRESHOLD = 6000
