package com.example.etsppb.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.etsppb.model.MembershipType
import com.example.etsppb.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RegistrationScreen(
    viewModel: LibraryViewModel,
    onSuccessRegister: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    val errorState by viewModel.formErrorState.collectAsState()

    val sampleStudyPrograms = remember {
        listOf(
            "Teknik Informatika",
            "Sistem Informasi",
            "Teknik Komputer",
            "Data Science",
            "Biro Administrasi / Unit Kerja",
            "Umum / Luar Kampus"
        )
    }
    var isProdiDropdownExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HowToReg,
                            contentDescription = "Pendaftaran Icon",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Form Pendaftaran Anggota",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Lengkapi data diri calon anggota di bawah ini secara akurat.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = formState.fullName,
                        onValueChange = { viewModel.onFullNameChange(it) },
                        label = { Text("Nama Lengkap *") },
                        placeholder = { Text("Contoh: Ahmad Rizky Pratama") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        isError = errorState.fullNameError != null,
                        supportingText = {
                            errorState.fullNameError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    OutlinedTextField(
                        value = formState.idNumber,
                        onValueChange = { viewModel.onIdNumberChange(it) },
                        label = { Text("NIM / NIP / Nomor Induk *") },
                        placeholder = { Text("Contoh: 2208101001") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Badge, contentDescription = null)
                        },
                        isError = errorState.idNumberError != null,
                        supportingText = {
                            errorState.idNumberError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            } ?: Text("Min. 8 digit angka tanpa spasi", fontSize = 11.sp)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = isProdiDropdownExpanded,
                        onExpandedChange = { isProdiDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = formState.studyProgram,
                            onValueChange = { viewModel.onStudyProgramChange(it) },
                            label = { Text("Program Studi / Unit Kerja *") },
                            placeholder = { Text("Pilih atau ketik nama prodi...") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.School, contentDescription = null)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isProdiDropdownExpanded)
                            },
                            isError = errorState.studyProgramError != null,
                            supportingText = {
                                errorState.studyProgramError?.let {
                                    Text(text = it, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = isProdiDropdownExpanded,
                            onDismissRequest = { isProdiDropdownExpanded = false }
                        ) {
                            sampleStudyPrograms.forEach { prodi ->
                                DropdownMenuItem(
                                    text = { Text(prodi) },
                                    onClick = {
                                        viewModel.onStudyProgramChange(prodi)
                                        isProdiDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Column {
                        Text(
                            text = "Jenis Keanggotaan *",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MembershipType.entries.forEach { type ->
                                val isSelected = formState.membershipType == type
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onMembershipTypeChange(type) },
                                    label = { Text(type.displayName) },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = formState.email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        label = { Text("Alamat Email *") },
                        placeholder = { Text("nama@domain.com") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        },
                        isError = errorState.emailError != null,
                        supportingText = {
                            errorState.emailError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = formState.phoneNumber,
                        onValueChange = { viewModel.onPhoneNumberChange(it) },
                        label = { Text("Nomor Telepon / WhatsApp *") },
                        placeholder = { Text("081234567890") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                        },
                        isError = errorState.phoneNumberError != null,
                        supportingText = {
                            errorState.phoneNumberError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            } ?: Text("Min. 10 digit angka", fontSize = 11.sp)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = formState.address,
                        onValueChange = { viewModel.onAddressChange(it) },
                        label = { Text("Alamat Domisili Lengkap *") },
                        placeholder = { Text("Jl. Mawar No. 10, Surabaya") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Home, contentDescription = null)
                        },
                        isError = errorState.addressError != null,
                        supportingText = {
                            errorState.addressError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        minLines = 2,
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.resetForm() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Bersihkan Form",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text("Reset")
                        }

                        Button(
                            onClick = {
                                val isSuccess = viewModel.registerMember()
                                if (isSuccess) {
                                    onSuccessRegister()
                                }
                            },
                            modifier = Modifier.weight(2f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.HowToReg,
                                contentDescription = "Daftar",
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text("Daftar Sekarang", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
