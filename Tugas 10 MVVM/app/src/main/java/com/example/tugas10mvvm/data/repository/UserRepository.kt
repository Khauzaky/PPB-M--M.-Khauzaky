package com.example.tugas10mvvm.data.repository

import com.example.tugas10mvvm.data.local.dao.UserDao
import com.example.tugas10mvvm.data.local.entity.User

interface UserRepository {
    suspend fun getUserByUsername(username: String): User?
    suspend fun registerUser(user: User)
}

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    override suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }
}
