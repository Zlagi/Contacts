package com.streamwide.haythemmejerbi.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.streamwide.haythemmejerbi.local.model.ContactEntity.Companion.CONTACTS_TABLE_NAME

@Entity(tableName = CONTACTS_TABLE_NAME)
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = CONTACTS_ID_COLUMN_NAME) val id: String,
    @ColumnInfo(name = CONTACTS_NAME_COLUMN_NAME) val name: String,
    @ColumnInfo(name = CONTACTS_NUMBERS_COLUMN_NAME) val numbers: List<String> = emptyList()
) {
    companion object {
        const val CONTACTS_TABLE_NAME = "contacts"
        const val CONTACTS_ID_COLUMN_NAME = "id"
        const val CONTACTS_NAME_COLUMN_NAME = "name"
        const val CONTACTS_NUMBERS_COLUMN_NAME = "numbers"
    }
}
