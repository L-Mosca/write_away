package br.com.writeaway.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.writeaway.domain.models.Note
import br.com.writeaway.util.DateConverter
import javax.inject.Inject

@Database(entities = [Note::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class WriteAwayDb : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var INSTANCE: WriteAwayDb? = null

        fun getDatabase(context: Context): WriteAwayDb {
            return if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WriteAwayDb::class.java,
                        "WriteAwayApp.Database"
                    ).fallbackToDestructiveMigration().build()
                }
                INSTANCE as WriteAwayDb
            } else {
                INSTANCE as WriteAwayDb
            }
        }
    }
}