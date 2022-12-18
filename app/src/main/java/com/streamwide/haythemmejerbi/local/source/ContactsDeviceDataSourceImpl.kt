package com.streamwide.haythemmejerbi.local.source

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.streamwide.haythemmejerbi.data.model.Contact
import com.streamwide.haythemmejerbi.data.source.ContactsDeviceDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactsDeviceDataSourceImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
) : ContactsDeviceDataSource {

    override fun requestContacts(): List<Contact> {
        val contacts = getPhoneContacts()
        val contactNumbers = getContactNumbers()
        return contacts.appendPhoneNumbers(contactNumbers = contactNumbers)
    }

    private fun getPhoneContacts(): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        val sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"

        val contactsCursor = context.contentResolver?.query(
            /* uri = */ ContactsContract.Contacts.CONTENT_URI,
            /* projection = */ projection,
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder = */ sortOrder
        )
        if (contactsCursor != null && contactsCursor.count > 0) {
            val idIndex = contactsCursor.getColumnIndex(/* p0 = */ ContactsContract.Contacts._ID)
            val nameIndex =
                contactsCursor.getColumnIndex(/* p0 = */ ContactsContract.Contacts.DISPLAY_NAME)
            while (contactsCursor.moveToNext()) {
                val id = contactsCursor.getString(/* p0 = */ idIndex)
                val name = contactsCursor.getString(/* p0 = */ nameIndex)
                if (name != null) {
                    contactsList.add(element = Contact(id = id, name = name))
                }
            }
            contactsCursor.close()
        }
        return contactsList
    }

    private fun getContactNumbers(): HashMap<String, ArrayList<String>> {
        val contactsNumberMap = hashMapOf<String, ArrayList<String>>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val phoneCursor: Cursor? = context.contentResolver.query(
            /* uri = */ ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            /* projection = */ projection,
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder = */ null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex =
                phoneCursor.getColumnIndex(/* p0 = */ ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex =
                phoneCursor.getColumnIndex(/* p0 = */ ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(/* p0 = */ contactIdIndex)
                val number: String = phoneCursor.getString(/* p0 = */ numberIndex)
                if (contactsNumberMap.containsKey(key = contactId)) {
                    contactsNumberMap[contactId]?.add(element = number.replace(" ", ""))
                } else {
                    contactsNumberMap[contactId] = arrayListOf(number.replace(" ", ""))
                }
            }
            phoneCursor.close()
        }
        return contactsNumberMap
    }

    private fun List<Contact>.appendPhoneNumbers(
        contactNumbers: HashMap<String, ArrayList<String>>,
    ) = map {

        val numbers = contactNumbers[it.id]

        it.copy(numbers = numbers ?: emptyList())
    }
}