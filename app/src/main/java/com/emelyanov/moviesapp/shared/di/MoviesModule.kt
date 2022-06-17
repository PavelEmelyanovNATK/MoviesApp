package com.emelyanov.moviesapp.shared.di

import com.emelyanov.moviesapp.navigation.core.CoreNavProvider
import com.emelyanov.moviesapp.shared.domain.services.moviesapi.IMoviesApi
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.MoviesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MoviesModule {

    private val json = Json { ignoreUnknownKeys = true }
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideMoviesApi(): IMoviesApi
    = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory(mediaType))
        .baseUrl("https://s3-eu-west-1.amazonaws.com/")
        .build()
        .create(IMoviesApi::class.java)

    @Singleton
    @Provides
    fun provideMoviesRepository(moviesApi: IMoviesApi): IMoviesRepository = MoviesRepository(moviesApi)

    @Singleton
    @Provides
    fun provideCoreNavProvider(): CoreNavProvider = CoreNavProvider()
}