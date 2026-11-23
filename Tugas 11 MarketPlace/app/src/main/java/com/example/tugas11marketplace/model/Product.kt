package com.example.tugas11marketplace.model

import java.util.concurrent.atomic.AtomicLong

private val idGenerator = AtomicLong(System.currentTimeMillis())

data class Product(
    val id: Long = idGenerator.getAndIncrement(),
    val name: String,
    val price: String,
    val description: String
)
