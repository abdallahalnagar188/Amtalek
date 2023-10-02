package eramo.amtalek.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eramo.amtalek.data.local.entity.AllImagesEntity
import eramo.amtalek.data.local.entity.CartDataEntity

@Database(
    entities = [CartDataEntity::class, AllImagesEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EventsDB : RoomDatabase() {
    abstract val dao: EventsDao
}