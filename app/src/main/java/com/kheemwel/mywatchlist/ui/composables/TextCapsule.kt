package com.kheemwel.mywatchlist.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextCapsule(text: String, onClick: () -> Unit = {}) {
    // Box(
    //     modifier = Modifier
    //         .background(Color(0xFFE3F2FD), shape = RoundedCornerShape(16.dp))
    //         .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp))
    //         .padding(8.dp),
    //     contentAlignment = Alignment.Center
    // ) {
    //     Text(
    //         text = text,
    //     )
    // }
    AssistChip(onClick = onClick, label = { Text(text) })
}