package com.kevin.statsdroid.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Lookup : Screen("lookup", "Tabel Distribusi")
    data object Hypothesis : Screen("hypothesis", "Uji Hipotesis")
    data object Clt : Screen("clt", "Demo CLT")
    data object Reference : Screen("reference", "Referensi Rumus")
    data object About : Screen("about", "Tentang Aplikasi")
}
