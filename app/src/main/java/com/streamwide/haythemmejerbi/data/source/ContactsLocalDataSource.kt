package com.streamwide.haythemmejerbi.data.source

import com.streamwide.haythemmejerbi.data.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsLocalDataSource {
    fun fetchContacts(): Flow<List<Contact>>
    suspend fun saveContacts(contacts: List<Contact>)
    fun searchBy(query: String): Flow<List<Contact>>
}

