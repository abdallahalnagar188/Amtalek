package eramo.amtalek.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eramo.amtalek.data.local.Converters
import eramo.amtalek.data.local.EventsDB
import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.data.repository.*
import eramo.amtalek.domain.repository.*
import eramo.amtalek.util.parser.GsonParser
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): EventsApi =
        Retrofit.Builder()
            .baseUrl(EventsApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(EventsApi::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideEventsDB(app: Application): EventsDB {
        return Room.databaseBuilder(app, EventsDB::class.java, "EventsDB")
            .addTypeConverter(Converters(GsonParser(Gson())))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(EventsApi: EventsApi): AuthRepository {
        return AuthRepositoryImpl(EventsApi)
    }

    @Provides
    @Singleton
    fun provideDrawerRepository(EventsApi: EventsApi): DrawerRepository {
        return DrawerRepositoryImpl(EventsApi)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(EventsApi: EventsApi): ProductsRepository {
        return ProductsRepositoryImpl(EventsApi)
    }

    @Provides
    @Singleton
    fun provideRequestRepository(EventsApi: EventsApi): RequestRepository {
        return RequestRepositoryImpl(EventsApi)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(EventsApi: EventsApi): OrderRepository {
        return OrderRepositoryImpl(EventsApi)
    }

    @Provides
    @Singleton
    fun provideCartRepository(EventsApi: EventsApi, db: EventsDB): CartRepository {
        return CartRepositoryImpl(EventsApi, db.dao)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}