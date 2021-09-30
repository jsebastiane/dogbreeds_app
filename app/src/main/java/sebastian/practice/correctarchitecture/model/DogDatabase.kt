package sebastian.practice.correctarchitecture.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//We can only have items of type dogbreed in this database
@Database(entities = arrayOf(DogBreed::class), version = 1)
//be careful to not allow multiple threads to access and edit data at the same time
//Singleton is a solution to this
abstract class DogDatabase: RoomDatabase() {
    abstract fun dogDao(): DogDao

    companion object {
        @Volatile private var instance: DogDatabase? = null
        private val LOCK = Any()



        //create instance and attach it to instance variable
        //we invoke the DogDatabase singleton - we either get an instance because it already exists
        //or we create/build database
        //Synchronize allows us to stop multiple threads from trying to access this block of code
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            //we use application context because context can be volatile sometimes
            //and result in being null - potentially when screen is rotated
            context.applicationContext,
            DogDatabase::class.java,
            "dogdatabase"
        ).build()
    }
}