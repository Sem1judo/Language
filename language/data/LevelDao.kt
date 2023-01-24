package sem.ua.language.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface LevelDao {


    @Query("SELECT * from levels")
    fun getAllLevels(): Flow<List<Level>>

    @Query("SELECT * from levels WHERE id = :id")
    fun getLevel(id: Int): Flow<Level>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(level: Level)

    @Update
    suspend fun update(level: Level)

    @Delete
    suspend fun delete(level: Level)
}