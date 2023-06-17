package jp.co.yumemi.android.code_check.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.constants.Constant.BASE_URL
import jp.co.yumemi.android.code_check.network.GitHubAccountApiService
import jp.co.yumemi.android.code_check.repository.GitHubAccountRepository
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
        return retrofit.build()
    }

    @Singleton
    @Provides
    fun provideGitHubAccountApiService(retrofit: Retrofit): GitHubAccountApiService {
        return retrofit.create(GitHubAccountApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideGitHubRepository(githubAccountApiService: GitHubAccountApiService): GitHubAccountRepository {
        return GitHubAccountRepository(githubAccountApiService)
    }
}