package ru.citadel.rise

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.citadel.rise.data.IServerService
import ru.citadel.rise.data.SynchronousCallAdapterFactory

class App : Application(){


    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl(IServerService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .build()

        service = retrofit.create(IServerService::class.java)
    }

    companion object {
        private lateinit var service: IServerService
        val api: IServerService
            get() = service
    }
}