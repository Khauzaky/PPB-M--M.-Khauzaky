package com.example.etsppb.model

import java.util.UUID

enum class MembershipType(val displayName: String) {
    MAHASISWA("Mahasiswa"),
    DOSEN("Dosen"),
    TENAGA_PENDIDIK("Tenaga Pendidik"),
    UMUM("Umum")
}

data class Member(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val idNumber: String,
    val studyProgram: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val membershipType: MembershipType = MembershipType.MAHASISWA,
    val registrationDate: String = ""
)
