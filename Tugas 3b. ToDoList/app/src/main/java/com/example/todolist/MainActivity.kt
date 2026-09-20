package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.ui.theme.TodoViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}

@Composable
fun TodoApp(todoViewModel: TodoViewModel = viewModel()) {
    var textInput by remember { mutableStateOf("")}

    Column (modifier = Modifier.fillMaxSize().padding(16.dp)){

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            OutlinedTextField(
                value = textInput,
                onValueChange = {textInput = it},
                label = {Text("Tugas Baru")},
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                todoViewModel.addTodo(textInput)
                textInput = ""
            }) {
                Text("Tambah")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn() {
            items(todoViewModel.todoList) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isDone,
                        onCheckedChange = { todoViewModel.toggleTodo(item) }
                    )
                    Text(
                        text = item.title,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { todoViewModel.deleteTodo(item) }) {
                        Text("❌")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreviw(){
    TodoApp()
}