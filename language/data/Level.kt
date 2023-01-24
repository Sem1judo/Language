package sem.ua.language.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class Level(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val duration: String
)