package sem.ua.language.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Level::class], version = 1, exportSchema = false)
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun levelDao(): LevelDao

    companion object {
        @Volatile
        private var Instance: LanguageDatabase? = null

        fun getDatabase(context: Context): LanguageDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LanguageDatabase::class.java, "language_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}