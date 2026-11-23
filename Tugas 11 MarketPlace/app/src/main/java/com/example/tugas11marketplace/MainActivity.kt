package com.example.tugas11marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tugas11marketplace.ui.theme.MainApp
import com.example.tugas11marketplace.ui.theme.MarketplaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketplaceTheme {
                MainApp()
            }
        }
    }
}
