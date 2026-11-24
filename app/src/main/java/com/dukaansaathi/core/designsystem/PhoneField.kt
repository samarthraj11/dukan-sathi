package com.dukaansaathi.core.designsystem

import com.dukaansaathi.ui.theme.Ds
import com.dukaansaathi.ui.theme.Mukta
import com.dukaansaathi.ui.theme.Yatra
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/** Boxed 10-digit mobile number field with a fixed +91 prefix. */
@Composable
fun PhoneField(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    onDone: () -> Unit = {},
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .border(2.5.dp, Ds.Primary, shape)
            .clip(shape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .background(Ds.Soft)
                .heightIn(min = 72.dp)
                .drawBehind { drawRect(Ds.LineStrong, Offset(size.width - 2.dp.toPx(), 0f), Size(2.dp.toPx(), size.height)) }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) { Text("+91", fontSize = 30.sp, fontWeight = FontWeight.Bold) }

        BasicTextField(
            value = value,
            onValueChange = { onChange(it.filter(Char::isDigit).take(10)) },
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            singleLine = true,
            textStyle = TextStyle(fontFamily = Mukta, fontSize = 34.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.04.em, color = Ds.Ink),
            cursorBrush = SolidColor(Ds.Primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            visualTransformation = PhoneTransformation,
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(placeholder, fontSize = 34.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.04.em, color = Ds.LineStrong)
                    }
                    inner()
                }
            },
        )
    }
}
