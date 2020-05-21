package ir.zinutech.android.weatherapp

import android.app.Application
import ir.zinutech.android.weatherapp.core.di.appModule
import ir.zinutech.android.weatherapp.core.di.netModule
import ir.zinutech.android.weatherapp.core.di.repositoriesModule
import ir.zinutech.android.weatherapp.core.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@App)
            modules(appModule + netModule + viewModelsModule + repositoriesModule)
        }
    }
}