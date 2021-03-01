package com.mint.weaponmestari.di

import com.google.gson.Gson
import com.mint.weaponmestari.networking.ResponseAdapterFactory
import com.mint.weaponmestari.networking.WeaponService
import com.mint.weaponmestari.networking.mock.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): WeaponService {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/user/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(ResponseAdapterFactory())
            .build()
            .create(WeaponService::class.java)
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(MockInterceptor())
            .build()
    }

}