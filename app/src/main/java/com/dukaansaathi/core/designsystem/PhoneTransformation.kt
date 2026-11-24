package com.dukaansaathi.core.designsystem

import com.dukaansaathi.ui.theme.Ds
import com.dukaansaathi.ui.theme.Mukta
import com.dukaansaathi.ui.theme.Yatra
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/** Shows 9876543210 as "98765 43210" while the stored value stays plain digits. */
object PhoneTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val shown = if (raw.length > 5) raw.take(5) + " " + raw.drop(5) else raw
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = if (offset <= 5) offset else offset + 1
            override fun transformedToOriginal(offset: Int) = if (offset <= 5) offset else offset - 1
        }
        return TransformedText(AnnotatedString(shown), mapping)
    }
}
