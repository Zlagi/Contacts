package com.streamwide.haythemmejerbi.data.repository

import android.content.Context
import android.database.ContentObserver
import android.provider.ContactsContract
import com.streamwide.haythemmejerbi.data.model.Contact
import com.streamwide.haythemmejerbi.data.source.ContactsDeviceDataSource
import com.streamwide.haythemmejerbi.data.source.ContactsLocalDataSource
import com.streamwide.haythemmejerbi.domain.repository.ContactsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ContactsRepositoryImpl
@Inject
constructor(
    private val contactsDeviceDataSource: ContactsDeviceDataSource,
    private val contactsLocalDataSource: ContactsLocalDataSource,
    @ApplicationContext val context: Context,
) : ContactsRepository {

    override suspend fun fetchContacts(): Flow<List<Contact>> {
        contactsLocalDataSource.saveContacts(contacts = contactsDeviceDataSource.requestContacts())
        return contactsLocalDataSource.fetchContacts()
    }

    override fun searchBy(query: String): Flow<List<Contact>> {
        return contactsLocalDataSource.searchBy(query = query)
    }

    override fun observeChanges(): Flow<Boolean> = callbackFlow {
        val contentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                trySend(selfChange)
            }
        }
        context.contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contentObserver)
        awaitClose()
    }
}
