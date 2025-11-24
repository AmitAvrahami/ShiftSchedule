package com.example.smartschedule.core.di

import com.example.smartschedule.feature.smartSchedule.data.repository.FakeScheduleRepository
import com.example.smartschedule.feature.smartSchedule.domain.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // המודול הזה חי לכל אורך חיי האפליקציה
abstract class ScheduleDataModule {

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        fakeImpl: FakeScheduleRepository
    ): ScheduleRepository
}