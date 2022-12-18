package com.streamwide.haythemmejerbi.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.streamwide.haythemmejerbi.local.model.ContactEntity
import com.streamwide.haythemmejerbi.local.model.ContactEntity.Companion.CONTACTS_NAME_COLUMN_NAME
import com.streamwide.haythemmejerbi.local.model.ContactEntity.Companion.CONTACTS_NUMBERS_COLUMN_NAME
import com.streamwide.haythemmejerbi.local.model.ContactEntity.Companion.CONTACTS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Query("SELECT * FROM $CONTACTS_TABLE_NAME")
    fun fetchContacts(): Flow<List<ContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContacts(contacts: List<ContactEntity>)

    @Query("SELECT * FROM $CONTACTS_TABLE_NAME " +
            "WHERE $CONTACTS_NAME_COLUMN_NAME LIKE :query OR $CONTACTS_NUMBERS_COLUMN_NAME LIKE :query")
    fun searchBy(query: String): Flow<List<ContactEntity>>
}
