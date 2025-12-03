package com.example.smartschedule.core.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseProviderModule {

    @Provides
    @Singleton
    fun provideFirebaseFireStore() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

}