package jp.co.yumemi.android.code_check.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.constants.Constant.BASE_URL
import jp.co.yumemi.android.code_check.network.GitHubAccountApiService
import jp.co.yumemi.android.code_check.network.util.NetworkConnectivityInterceptor
import jp.co.yumemi.android.code_check.repository.GitHubAccountRepository
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Module responsible for providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides the base URL for the API.
     */
    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    /**
     * Provides the converter factory for JSON parsing.
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides the HTTP client for making network requests.
     */
    @Singleton
    @Provides
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(NetworkConnectivityInterceptor(context))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    /**
     * Provides the Retrofit instance for API communication.
     *
     * @param okHttpClient The OkHttpClient instance.
     * @param baseUrl The base URL for the API.
     * @param converterFactory The converter factory for JSON parsing.
     */
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

    /**
     * Provides the GitHubAccountApiService instance for accessing GitHub API endpoints.
     *
     * @param retrofit The Retrofit instance.
     */
    @Singleton
    @Provides
    fun provideGitHubAccountApiService(retrofit: Retrofit): GitHubAccountApiService {
        return retrofit.create(GitHubAccountApiService::class.java)
    }

    /**
     * Provides the GitHubAccountRepository instance for managing GitHub account data.
     *
     * @param githubAccountApiService The GitHubAccountApiService instance.
     */
    @Singleton
    @Provides
    fun provideGitHubRepository(githubAccountApiService: GitHubAccountApiService): GitHubAccountRepository {
        return GitHubAccountRepository(githubAccountApiService)
    }
}