package com.streamwide.haythemmejerbi.domain.repository

import com.streamwide.haythemmejerbi.data.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    suspend fun fetchContacts(): Flow<List<Contact>>
    fun searchBy(query: String): Flow<List<Contact>>
    fun observeContactsUpdates(): Flow<Boolean>
}
