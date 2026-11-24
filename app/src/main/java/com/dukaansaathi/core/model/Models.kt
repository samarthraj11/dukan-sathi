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
    val price: Int, // per carton, before GST
    val stock: Stock,
    val left: Int? = null,
    val offer: String? = null,
    val art: Art,
)

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

data class Line(val product: Product, val qty: Int, val total: Int)

data class Totals(val lines: List<Line>, val subtotal: Int, val gst: Int, val total: Int, val cartons: Int)

const val GST_RATE = 0.18
const val OFFER_THRESHOLD = 6000
