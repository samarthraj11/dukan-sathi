package com.dukaansaathi.data.local

import androidx.compose.ui.graphics.Color
import com.dukaansaathi.core.model.Art
import com.dukaansaathi.core.model.Distributor
import com.dukaansaathi.core.model.DistributorStatus
import com.dukaansaathi.core.model.Motif
import com.dukaansaathi.core.model.Order
import com.dukaansaathi.core.model.OrderStatus
import com.dukaansaathi.core.model.Product
import com.dukaansaathi.core.model.Qty
import com.dukaansaathi.core.model.Stock
import com.dukaansaathi.core.model.Tone
import com.dukaansaathi.ui.theme.Ds

// Dummy data for the UI-only build. Replaced by the backend later.

/** Demo login: this number is an existing user, PIN 123456. Any other number sets a new PIN. */
val knownPhones = listOf("9876543210")
const val DEMO_PIN = "123456"

val dummyDistributors = listOf(
    Distributor("vaishnavi", "Vaishnavi Entp", "V", Ds.Orange, DistributorStatus.Active, "1 order on the way", Tone.Ok),
    Distributor("verma", "Verma Traders", "V", Ds.Teal, DistributorStatus.Active, "Due ₹2,400", Tone.Plain),
    Distributor("jain", "Jain Foods", "J", Ds.Purple, DistributorStatus.Pending, "Waiting for approval", Tone.Wait),
)

private fun c(hex: Long) = Color(0xFF000000 or hex)

private val creamOrange = c(0xF7B27A)
private val creamElaichi = c(0xCDE5A6)
private val creamChoco = c(0x5E4234)
private val creamMango = c(0xF5BC45)
private val creamStrawberry = c(0xF3A3B4)

// Prices are placeholders (a bundle is 12 packets) until the distributor shares the real rate list.
val dummyProducts = listOf(
    // Vaishnavi Entp: the 13 items in stock. Packets per peti follow the distributor's list.
    Product("marie70", "vaishnavi", "Dream Marie", "70 g", 120, Stock.In, pktsPerPeti = 48, art = Art(c(0xE7C46A), Ds.Stamp, Motif.Round)),
    Product("marie35", "vaishnavi", "Dream Marie", "35 g", 60, Stock.In, offer = "10+1 free", art = Art(c(0xF0D78F), Ds.Stamp, Motif.Round)),
    Product("crackers", "vaishnavi", "Hamara Crackers", "33 g", 60, Stock.In, art = Art(c(0x8FC7C9), Ds.Ink, Motif.Square)),
    Product("butter", "vaishnavi", "Butter Gold Plus", "31 g", 60, Stock.In, art = Art(c(0xE9A653), Ds.Ink, Motif.Butter, c(0xF6DA7B))),
    Product("milki", "vaishnavi", "Milki Bite", "36 g", 60, Stock.Low, left = 4, art = Art(c(0x9DB8E8), Ds.Primary, Motif.Drop)),
    Product("orange", "vaishnavi", "Orange Cream", "29 g", 60, Stock.In, art = Art(c(0xEE9B6E), Ds.Ink, Motif.Sandwich, creamOrange)),
    Product("elaichi", "vaishnavi", "Elaichi Cream", "29 g", 60, Stock.In, art = Art(c(0x8FCB9A), Ds.Ink, Motif.Sandwich, creamElaichi)),
    Product("choco", "vaishnavi", "Choco Cream", "29 g", 60, Stock.In, art = Art(c(0x9C7B66), Ds.Gold, Motif.Sandwich, creamChoco)),
    Product("mango", "vaishnavi", "Mango Cream", "31 g", 60, Stock.In, art = Art(c(0xF3C14B), Ds.Ink, Motif.Sandwich, creamMango)),
    Product("strawberry", "vaishnavi", "Strawberry Cream", "29 g", 60, Stock.In, art = Art(c(0xE8899B), Ds.Ink, Motif.Sandwich, creamStrawberry)),
    Product("bnw", "vaishnavi", "Black & White", "31 g", 60, Stock.In, art = Art(c(0x707078), Color.White, Motif.TwoTone, c(0x5B4636))),
    Product("chococake", "vaishnavi", "Choco Cake", "30 g", 120, Stock.In, pktsPerPeti = 60, art = Art(c(0x8A6A5C), Ds.Gold, Motif.Cake, c(0x5E4234))),
    Product("fruitcake", "vaishnavi", "Fruit Cake", "32 g", 120, Stock.Low, left = 3, pktsPerPeti = 60, art = Art(c(0xA58BD0), Ds.Gold, Motif.Cake, c(0xF6E9D2))),

    Product("digestive", "verma", "Digestive Wheat", "40 g", 156, Stock.In, art = Art(c(0xD8B27A), Ds.Teal, Motif.Round)),
    Product("vcrackers", "verma", "Salted Crackers", "33 g", 108, Stock.In, art = Art(c(0x8FC7C9), Ds.Ink, Motif.Square)),
    Product("vchoco", "verma", "Choco Cream", "29 g", 168, Stock.Low, left = 6, art = Art(c(0xB98C7A), Ds.Gold, Motif.Sandwich, creamChoco)),
)

val dummyPastOrders = listOf(
    Order("o1042", "vaishnavi", "#1042", "Sat 22 Nov", 5880, OrderStatus.OnTheWay),
    Order("o218", "verma", "#218", "Fri 21 Nov", 2400, OrderStatus.Delivered),
    Order("o1038", "vaishnavi", "#1038", "Mon 17 Nov", 6301, OrderStatus.Delivered),
    Order("o214", "verma", "#214", "Tue 11 Nov", 3150, OrderStatus.Delivered),
)

/** "Usual" order shown on Home, in petis and bundles. */
val dummyUsualOrder: Map<String, Map<String, Qty>> = mapOf(
    "vaishnavi" to mapOf("marie35" to Qty(peti = 1), "butter" to Qty(bundle = 6), "orange" to Qty(bundle = 4)),
    "verma" to mapOf("digestive" to Qty(bundle = 8), "vcrackers" to Qty(bundle = 6)),
)

/** Items of each distributor's latest delivered order, for "Order again". */
val dummyLastOrder: Map<String, Map<String, Qty>> = mapOf(
    "vaishnavi" to mapOf("marie35" to Qty(peti = 1), "crackers" to Qty(bundle = 3), "choco" to Qty(bundle = 2)),
    "verma" to mapOf("digestive" to Qty(bundle = 8), "vcrackers" to Qty(bundle = 6)),
)
