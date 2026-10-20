package com.example.etsppb.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.etsppb.ui.screens.MainScreen
import com.example.etsppb.ui.screens.MemberListScreen
import com.example.etsppb.ui.screens.RegistrationScreen
import com.example.etsppb.viewmodel.LibraryViewModel

enum class AppScreenTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Beranda", Icons.Filled.Home, Icons.Outlined.Home),
    REGISTER("Pendaftaran", Icons.Filled.PersonAdd, Icons.Outlined.PersonAdd),
    MEMBER_LIST("Daftar Anggota", Icons.Filled.Badge, Icons.Outlined.Badge)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer(
    viewModel: LibraryViewModel
) {
    var selectedTabOrdinal by remember { mutableIntStateOf(AppScreenTab.HOME.ordinal) }
    val selectedTab = AppScreenTab.entries[selectedTabOrdinal]

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            AppScreenTab.HOME -> "LibraryReg - Beranda"
                            AppScreenTab.REGISTER -> "Pendaftaran Anggota"
                            AppScreenTab.MEMBER_LIST -> "Daftar & Kelola Anggota"
                        },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Logo Aplikasi",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                AppScreenTab.entries.forEach { tab ->
                    val isSelected = tab.ordinal == selectedTabOrdinal
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTabOrdinal = tab.ordinal },
                        label = { Text(tab.title) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppScreenTab.HOME -> {
                    MainScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = {
                            selectedTabOrdinal = AppScreenTab.REGISTER.ordinal
                        },
                        onNavigateToList = {
                            selectedTabOrdinal = AppScreenTab.MEMBER_LIST.ordinal
                        },
                        onNavigateToSearch = {
                            selectedTabOrdinal = AppScreenTab.MEMBER_LIST.ordinal
                        },
                        onNavigateToManage = {
                            selectedTabOrdinal = AppScreenTab.MEMBER_LIST.ordinal
                        }
                    )
                }

                AppScreenTab.REGISTER -> {
                    RegistrationScreen(
                        viewModel = viewModel,
                        onSuccessRegister = {
                            selectedTabOrdinal = AppScreenTab.MEMBER_LIST.ordinal
                        }
                    )
                }

                AppScreenTab.MEMBER_LIST -> {
                    MemberListScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
