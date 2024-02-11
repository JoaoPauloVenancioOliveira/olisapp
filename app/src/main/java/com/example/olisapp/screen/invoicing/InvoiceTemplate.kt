import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.olisapp.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InvoiceTemplate() {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        Tabs(
            titles = listOf("Cadastrar produto", "Faturamento"),
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { index -> selectedTabIndex = index }
        )

        TabContent(selectedTabIndex)
    }
}

@Composable
fun Tabs(
    titles: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        modifier = Modifier.padding(10.dp),
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Color.Transparent,
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
            )
        },
        divider = {}
    ) {
        titles.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            val backgroundColor = if (isSelected) Color(0xFF06628d) else Color(0xFFe5e5e5)
            val contentColor = if (isSelected) Color.White else Color.Gray

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .padding(5.dp)
                    .background(backgroundColor, RoundedCornerShape(16.dp))
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = title,
                    color = contentColor,
                    style = TextStyle(fontWeight = if (isSelected) FontWeight.Bold else null)
                )
            }
        }
    }
}


@Composable
fun TabContent(selectedTabIndex: Int) {
    when (selectedTabIndex) {
        0 -> CreateProductScreen()
        1 -> BillingScreen()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateProductScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var productName by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        val context = LocalContext.current

        val formattedPrice = remember(price) {
            formatCurrencyAmount(price)
        }

        val formattedName = remember(productName) {
            productNameToRegister(productName)
        }


        Scaffold {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nome do Produto") },
                    placeholder = { Text("Digite o nome do produto") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = formattedPrice,
                    onValueChange = { newText ->
                        val rawValue = newText.filter { it.isDigit() }
                        price = rawValue
                    },
                    label = { Text("Preço") },
                    placeholder = { Text("Digite o preço") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (productName.isEmpty()) {
                            Toast.makeText(context, "Digite o nome do produto", Toast.LENGTH_SHORT).show()
                        } else if (price.isEmpty()) {
                            Toast.makeText(context, "Digite o preço do produto", Toast.LENGTH_SHORT).show()
                        } else {
                            val database = FirebaseDatabase.getInstance()
                            val productsRef  = database.getReference("products")

                            productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        val lastOrderId = dataSnapshot.children.last().key

                                        val product = Product(
                                            id = lastOrderId?.toLong()?.plus(1),
                                            name = formattedName,
                                            price = convertPriceStringToNumber(formattedPrice),
                                        )

                                        productsRef.child(product.id.toString()).setValue(product)

                                    } else {
                                        println("Nenhum pedido encontrado.")
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    println("Erro ao ler o banco de dados: ${databaseError.message}")
                                }
                            })

                            Toast.makeText(context, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        price = ""
                        productName = ""
                    }
                ) {
                    Text("Cadastrar")
                }
            }
        }
    }
}

fun convertPriceStringToNumber(valor: String): Double {
    val valorSemPrefixo = valor.replace("R$", "").trim()
    val valorFormatado = valorSemPrefixo.replace(".", "").replace(",", ".")
    return valorFormatado.toDouble()
}

fun formatCurrencyAmount(amount: String): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val value = if (amount.isEmpty()) 0.0 else amount.toDouble() / 100
    return numberFormat.format(value)
}

fun productNameToRegister(name: String): String {
    return name
}

@Composable
fun BillingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //TODO()
    }
}