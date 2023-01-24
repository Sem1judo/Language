package sem.ua.language.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface LevelRepository {

    fun getAllLevelsStream(): Flow<List<Level>>

    fun getLevelStream(id: Int): Flow<Level>

    suspend fun insertLevel(level: Level)

    suspend fun updateLevel(level: Level)

    suspend fun deleteLevel(level: Level)

}