package com.streamwide.haythemmejerbi.data.source

import com.streamwide.haythemmejerbi.data.model.Contact

interface ContactsDeviceDataSource {
    fun requestContacts(): List<Contact>
}