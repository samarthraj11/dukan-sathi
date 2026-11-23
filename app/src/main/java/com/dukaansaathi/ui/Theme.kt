package com.dukaansaathi.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dukaansaathi.R

/** Soft, muted palette. Keep every new color gentle. */
object Ds {
    val Ink = Color(0xFF1C1C1C)
    val Sub = Color(0xFF5A5A5A)
    val Line = Color(0xFFE3E3E3)
    val LineStrong = Color(0xFFBDBDBD)
    val Soft = Color(0xFFEDEDED)

    val Primary = Color(0xFF4454B0)
    val Gold = Color(0xFFF8DE96)
    val GoldTint = Color(0xFFFFF1CC)
    val Green = Color(0xFF3E9B66)
    val GreenText = Color(0xFF2A774B)
    val GreenTint = Color(0xFFE3F3E9)
    val Amber = Color(0xFFE08A00)
    val AmberText = Color(0xFF9A5B00)
    val Stamp = Color(0xFFC4533F)

    val Orange = Color(0xFFD9733A)
    val Teal = Color(0xFF3A9A9C)
    val Purple = Color(0xFF8570D8)
}

val Mukta = FontFamily(
    Font(R.font.mukta_regular, FontWeight.Normal),
    Font(R.font.mukta_medium, FontWeight.Medium),
    Font(R.font.mukta_semibold, FontWeight.SemiBold),
    Font(R.font.mukta_bold, FontWeight.Bold),
)

/** Headings and the logo stamp. Also covers Devanagari. */
val Yatra = FontFamily(Font(R.font.yatraone_regular, FontWeight.Normal))

@Composable
fun DukaanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Ds.Primary,
            onPrimary = Color.White,
            background = Color.White,
            surface = Color.White,
            onSurface = Ds.Ink,
        ),
    ) {
        Surface(color = Color.White, contentColor = Ds.Ink) {
            ProvideTextStyle(TextStyle(fontFamily = Mukta, fontSize = 18.sp), content)
        }
    }
}
