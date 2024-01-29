package com.example.olisapp.navigation

sealed interface NavigationDestination {
    val route: String
}

data object OrderScreen : NavigationDestination {
    override val route = "order-screen"
}

data object SummaryScreen : NavigationDestination {
    override val route = "summary-screen"
}

data object InvoicingScreen : NavigationDestination {
    override val route = "invoicing-screen"
}
