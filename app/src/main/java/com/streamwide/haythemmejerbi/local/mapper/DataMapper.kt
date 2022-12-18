package com.streamwide.haythemmejerbi.local.mapper

import com.streamwide.haythemmejerbi.data.model.Contact
import com.streamwide.haythemmejerbi.local.model.ContactEntity

fun List<ContactEntity>.toContacts() = map {
    Contact(id = it.id, name = it.name, numbers = it.numbers)
}

fun List<Contact>.toEntities() = map {
    ContactEntity(id = it.id, name = it.name, numbers = it.numbers)
}