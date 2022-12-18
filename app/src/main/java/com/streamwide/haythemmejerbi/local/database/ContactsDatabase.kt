package com.streamwide.haythemmejerbi.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.streamwide.haythemmejerbi.local.converter.DataConverter
import com.streamwide.haythemmejerbi.local.dao.ContactsDao
import com.streamwide.haythemmejerbi.local.model.ContactEntity

@Database(
    entities = [
        ContactEntity::class
    ],
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun applicationDao(): ContactsDao

    companion object {
        const val CONTACTS_DATABASE_NAME = "ContactsDatabase"
    }
}