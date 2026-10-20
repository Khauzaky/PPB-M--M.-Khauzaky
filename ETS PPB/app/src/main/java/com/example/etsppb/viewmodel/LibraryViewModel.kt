package com.example.etsppb.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.etsppb.model.Member
import com.example.etsppb.model.MembershipType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("library_reg_prefs", Context.MODE_PRIVATE)

    private val _formState = MutableStateFlow(RegistrationFormState())
    val formState: StateFlow<RegistrationFormState> = _formState.asStateFlow()

    private val _formErrorState = MutableStateFlow(RegistrationFormErrorState())
    val formErrorState: StateFlow<RegistrationFormErrorState> = _formErrorState.asStateFlow()

    private val _members = MutableStateFlow<List<Member>>(loadMembersFromStorage())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow<MembershipType?>(null)
    val selectedFilter: StateFlow<MembershipType?> = _selectedFilter.asStateFlow()

    val filteredMembers: StateFlow<List<Member>> = combine(
        _members,
        _searchQuery,
        _selectedFilter
    ) { memberList, query, filter ->
        memberList.filter { member ->
            val matchesQuery = query.isBlank() ||
                    member.fullName.contains(query, ignoreCase = true) ||
                    member.idNumber.contains(query, ignoreCase = true) ||
                    member.studyProgram.contains(query, ignoreCase = true) ||
                    member.email.contains(query, ignoreCase = true)

            val matchesFilter = filter == null || member.membershipType == filter

            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _members.value
    )

    private val _selectedMemberForDetail = MutableStateFlow<Member?>(null)
    val selectedMemberForDetail: StateFlow<Member?> = _selectedMemberForDetail.asStateFlow()

    private val _selectedMemberForEdit = MutableStateFlow<Member?>(null)
    val selectedMemberForEdit: StateFlow<Member?> = _selectedMemberForEdit.asStateFlow()

    private val _selectedMemberForDelete = MutableStateFlow<Member?>(null)
    val selectedMemberForDelete: StateFlow<Member?> = _selectedMemberForDelete.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun onFullNameChange(newValue: String) {
        _formState.value = _formState.value.copy(fullName = newValue)
        if (_formErrorState.value.fullNameError != null) {
            _formErrorState.value = _formErrorState.value.copy(fullNameError = null)
        }
    }

    fun onIdNumberChange(newValue: String) {
        _formState.value = _formState.value.copy(idNumber = newValue)
        if (_formErrorState.value.idNumberError != null) {
            _formErrorState.value = _formErrorState.value.copy(idNumberError = null)
        }
    }

    fun onStudyProgramChange(newValue: String) {
        _formState.value = _formState.value.copy(studyProgram = newValue)
        if (_formErrorState.value.studyProgramError != null) {
            _formErrorState.value = _formErrorState.value.copy(studyProgramError = null)
        }
    }

    fun onEmailChange(newValue: String) {
        _formState.value = _formState.value.copy(email = newValue)
        if (_formErrorState.value.emailError != null) {
            _formErrorState.value = _formErrorState.value.copy(emailError = null)
        }
    }

    fun onPhoneNumberChange(newValue: String) {
        _formState.value = _formState.value.copy(phoneNumber = newValue)
        if (_formErrorState.value.phoneNumberError != null) {
            _formErrorState.value = _formErrorState.value.copy(phoneNumberError = null)
        }
    }

    fun onAddressChange(newValue: String) {
        _formState.value = _formState.value.copy(address = newValue)
        if (_formErrorState.value.addressError != null) {
            _formErrorState.value = _formErrorState.value.copy(addressError = null)
        }
    }

    fun onMembershipTypeChange(newValue: MembershipType) {
        _formState.value = _formState.value.copy(membershipType = newValue)
    }

    fun validateForm(): Boolean {
        val current = _formState.value
        var isValid = true

        var nameErr: String? = null
        var idErr: String? = null
        var prodiErr: String? = null
        var emailErr: String? = null
        var phoneErr: String? = null
        var addressErr: String? = null

        if (current.fullName.isBlank()) {
            nameErr = "Nama Lengkap wajib diisi!"
            isValid = false
        } else if (current.fullName.trim().length < 3) {
            nameErr = "Nama Lengkap minimal 3 karakter!"
            isValid = false
        }

        if (current.idNumber.isBlank()) {
            idErr = "NIM/NIP wajib diisi!"
            isValid = false
        } else if (!current.idNumber.all { it.isDigit() }) {
            idErr = "NIM/NIP hanya boleh berisi angka!"
            isValid = false
        } else if (current.idNumber.trim().length < 8) {
            idErr = "NIM/NIP minimal 8 digit angka!"
            isValid = false
        }

        if (current.studyProgram.isBlank()) {
            prodiErr = "Program Studi/Unit Kerja wajib diisi!"
            isValid = false
        }

        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        if (current.email.isBlank()) {
            emailErr = "Email wajib diisi!"
            isValid = false
        } else if (!emailRegex.matches(current.email.trim())) {
            emailErr = "Format email tidak valid! Contoh: nama@domain.com"
            isValid = false
        }

        if (current.phoneNumber.isBlank()) {
            phoneErr = "Nomor Telepon wajib diisi!"
            isValid = false
        } else if (!current.phoneNumber.all { it.isDigit() }) {
            phoneErr = "Nomor Telepon hanya boleh berisi angka!"
            isValid = false
        } else if (current.phoneNumber.trim().length < 10) {
            phoneErr = "Nomor Telepon minimal 10 digit angka!"
            isValid = false
        }

        if (current.address.isBlank()) {
            addressErr = "Alamat Domisili wajib diisi!"
            isValid = false
        }

        _formErrorState.value = RegistrationFormErrorState(
            fullNameError = nameErr,
            idNumberError = idErr,
            studyProgramError = prodiErr,
            emailError = emailErr,
            phoneNumberError = phoneErr,
            addressError = addressErr
        )

        return isValid
    }

    fun registerMember(): Boolean {
        if (!validateForm()) {
            _snackbarMessage.value = "Gagal mendaftar. Silakan lengkapi semua bidang bertanda merah!"
            return false
        }

        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val newMember = Member(
            fullName = _formState.value.fullName.trim(),
            idNumber = _formState.value.idNumber.trim(),
            studyProgram = _formState.value.studyProgram.trim(),
            email = _formState.value.email.trim(),
            phoneNumber = _formState.value.phoneNumber.trim(),
            address = _formState.value.address.trim(),
            membershipType = _formState.value.membershipType,
            registrationDate = currentDate
        )

        val updatedList = listOf(newMember) + _members.value
        _members.value = updatedList
        saveMembersToStorage(updatedList)

        resetForm()
        _snackbarMessage.value = "Berhasil mendaftarkan ${newMember.fullName} sebagai anggota perpustakaan!"
        return true
    }

    fun updateMember(updatedMember: Member) {
        val updatedList = _members.value.map { member ->
            if (member.id == updatedMember.id) updatedMember else member
        }
        _members.value = updatedList
        saveMembersToStorage(updatedList)

        _selectedMemberForEdit.value = null
        _snackbarMessage.value = "Data anggota ${updatedMember.fullName} berhasil diperbarui!"
    }

    fun deleteMember(memberId: String) {
        val memberToDelete = _members.value.find { it.id == memberId }
        val updatedList = _members.value.filterNot { it.id == memberId }
        _members.value = updatedList
        saveMembersToStorage(updatedList)

        _selectedMemberForDelete.value = null
        if (memberToDelete != null) {
            _snackbarMessage.value = "Anggota ${memberToDelete.fullName} telah dihapus."
        }
    }

    fun resetForm() {
        _formState.value = RegistrationFormState()
        _formErrorState.value = RegistrationFormErrorState()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onFilterChange(type: MembershipType?) {
        _selectedFilter.value = if (_selectedFilter.value == type) null else type
    }

    fun showMemberDetail(member: Member?) {
        _selectedMemberForDetail.value = member
    }

    fun showMemberEditDialog(member: Member?) {
        _selectedMemberForEdit.value = member
    }

    fun showMemberDeleteConfirmation(member: Member?) {
        _selectedMemberForDelete.value = member
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    private fun saveMembersToStorage(memberList: List<Member>) {
        val jsonArray = JSONArray()
        for (member in memberList) {
            val jsonObject = JSONObject().apply {
                put("id", member.id)
                put("fullName", member.fullName)
                put("idNumber", member.idNumber)
                put("studyProgram", member.studyProgram)
                put("email", member.email)
                put("phoneNumber", member.phoneNumber)
                put("address", member.address)
                put("membershipType", member.membershipType.name)
                put("registrationDate", member.registrationDate)
            }
            jsonArray.put(jsonObject)
        }
        sharedPreferences.edit().putString("saved_members_json", jsonArray.toString()).apply()
    }

    private fun loadMembersFromStorage(): List<Member> {
        val jsonString = sharedPreferences.getString("saved_members_json", null) ?: return emptyList()
        val memberList = mutableListOf<Member>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val member = Member(
                    id = obj.optString("id"),
                    fullName = obj.optString("fullName"),
                    idNumber = obj.optString("idNumber"),
                    studyProgram = obj.optString("studyProgram"),
                    email = obj.optString("email"),
                    phoneNumber = obj.optString("phoneNumber"),
                    address = obj.optString("address"),
                    membershipType = try {
                        MembershipType.valueOf(obj.optString("membershipType", MembershipType.MAHASISWA.name))
                    } catch (_: Exception) {
                        MembershipType.MAHASISWA
                    },
                    registrationDate = obj.optString("registrationDate")
                )
                memberList.add(member)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return memberList
    }
}
