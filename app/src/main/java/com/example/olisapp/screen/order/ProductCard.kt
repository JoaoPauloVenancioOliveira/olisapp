package com.example.olisapp.screen.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductCard(
    productName: String?,
    productValue: String?,
    onPlusClick: (Double) -> Unit,
    onMinusClick: (Double) -> Unit,
    reset: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        var quantidade by remember { mutableIntStateOf(0) }

        if (reset) {
            quantidade = 0
        }

        Text(
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
            ),
            text = productName ?: "",
            modifier = Modifier
                .padding(10.dp)
                .padding(start = 10.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Text(
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Black

                ),
                text = "R$ $productValue",
                textAlign = TextAlign.Center,

                )

            Text(
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,

                    ),
                text = "Qtd: $quantidade",
                textAlign = TextAlign.Center,

                )

            Spacer(modifier = Modifier.width(40.dp))

            Row {

                Icon(
                    Icons.Outlined.RemoveCircle,
                    contentDescription = null,
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.clickable {
                        if (quantidade > 0) {
                            if (productValue != null) {
                                onMinusClick(productValue.toDouble())
                            }
                            quantidade--
                        }
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = null,
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.clickable {
                        if (productValue != null) {
                            onPlusClick(productValue.toDouble())
                        }
                        quantidade++
                    }
                )

            }
        }
    }
}