package com.example.tugas4bbmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tugas4bbmicalculator.ui.theme.Tugas4bBMICalculatorTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas4bBMICalculatorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ){ innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        BMIScreen()
                    }
                }
            }
        }
    }
}

fun HitungBMI(berat: Float, tinggi: Float): Float{
    if (tinggi <= 0f || berat <= 0f){
        return 0f
    }
    val tinggiM = tinggi / 100f
    return berat / (tinggiM * tinggiM)
}

fun KategoriBMI(bmi: Float): String{
    return when {
        bmi <= 0f -> ""
        bmi < 18.5f -> "Kurus"
        bmi < 24.9f -> "Normal"
        bmi < 29.9f -> "Gemuk"
        bmi < 34.9f -> "Obesitas"
        else -> "Obesitas Extreme"
    }
}

@Composable
fun BMIScreen (modifier: Modifier = Modifier){
    var berat by remember { mutableStateOf("") }
    var tinggi by remember { mutableStateOf("") }
    var hasil by remember { mutableFloatStateOf(0f) }
    var kategori by remember {mutableStateOf("")}

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA), // Light Teal
            Color(0xFFEDE7F6), // Light Purple
            Color(0xFFFCE4EC)  // Light Pink
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Kalkulator BMI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = berat,
            onValueChange = {berat = it},
            label = {Text("Berat Badan (Kg)")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.7f), shape = MaterialTheme.shapes.small)
        )

        OutlinedTextField(
            value = tinggi,
            onValueChange = { tinggi = it },
            label = { Text("Tinggi Badan (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.7f), shape = MaterialTheme.shapes.small)
        )

        Button (
            onClick = {
                val beratF = berat.toFloatOrNull() ?: 0f
                val tinggiF = tinggi.toFloatOrNull() ?: 0f

                hasil = HitungBMI(beratF, tinggiF)
                kategori = KategoriBMI(hasil)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Star, contentDescription = null)
            Text(" Hitung BMI")
        }

        OutlinedButton(
            onClick = {
                berat = ""
                tinggi = ""
                hasil = 0f
                kategori = ""
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(Icons.Default.Refresh, contentDescription = null)
            Text (" Reset")
        }

        if (hasil > 0f){
            val warna = when (kategori){
                "Kurus" -> Color(0xFF2196F3)
                "Normal" -> Color(0xFF4CAF50)
                "Gemuk" -> Color(0xFFFF9800)
                "Obesitas" -> Color(0xFFE91E63)
                else -> Color(0xFFF44336)
            }

            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = warna.copy(alpha = 0.12f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ){
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hasil BMI anda",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "%.1f".format(hasil),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = warna
                    )
                    Text(
                        text = kategori,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = warna
                    )
                }
            }
        }
    }
}

@Preview ( showSystemUi = true)
@Composable
fun BMIPreview(){
    Tugas4bBMICalculatorTheme() {
        BMIScreen()
    }
}