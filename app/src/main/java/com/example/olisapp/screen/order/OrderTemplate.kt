package com.example.olisapp.screen.order

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.olisapp.Product
import com.example.olisapp.R
import com.example.olisapp.ui.theme.OlisappTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderTemplate() {

    var name by remember { mutableStateOf(TextFieldValue()) }
    val productList = listOf(
        Product("Batata Pequena", "15.00"),
        Product("Batata Grande", "22.00"),
        Product("Batata + Calabresa", "25.00"),
        Product("Batata + Bacon", "25.00"),
        Product("Batata com frango", "30.00"),
        Product("Batata + frango + calabresa + bacon", "35.00"),

        )
    var productvalue by remember { mutableDoubleStateOf(0.0) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPaymentIndex by remember { mutableStateOf(0) }
    val paymentOptions = listOf("Dinheiro/Pix", "Cartão Débito", "Cartão Crédito")


    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Digite nome do cliente") },
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(productList) {
                    Products(
                        productName = it.productName,
                        productValue = it.productValue,
                        onPlusClick = { plus ->
                            productvalue += plus
                        },
                        onMinusClick = { minus ->
                            if (productvalue > 0.0) {
                                productvalue -= minus
                            }
                        }
                    )
                }
            }

            Text(
                text = "Valor total: R$ $productvalue",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = { showDialog = true },
            ) {
                Text("Finalizar pedido")
            }

            if (showDialog) {
                RadioButtonDialog(
                    onDismiss = {showDialog = false}    )
            }
        }
    }
}

@Composable
fun RadioButtonDialog(onDismiss: () -> Unit) {
    var selectedOption by remember { mutableStateOf(Option.Option1) }
    val options = Option.entries.toTypedArray()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Escolha a forma de pagamento") },
        text = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                
            ) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { selectedOption = option }
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option  }
                        )
                        Text(
                            text = option.label,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Descartar")
            }
        }
    )
}

enum class Option(val label: String) {
    Option1("Dinheiro/Pix"),
    Option2("Cartão de crédito"),
    Option3("Cartão de débito")
}

@Composable
fun ItemCard() {
    Box(
        modifier = Modifier.padding(bottom = 10.dp)
    ) {

        Card(
            modifier = Modifier
                .size(width = 340.dp, height = 120.dp)
                .padding(start = 80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 15.dp
            )
        ) {
            Text(
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Black
                ),
                text = "Batata frita + bacon",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(top = 30.dp, start = 75.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 75.dp)
            ) {

                Text(
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                    ),
                    text = "R$ 4,00",
                    textAlign = TextAlign.Center,

                    )

                Spacer(modifier = Modifier.width(40.dp))

                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = null,
                    tint = Color(0xFFFFA500)

                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    Icons.Outlined.RemoveCircle,
                    contentDescription = null,
                    tint = Color(0xFFFFA500)
                )
            }


        }
        Image(
            painter = painterResource(id = R.drawable.fried_chicken),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(130.dp)
                .padding(bottom = 20.dp),
            alignment = Alignment.CenterStart
        )
    }
}

@Preview(device = "spec:width=720px,height=1280px,dpi=320", showSystemUi = true)
@Composable
fun OrderTemplatePreview() {
    OlisappTheme {
        OrderTemplate()
    }
}







