package com.kevin.statsdroid.di

import com.kevin.statsdroid.data.repository.StatsRepositoryImpl
import com.kevin.statsdroid.domain.repository.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindStatsRepository(statsRepositoryImpl: StatsRepositoryImpl): StatsRepository
}
