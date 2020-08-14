package ru.citadel.rise.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.citadel.rise.data.model.*


@Database(
    entities = [User::class, Project::class, UserWithTheirProjects::class, UserWithFavProjects::class,
                ChatShortView::class, UserChatRelation::class, Chat::class],
    version = 1,
    exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun dao(): IDaoLocal

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            LocalDatabase::class.java, "rise_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
