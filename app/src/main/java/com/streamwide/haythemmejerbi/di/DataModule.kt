package com.streamwide.haythemmejerbi.di

import android.content.Context
import androidx.room.Room
import com.streamwide.haythemmejerbi.local.dao.ContactsDao
import com.streamwide.haythemmejerbi.local.database.ContactsDatabase
import com.streamwide.haythemmejerbi.local.database.ContactsDatabase.Companion.CONTACTS_DATABASE_NAME
import com.streamwide.haythemmejerbi.data.repository.ContactsRepositoryImpl
import com.streamwide.haythemmejerbi.local.source.ContactsDeviceDataSourceImpl
import com.streamwide.haythemmejerbi.local.source.ContactsLocalDataSourceImpl
import com.streamwide.haythemmejerbi.data.source.ContactsDeviceDataSource
import com.streamwide.haythemmejerbi.data.source.ContactsLocalDataSource
import com.streamwide.haythemmejerbi.domain.repository.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun provideContactsRepository(
        contactsRepository: ContactsRepositoryImpl,
    ): ContactsRepository

    @Binds
    abstract fun provideContactsDeviceDataSource(
        deviceDataSource: ContactsDeviceDataSourceImpl,
    ): ContactsDeviceDataSource

    @Binds
    abstract fun provideContactsLocalDataSource(
        contactsDataSource: ContactsLocalDataSourceImpl,
    ): ContactsLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideContactsDatabase(
            @ApplicationContext context: Context,
        ): ContactsDatabase = Room.databaseBuilder(
            context,
            ContactsDatabase::class.java,
            CONTACTS_DATABASE_NAME
        ).build()

        @Provides
        fun provideApplicationDao(
            contactsDatabase: ContactsDatabase,
        ): ContactsDao = contactsDatabase.applicationDao()
    }
}