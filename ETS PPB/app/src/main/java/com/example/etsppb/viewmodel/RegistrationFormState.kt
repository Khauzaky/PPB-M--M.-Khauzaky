package com.example.etsppb.viewmodel

import com.example.etsppb.model.MembershipType

data class RegistrationFormState(
    val fullName: String = "",
    val idNumber: String = "",
    val studyProgram: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val membershipType: MembershipType = MembershipType.MAHASISWA
)

data class RegistrationFormErrorState(
    val fullNameError: String? = null,
    val idNumberError: String? = null,
    val studyProgramError: String? = null,
    val emailError: String? = null,
    val phoneNumberError: String? = null,
    val addressError: String? = null
) {
    fun hasError(): Boolean {
        return fullNameError != null ||
                idNumberError != null ||
                studyProgramError != null ||
                emailError != null ||
                phoneNumberError != null ||
                addressError != null
    }
}
