package com.dukaansaathi.core.designsystem

import com.dukaansaathi.ui.theme.Ds
import com.dukaansaathi.ui.theme.Mukta
import com.dukaansaathi.ui.theme.Yatra
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.Stroke
import com.dukaansaathi.core.model.Motif
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dukaansaathi.core.model.Art
import com.dukaansaathi.core.model.Distributor

@Composable
fun Avatar(initial: String, color: Color, size: Dp = 38.dp, faded: Boolean = false) {
    Box(
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = if (faded) 0.55f else 1f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(initial, color = Color.White, fontFamily = Yatra, fontSize = (size.value * 0.52f).sp)
    }
}

/** Simple drawn pack, tinted per product. Replaced by real photos later. */
@Composable
fun ProductArt(art: Art, modifier: Modifier = Modifier) {
    Canvas(modifier.aspectRatio(1f)) {
        val u = size.width / 64f
        fun o(x: Float, y: Float) = Offset(x * u, y * u)
        fun sz(w: Float, h: Float) = Size(w * u, h * u)
        val biscuit = Color(0xFFDDB574)
        val biscuitDark = Color(0xFFBF9650)

        // Tile, then an upright packet with crimped ends and a white label.
        drawRoundRect(lerp(art.pack, Color.White, 0.78f), cornerRadius = CornerRadius(10 * u, 10 * u))
        drawRoundRect(art.pack, o(11f, 6f), sz(42f, 52f), CornerRadius(3 * u, 3 * u))
        val crimp = lerp(art.pack, Color.Black, 0.18f)
        drawRect(crimp, o(11f, 6f), sz(42f, 4f))
        drawRect(crimp, o(11f, 54f), sz(42f, 4f))
        for (i in 0..6) {
            drawLine(lerp(art.pack, Color.White, 0.35f), o(14f + i * 6f, 6.5f), o(14f + i * 6f, 9.5f), strokeWidth = 1f * u)
            drawLine(lerp(art.pack, Color.White, 0.35f), o(14f + i * 6f, 54.5f), o(14f + i * 6f, 57.5f), strokeWidth = 1f * u)
        }
        drawRoundRect(Color.White, o(14f, 14f), sz(36f, 30f), CornerRadius(3 * u, 3 * u))
        drawRoundRect(art.accent, o(14f, 47f), sz(36f, 4f), CornerRadius(1.5f * u, 1.5f * u))

        when (art.motif) {
            Motif.Round -> {
                drawCircle(biscuit, 11 * u, o(32f, 29f))
                drawCircle(biscuitDark, 8 * u, o(32f, 29f), style = Stroke(1f * u))
                for (i in 0 until 8) {
                    val ang = Math.toRadians(i * 45.0)
                    drawCircle(biscuitDark, 1.1f * u, o(32f + 5.2f * Math.cos(ang).toFloat(), 29f + 5.2f * Math.sin(ang).toFloat()))
                }
            }
            Motif.Square -> {
                drawRoundRect(biscuit, o(21f, 18f), sz(22f, 22f), CornerRadius(2 * u, 2 * u))
                for (r in 0..2) for (k in 0..2) drawCircle(biscuitDark, 1.3f * u, o(26f + k * 6f, 23f + r * 6f))
            }
            Motif.Butter -> {
                drawCircle(biscuit, 10 * u, o(30f, 32f))
                drawCircle(biscuitDark, 7 * u, o(30f, 32f), style = Stroke(1f * u))
                drawRoundRect(art.accent2, o(33f, 17f), sz(13f, 8f), CornerRadius(1.5f * u, 1.5f * u))
                drawLine(Color.White, o(35f, 19.5f), o(41f, 19.5f), strokeWidth = 1.2f * u)
            }
            Motif.Drop -> {
                val drop = Path().apply {
                    moveTo(32 * u, 17 * u)
                    cubicTo(40 * u, 27 * u, 42 * u, 31 * u, 42 * u, 34 * u)
                    cubicTo(42 * u, 40 * u, 37 * u, 42 * u, 32 * u, 42 * u)
                    cubicTo(27 * u, 42 * u, 22 * u, 40 * u, 22 * u, 34 * u)
                    cubicTo(22 * u, 31 * u, 24 * u, 27 * u, 32 * u, 17 * u)
                    close()
                }
                drawPath(drop, art.pack.copy(alpha = 0.35f))
                drawPath(drop, art.accent, style = Stroke(1.6f * u))
                drawCircle(Color.White, 2.2f * u, o(28f, 33f))
            }
            Motif.Sandwich -> {
                drawRoundRect(biscuit, o(19f, 19f), sz(26f, 8f), CornerRadius(3.5f * u, 3.5f * u))
                drawRoundRect(art.accent2, o(18f, 27f), sz(28f, 6f), CornerRadius(3 * u, 3 * u))
                drawRoundRect(biscuit, o(19f, 33f), sz(26f, 8f), CornerRadius(3.5f * u, 3.5f * u))
                for (k in 0..2) drawCircle(biscuitDark, 1f * u, o(25f + k * 7f, 23f))
            }
            Motif.TwoTone -> {
                drawCircle(Color(0xFFFFF6E3), 11 * u, o(32f, 29f))
                drawArc(art.accent2, 90f, 180f, true, o(21f, 18f), sz(22f, 22f))
                drawCircle(art.accent2, 11 * u, o(32f, 29f), style = Stroke(1f * u))
                drawCircle(Color(0xFFFFF6E3), 2.4f * u, o(27f, 29f))
                drawCircle(art.accent2, 2.4f * u, o(37f, 29f))
            }
            Motif.Cake -> {
                drawRoundRect(Color(0xFFF1D9A0), o(19f, 24f), sz(26f, 14f), CornerRadius(2 * u, 2 * u))
                drawRoundRect(art.accent2, o(18f, 19f), sz(28f, 7f), CornerRadius(3 * u, 3 * u))
                if (art.pack == Color(0xFFA58BD0)) {
                    drawCircle(Color(0xFFD9695A), 2f * u, o(25f, 22.5f))
                    drawCircle(Color(0xFF6FAF77), 2f * u, o(32f, 22.5f))
                    drawCircle(Color(0xFFD9695A), 2f * u, o(39f, 22.5f))
                } else {
                    drawRect(art.accent2, o(23f, 26f), sz(3f, 5f))
                    drawRect(art.accent2, o(35f, 26f), sz(3f, 7f))
                }
            }
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 64.dp,
    fontSize: TextUnit = 24.sp,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(height),
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Ds.Primary,
            contentColor = Color.White,
            disabledContainerColor = Ds.Primary.copy(alpha = 0.4f),
            disabledContentColor = Color.White,
        ),
    ) { Text(text, fontSize = fontSize, fontWeight = FontWeight.Bold) }
}

@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    fontSize: TextUnit = 22.sp,
    leading: (@Composable () -> Unit)? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(height),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.5.dp, Ds.Primary),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Ds.Primary),
    ) {
        leading?.invoke()
        Text(text, fontSize = fontSize, fontWeight = FontWeight.Bold)
    }
}

/** Count stepper (peti or bundle). Big targets, no typing. */
@Composable
fun Stepper(qty: Int, name: String, unit: String, onChange: (Int) -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        Modifier
            .border(2.dp, Ds.Ink, shape)
            .clip(shape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(42.dp, 48.dp)
                .semantics { contentDescription = "Remove one $unit of $name" }
                .clickable { onChange(qty - 1) },
            contentAlignment = Alignment.Center,
        ) { Text("−", fontSize = 28.sp, fontWeight = FontWeight.SemiBold) }
        Box(Modifier.widthIn(min = 30.dp), contentAlignment = Alignment.Center) {
            Text("$qty", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Box(
            Modifier
                .size(42.dp, 48.dp)
                .background(Ds.Primary)
                .semantics { contentDescription = "Add one $unit of $name" }
                .clickable { onChange(qty + 1) },
            contentAlignment = Alignment.Center,
        ) { Text("+", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = Color.White) }
    }
}

/** Shows who the shop is ordering from. Tapping opens the distributor sheet. */
@Composable
fun DistributorChip(distributor: Distributor, onClick: () -> Unit, compact: Boolean = false) {
    Row(
        Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .heightIn(min = if (compact) 48.dp else 52.dp)
            .padding(start = if (compact) 6.dp else 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = "Ordering from ${distributor.name}. Change distributor"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp),
    ) {
        Avatar(distributor.initial, distributor.color, if (compact) 34.dp else 38.dp)
        Text(distributor.name, fontSize = if (compact) 20.sp else 21.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.Rounded.KeyboardArrowDown, null, Modifier.size(if (compact) 20.dp else 22.dp))
    }
}

enum class Tab(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Outlined.Home),
    Catalogue("catalogue", "Catalogue", Icons.Outlined.GridView),
    Orders("orders", "Orders", Icons.Outlined.ReceiptLong),
    Account("account", "Account", Icons.Outlined.Person),
}

@Composable
fun BottomNav(current: Tab, onSelect: (Tab) -> Unit) {
    Column(Modifier.background(Color.White).navigationBarsPadding()) {
        HorizontalDivider(color = Ds.Line)
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Tab.entries.forEach { tab ->
                val selected = tab == current
                val tint = if (selected) Ds.Primary else Ds.Sub
                Column(
                    Modifier
                        .weight(1f)
                        .heightIn(min = 68.dp)
                        .clickable { onSelect(tab) }
                        .drawBehind { if (selected) drawRect(Ds.Gold, size = Size(size.width, 4.dp.toPx())) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(tab.icon, null, Modifier.size(30.dp), tint = tint)
                    Text(
                        tab.label,
                        color = tint,
                        fontSize = 16.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

const val PIN_LENGTH = 6

/** Six writing lines; a dot appears on each digit entered. */
@Composable
fun PinBlanks(value: String) {
    val blink = rememberInfiniteTransition(label = "cursor").animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(530, easing = LinearEasing), RepeatMode.Reverse),
        label = "cursorAlpha",
    )
    Row(
        Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "${value.length} of $PIN_LENGTH digits entered" },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        repeat(PIN_LENGTH) { i ->
            val filled = i < value.length
            val lineColor = if (filled) Ds.Ink else Color(0xFF8E8E8E)
            Box(
                Modifier
                    .weight(1f)
                    .height(50.dp)
                    .drawBehind {
                        val w = 4.dp.toPx()
                        drawRect(lineColor, Offset(0f, size.height - w), Size(size.width, w))
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (filled) Box(Modifier.size(20.dp).clip(CircleShape).background(Ds.Ink))
                else if (i == value.length) Box(
                    Modifier.width(2.5.dp).height(30.dp).graphicsLayer { alpha = blink.value }.background(Ds.Primary),
                )
            }
        }
    }
}

/** Calculator-style keypad. Calls [onKey] with a digit, or "del". */
@Composable
fun Keypad(onKey: (String) -> Unit) {
    val rows = listOf(listOf("1", "2", "3"), listOf("4", "5", "6"), listOf("7", "8", "9"), listOf("", "0", "del"))
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Ds.LineStrong)
            .padding(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        rows.forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                row.forEach { key ->
                    Box(
                        Modifier
                            .weight(1f)
                            .height(64.dp)
                            .background(if (key == "" || key == "del") Ds.Soft else Color.White)
                            .then(if (key == "") Modifier else Modifier.clickable { onKey(key) }),
                        contentAlignment = Alignment.Center,
                    ) {
                        when (key) {
                            "" -> {}
                            "del" -> Icon(
                                Icons.AutoMirrored.Outlined.Backspace,
                                contentDescription = "Delete last digit",
                                modifier = Modifier.size(34.dp),
                            )
                            else -> Text(key, fontSize = 36.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

/** Icon for the Cart's payment chip lives here so screens share one import. */
val WalletIcon: ImageVector = Icons.Outlined.AccountBalanceWallet
