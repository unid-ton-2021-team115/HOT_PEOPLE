package ins.hands.unid

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
        KakaoSdk.init(this, "ce1071ca2fd405b7ba6827444533f951")
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(roomModule, networkModule, viewModelModule))
        }
    }
}

val roomModule = module{

}
val BaseUrl = "http://3.37.202.181:3003"
val networkModule = module{
    single{
        OkHttpClient.Builder().apply{
            connectTimeout(5L, TimeUnit.SECONDS)
            writeTimeout(1L,TimeUnit.SECONDS)
            readTimeout(5L,TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

        }.build()
    }
    single<RemoteDateSourcePlace>{
        RemoteDataSourcePlaceImpl(Retrofit.Builder().client(get<OkHttpClient>()).baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build().create(GetPlaceService::class.java))
    }
}
val viewModelModule = module{
    viewModel{
        ApiTestViewModel()
    }
    viewModel {
        MainViewModel()
    }
    viewModel {
        HomeViewModel()
    }
}