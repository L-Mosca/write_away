package br.com.writeaway

import android.app.Application
import br.com.writeaway.domain.database.WriteAwayDb
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WriteAwayApp : Application() {

    lateinit var db: WriteAwayDb

    override fun onCreate() {
        super.onCreate()
        db = WriteAwayDb.getDatabase(this)
    }

}