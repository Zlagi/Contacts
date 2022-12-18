package com.streamwide.haythemmejerbi.local.source

import com.streamwide.haythemmejerbi.data.model.Contact
import com.streamwide.haythemmejerbi.data.source.ContactsLocalDataSource
import com.streamwide.haythemmejerbi.local.dao.ContactsDao
import com.streamwide.haythemmejerbi.local.mapper.toContacts
import com.streamwide.haythemmejerbi.local.mapper.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactsLocalDataSourceImpl
@Inject
constructor(
    private val contactsDao: ContactsDao,
) : ContactsLocalDataSource {

    override fun fetchContacts(): Flow<List<Contact>> {
        return contactsDao.fetchContacts()
            .map {
                it.toContacts()
            }
    }

    override suspend fun saveContacts(contacts: List<Contact>) {
        contactsDao.saveContacts(contacts = contacts.toEntities())
    }

    override fun searchBy(query: String): Flow<List<Contact>> {
        return contactsDao.searchBy(query = "%$query%")
            .map {
                it.toContacts()
            }
    }
}
