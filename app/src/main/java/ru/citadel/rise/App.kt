package ru.citadel.rise

import android.app.Application
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.kryptoprefs.preferences.KryptoBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.citadel.rise.data.IServerService
import ru.citadel.rise.data.IServerService.Companion.BASE_URL
import ru.citadel.rise.data.Prefs
import ru.citadel.rise.data.PrefsConst
import ru.citadel.rise.data.SynchronousCallAdapterFactory

class App : Application(){


    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .build()

        service = retrofit.create(IServerService::class.java)

        preferences = Prefs(KryptoBuilder.hybrid(this, PrefsConst.PREFS_NAME))

        val opts = IO.Options().apply {
            reconnection = true
            reconnectionDelay = 3000
            forceNew = true
        }
        mSocket = IO.socket("$BASE_URL/chat", opts)
    }

    companion object {
        private lateinit var service: IServerService
        val api: IServerService
            get() = service

        private lateinit var preferences : Prefs
        val prefs: Prefs
            get() = preferences

        private lateinit var mSocket: Socket
        val socket: Socket
            get() = mSocket
    }
}