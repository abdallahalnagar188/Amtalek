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
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.MyInterceptor
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
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): AmtalekApi =
        Retrofit.Builder()
            .baseUrl(AmtalekApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(AmtalekApi::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(MyInterceptor())
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
    fun provideAuthRepository(AmtalekApi: AmtalekApi): AuthRepository {
        return AuthRepositoryImpl(AmtalekApi)
    }

    @Provides
    @Singleton
    fun provideDrawerRepository(AmtalekApi: AmtalekApi): DrawerRepository {
        return DrawerRepositoryImpl(AmtalekApi)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(AmtalekApi: AmtalekApi): ProductsRepository {
        return ProductsRepositoryImpl(AmtalekApi)
    }

    @Provides
    @Singleton
    fun provideRequestRepository(AmtalekApi: AmtalekApi): RequestRepository {
        return RequestRepositoryImpl(AmtalekApi)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(AmtalekApi: AmtalekApi): OrderRepository {
        return OrderRepositoryImpl(AmtalekApi)
    }

    @Provides
    @Singleton
    fun provideCartRepository(AmtalekApi: AmtalekApi, db: EventsDB): CartRepository {
        return CartRepositoryImpl(AmtalekApi, db.dao)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}