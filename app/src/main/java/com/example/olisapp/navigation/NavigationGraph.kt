package com.example.olisapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.olisapp.screen.invoicing.InvoiceScreen
import com.example.olisapp.screen.order.OrderScreen
import com.example.olisapp.screen.summary.SummaryScreen

fun NavGraphBuilder.navigation(paddingValues: PaddingValues) {
    composable(route = OrderScreen.route) {
        OrderScreen(paddingValues)
    }

    composable(route = SummaryScreen.route) {
        SummaryScreen()
    }

    composable(route = InvoicingScreen.route) {
        InvoiceScreen()
    }
}