package com.example.tugas10mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.tugas10mvvm.data.local.database.AppDatabase
import com.example.tugas10mvvm.data.repository.UserRepositoryImpl
import com.example.tugas10mvvm.ui.login.LoginScreen
import com.example.tugas10mvvm.ui.login.LoginViewModel
import com.example.tugas10mvvm.ui.login.LoginViewModelFactory
import com.example.tugas10mvvm.ui.theme.Tugas10MVVMTheme

class MainActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = UserRepositoryImpl(database.userDao())
        LoginViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas10MVVMTheme {
                LoginScreen(viewModel = viewModel)
            }
        }
    }
}

