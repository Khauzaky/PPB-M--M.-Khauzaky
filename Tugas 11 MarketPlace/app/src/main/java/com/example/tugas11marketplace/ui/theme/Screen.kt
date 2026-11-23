package com.example.tugas11marketplace.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.tugas11marketplace.ui.*
import com.example.tugas11marketplace.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf("home") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (currentScreen == "add") "Tambah Produk" else "MarketSiswa",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    if (currentScreen == "add") {
                        IconButton(onClick = { currentScreen = "home" }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == "home",
                    onClick = { currentScreen = "home" },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentScreen == "profile",
                    onClick = { currentScreen = "profile" },
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
            }
        },
        floatingActionButton = {
            if (currentScreen == "home") {
                ExtendedFloatingActionButton(
                    onClick = { currentScreen = "add" },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Jual")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "home" -> HomeScreen(products = viewModel.productList)
                "add" -> AddProductScreen(
                    onAddClick = { name, price, desc, onComplete ->
                        viewModel.addProduct(name, price, desc) {
                            onComplete()
                            scope.launch {
                                currentScreen = "home"
                                snackbarHostState.showSnackbar("Produk berhasil ditambahkan!")
                            }
                        }
                    }
                )
                "profile" -> ProfileScreen()
            }
        }
    }
}

@Composable
fun MarketplaceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6)
        ),
        content = content
    )
}
