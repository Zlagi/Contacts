package com.streamwide.haythemmejerbi.data.model

data class Contact(
    val id: String,
    val name: String,
    val numbers: List<String> = emptyList(),
)
