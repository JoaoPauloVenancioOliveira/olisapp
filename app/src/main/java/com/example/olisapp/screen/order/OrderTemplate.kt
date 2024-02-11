package com.example.olisapp.screen.order

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.olisapp.ui.theme.OlisappTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderTemplate(
    paddingValues: PaddingValues,
    viewModel: OrderViewModel
) {

    var name by remember { mutableStateOf(TextFieldValue()) }
    var productValue by remember { mutableDoubleStateOf(0.0) }
    var showDialog by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }
    var showItemDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Pix/Dinheiro") }
    val options = listOf("Pix/Dinheiro", "Crédito", "Débito")
    var resetScreen by remember { mutableStateOf(false) }

    val productsLoadingState by viewModel.productsLoadingState.collectAsStateWithLifecycle()

    when (productsLoadingState) {
        is ProductsLoadingState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        is ProductsLoadingState.Success -> {
            val products = (productsLoadingState as ProductsLoadingState.Success).products
            Scaffold {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Column {
                            Text(
                                text = "Cliente",
                                modifier = Modifier.padding(8.dp),
                                color = Color.Gray
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = name,
                                onValueChange = { name = it },
                                placeholder = { Text("Digite nome do cliente") },
                            )
                        }


                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(products) {
                                Products(
                                    productName = it.name,
                                    productValue = it.price.toString(),
                                    onPlusClick = { plus ->
                                        productValue += plus
                                    },
                                    onMinusClick = { minus ->
                                        if (productValue > 0.0) {
                                            productValue -= minus
                                        }
                                    },
                                    reset = resetScreen
                                )
                            }
                        }

                        DropDownMenu(
                            options = options,
                            onOptionSelected = { selectedOption = it },
                            selectedOption = selectedOption
                        )

                        Text(
                            text = "Valor total: R$ $productValue",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Button(
                            onClick = {
                                if (name.text.isEmpty()) {
                                    showNameDialog = true
                                } else if (productValue <= 0) {
                                    showItemDialog = true
                                } else {
                                    showDialog = true
                                }
                            },
                        ) {
                            Text("Encerrar pedido")
                        }

                        if (showDialog) {
                            ConfirmAlertDialog(
                                showDialog = true,
                                onConfirm = {
                                    showDialog = false
                                    resetScreen = true
                                },
                                onCancel = { showDialog = false })
                        }

                        if (showNameDialog) {
                            NameValidationDialog(
                                showDialog = true,
                                onCancel = { showNameDialog = false }
                            )
                        }

                        if (showItemDialog) {
                            ItemAmountValidationDialog(
                                showDialog = true,
                                onCancel = { showItemDialog = false }
                            )
                        }

                        if (resetScreen) {
                            name = TextFieldValue("")
                            productValue = 0.0
                            selectedOption = "Pix/Dinheiro"

                            SuccessOrder(
                                showDialog = true,
                                onCancel = {
                                    showDialog = false
                                    resetScreen = false
                                }
                            )
                        }
                    }
                }
            }
        }
        is ProductsLoadingState.Error -> {
            val errorMessage = (productsLoadingState as ProductsLoadingState.Error).errorMessage
            Text(text = "Erro ao carregar produtos: $errorMessage")
        }
    }
}

@Composable
fun ConfirmAlertDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Pedido")
            },
            text = {
                Text("Deseja encerrar o pedido?")
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                }) {
                    Text("Sim")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onCancel()
                }) {
                    Text("Não")
                }
            }
        )
    }
}


@Composable
fun NameValidationDialog(
    onCancel: () -> Unit,
    showDialog: Boolean,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Text("Cliente é um campo obrigatório")
            },
            confirmButton = {
                Button(onClick = {
                    onCancel()
                }) {
                    Text("Ok")
                }
            }
        )
    }
}

@Composable
fun ItemAmountValidationDialog(
    onCancel: () -> Unit,
    showDialog: Boolean,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Text("A quantidade de itens deve ser maior que zero")
            },
            confirmButton = {
                Button(onClick = {
                    onCancel()
                }) {
                    Text("Ok")
                }
            }
        )
    }
}

@Composable
fun SuccessOrder(
    onCancel: () -> Unit,
    showDialog: Boolean,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Text("Pedido feito com sucesso")
            },
            confirmButton = {
                Button(onClick = {
                    onCancel()
                }) {
                    Text("Ok")
                }
            }
        )
    }
}


@Composable
fun DropDownMenu(
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    selectedOption: String
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionIndex by remember { mutableIntStateOf(options.indexOf(selectedOption)) }

    Column {
        Text(
            text = "Forma de Pagamento",
            modifier = Modifier.padding(8.dp),
            color = Color.Gray
        )
        Surface(
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color.Black),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = !expanded })
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterEnd)
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            onClick = {
                                onOptionSelected(option)
                                selectedOptionIndex = index
                                expanded = false
                            },
                            text = {
                                Text(text = option)
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(device = "spec:width=720px,height=1280px,dpi=320", showSystemUi = true)
@Composable
fun OrderTemplatePreview() {
    OlisappTheme {
        OrderTemplate(paddingValues = PaddingValues(), OrderViewModel())
    }
}







