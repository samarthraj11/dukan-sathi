package com.dukaansaathi

import androidx.compose.ui.graphics.Color
import com.dukaansaathi.ui.Ds
import java.text.NumberFormat
import java.util.Locale

// Dummy data for the UI-only build. Replaced by the backend later.

/** Demo login: this number is an existing user, PIN 123456. Any other number sets a new PIN. */
val knownPhones = listOf("9876543210")
const val DEMO_PIN = "123456"

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

val distributors = listOf(
    Distributor("vaishnavi", "Vaishnavi Entp", "V", Ds.Orange, DistributorStatus.Active, "1 order on the way", Tone.Ok),
    Distributor("verma", "Verma Traders", "V", Ds.Teal, DistributorStatus.Active, "Due ₹2,400", Tone.Plain),
    Distributor("jain", "Jain Foods", "J", Ds.Purple, DistributorStatus.Pending, "Waiting for approval", Tone.Wait),
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

private fun c(hex: Long) = Color(0xFF000000 or hex)

val products = listOf(
    Product("marie", "vaishnavi", "Marie Gold", 456, Stock.In, offer = "10+1 free", art = Art(c(0xE7C46A), Ds.Stamp, Ds.Ink)),
    Product("glucose", "vaishnavi", "Glucose Plus", 540, Stock.In, art = Art(c(0x6FBF73), Ds.Green, Ds.Ink)),
    Product("rusk", "vaishnavi", "Milk Rusk", 360, Stock.Low, left = 4, art = Art(c(0xEE8B6E), Ds.Ink, Ds.Stamp)),
    Product("cream", "vaishnavi", "Cream Sandwich", 720, Stock.In, art = Art(c(0x9B8CF0), Ds.Ink, Ds.Sub)),
    Product("digestive", "verma", "Digestive Wheat", 600, Stock.In, art = Art(c(0xD8B27A), Ds.Teal, Ds.Ink)),
    Product("crackers", "verma", "Salted Crackers", 420, Stock.In, art = Art(c(0x8FC7C9), Ds.Ink, Ds.Teal)),
    Product("choco", "verma", "Choco Cream", 690, Stock.Low, left = 6, art = Art(c(0xB98C7A), Ds.Gold, Ds.Ink)),
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

val pastOrders = listOf(
    Order("o1042", "vaishnavi", "#1042", "Sat 22 Nov", 5880, OrderStatus.OnTheWay),
    Order("o218", "verma", "#218", "Fri 21 Nov", 2400, OrderStatus.Delivered),
    Order("o1038", "vaishnavi", "#1038", "Mon 17 Nov", 6301, OrderStatus.Delivered),
    Order("o214", "verma", "#214", "Tue 11 Nov", 3150, OrderStatus.Delivered),
)

/** "Usual" order shown on Home, in cartons. */
val usualOrder: Map<String, Map<String, Int>> = mapOf(
    "vaishnavi" to mapOf("marie" to 5, "glucose" to 3, "rusk" to 2),
    "verma" to mapOf("digestive" to 4, "crackers" to 2),
)

/** Items of each distributor's latest delivered order, for "Order again". */
val lastOrderItems: Map<String, Map<String, Int>> = mapOf(
    "vaishnavi" to mapOf("marie" to 5, "glucose" to 3, "cream" to 2),
    "verma" to mapOf("digestive" to 4, "crackers" to 2),
)

const val GST_RATE = 0.18
const val OFFER_THRESHOLD = 6000

private val inr = NumberFormat.getIntegerInstance(Locale("en", "IN"))
fun rupees(n: Int): String = "₹" + inr.format(n)
