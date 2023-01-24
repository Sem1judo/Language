package sem.ua.language.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

class OfflineLevelRepository(private val levelDao: LevelDao) : LevelRepository {

    override fun getAllLevelsStream(): Flow<List<Level>> = levelDao.getAllLevels()

    override fun getLevelStream(id: Int): Flow<Level> = levelDao.getLevel(id)

    override suspend fun insertLevel(level: Level) = levelDao.insert(level)

    override suspend fun updateLevel(level: Level) = levelDao.update(level)

    override suspend fun deleteLevel(level: Level) = levelDao.delete(level)

}