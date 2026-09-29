package com.example.kuis1

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kuis1.ui.theme.Kuis1Theme
import kotlinx.coroutines.delay

data class Mahasiswa(
    val nim: String,
    val nama: String,
    val prodi: String
)

val listProdi = listOf(
    "Informatika",
    "Sistem Informasi",
    "Teknik Komputer",
    "Rekayasa Perangkat Lunak",
    "Rekayasa Kecerdasan Artifisial"
)

enum class Screen {
    Splash,
    Home,
    AddStudent,
    EditStudent
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kuis1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ManagerApp()
                }
            }
        }
    }
}

@Composable
fun ManagerApp() {
    var currentScreen by remember { mutableStateOf(Screen.Splash) }
    var studentCounter by remember { mutableIntStateOf(2209001) }

    fun generateNIM(): String {
        val nextNim = studentCounter.toString()
        studentCounter++
        return nextNim
    }

    val mhslist = remember { mutableStateListOf<Mahasiswa>() }

    var selectMHS by remember { mutableStateOf<Mahasiswa?>(null) }
    var studentDelete by remember { mutableStateOf<Mahasiswa?>(null) }
    var showAboutDialog by remember { mutableStateOf(false) }

    when (currentScreen) {
        Screen.Splash -> {
            SplashScreenView(onTimeout = { currentScreen = Screen.Home })
        }

        Screen.Home -> {
            HomeScreen(
                students = mhslist,
                onAddClick = { currentScreen = Screen.AddStudent },
                onEditClick = { mhs ->
                    selectMHS = mhs
                    currentScreen = Screen.EditStudent
                },
                onDeleteClick = { mhs ->
                    studentDelete = mhs
                },
                onAboutClick = {
                    showAboutDialog = true
                },
                onRefreshClick = {
                }
            )

            studentDelete?.let { mhs ->
                DeleteConfirmDialog(
                    nama = mhs.nama,
                    onConfirm = {
                        mhslist.remove(mhs)
                        studentDelete = null
                    },
                    onDismiss = { studentDelete = null }
                )
            }

            if (showAboutDialog) {
                AboutDialog(onDismiss = { showAboutDialog = false })
            }
        }

        Screen.AddStudent -> {
            val autoGenNIM = remember { generateNIM() }

            AddEditStudentScreen(
                title = "Tambah Mahasiswa",
                mahasiswa = null,
                defaultNIM = autoGenNIM,
                onSave = { nim, nama, prodi ->
                    mhslist.add(Mahasiswa(nim = nim, nama = nama, prodi = prodi))
                    currentScreen = Screen.Home
                },
                onCancel = {
                    if (studentCounter > 2209001) {
                        studentCounter--
                    }
                    currentScreen = Screen.Home
                }
            )
        }

        Screen.EditStudent -> {
            AddEditStudentScreen(
                title = "Edit Mahasiswa",
                mahasiswa = selectMHS,
                defaultNIM = selectMHS?.nim ?: "",
                onSave = { nim, nama, prodi ->
                    selectMHS?.let { oldMHS ->
                        val index = mhslist.indexOfFirst { it.nim == oldMHS.nim }
                        if (index != -1) {
                            mhslist[index] = Mahasiswa(
                                nim = nim,
                                nama = nama,
                                prodi = prodi
                            )
                        }
                    }
                    selectMHS = null
                    currentScreen = Screen.Home
                },
                onCancel = {
                    selectMHS = null
                    currentScreen = Screen.Home
                }
            )
        }
    }
}

@Composable
fun SplashScreenView(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.topi_toga),
                    contentDescription = "Logo Student Manager",
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Student Manager",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kelola data mahasiswa dengan mudah",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tampilan awal aplikasi",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    students: SnapshotStateList<Mahasiswa>,
    onAddClick: () -> Unit,
    onEditClick: (Mahasiswa) -> Unit,
    onDeleteClick: (Mahasiswa) -> Unit,
    onAboutClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val filteredStudents = remember(students.toList(), searchQuery) {
        if (searchQuery.isBlank()) {
            students
        } else {
            students.filter { mhs ->
                mhs.nama.contains(searchQuery, ignoreCase = true) ||
                        mhs.nim.contains(searchQuery, ignoreCase = true) ||
                        mhs.prodi.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Student Manager",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu Options"
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Reload / Refresh") },
                            leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                            onClick = {
                                menuExpanded = false
                                searchQuery = ""
                                onRefreshClick()
                                Toast.makeText(context, "Data diperbarui", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Tentang Aplikasi") },
                            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                            onClick = {
                                menuExpanded = false
                                onAboutClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Keluar") },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                            onClick = {
                                menuExpanded = false
                                (context as? Activity)?.finish()
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Mahasiswa"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari mahasiswa...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Text(
                text = "Jumlah mahasiswa: ${filteredStudents.size}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (filteredStudents.isEmpty()) {
                EmptyStateView(isSearching = searchQuery.isNotEmpty())
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredStudents, key = { it.nim }) { mhs ->
                        MahasiswaCard(
                            mahasiswa = mhs,
                            onEdit = { onEditClick(mhs) },
                            onDelete = { onDeleteClick(mhs) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MahasiswaCard(
    mahasiswa: Mahasiswa,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mahasiswa.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "NIM: ${mahasiswa.nim}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = mahasiswa.prodi,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Mahasiswa",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Mahasiswa",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(isSearching: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.topi_toga),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isSearching) "Mahasiswa tidak ditemukan" else "Belum ada data mahasiswa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isSearching)
                    "Tidak ada data mahasiswa yang sesuai dengan kata kunci pencarian."
                else
                    "Tekan tombol + untuk menambahkan mahasiswa pertama.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStudentScreen(
    title: String,
    mahasiswa: Mahasiswa?,
    defaultNIM: String,
    onSave: (nim: String, nama: String, prodi: String) -> Unit,
    onCancel: () -> Unit
) {
    var nim by remember { mutableStateOf(mahasiswa?.nim ?: defaultNIM) }
    var nama by remember { mutableStateOf(mahasiswa?.nama ?: "") }
    var prodi by remember { mutableStateOf(mahasiswa?.prodi ?: "") }

    var expandedProdi by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text(
                        text = "NIM",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = nim,
                        onValueChange = { nim = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan NIM") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Column {
                    Text(
                        text = "Nama",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = nama,
                        onValueChange = {
                            nama = it
                            if (errorMessage != null) errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan nama mahasiswa") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Column {
                    Text(
                        text = "Program Studi",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedProdi,
                        onExpandedChange = { expandedProdi = !expandedProdi }
                    ) {
                        OutlinedTextField(
                            value = prodi.ifEmpty { "Pilih program studi" },
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProdi)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedTextColor = if (prodi.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedProdi,
                            onDismissRequest = { expandedProdi = false }
                        ) {
                            listProdi.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        prodi = option
                                        expandedProdi = false
                                        if (errorMessage != null) errorMessage = null
                                    }
                                )
                            }
                        }
                    }
                }

                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Batal")
                }

                Button(
                    onClick = {
                        when {
                            nim.isBlank() -> errorMessage = "NIM tidak boleh kosong"
                            nama.isBlank() -> errorMessage = "Nama mahasiswa tidak boleh kosong"
                            prodi.isBlank() -> errorMessage = "Pilih program studi"
                            else -> onSave(nim.trim(), nama.trim(), prodi.trim())
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Simpan")
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmDialog(
    nama: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = "Hapus Mahasiswa?",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Apakah anda yakin ingin menghapus data mahasiswa \"$nama\"?",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "Tentang Aplikasi",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Student Manager",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Versi 1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Aplikasi sederhana untuk mengelola data mahasiswa secara efektif dan mudah.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dibuat dengan Kotlin, Jetpack Compose, & Material 3.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Tutup")
            }
        }
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Student Manager App Interactive Preview"
)
@Composable
fun StudentManagerAppPreview() {
    Kuis1Theme {
        ManagerApp()
    }
}


