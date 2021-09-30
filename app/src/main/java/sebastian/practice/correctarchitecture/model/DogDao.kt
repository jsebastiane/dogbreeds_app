package sebastian.practice.correctarchitecture.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//Data access object
//Define what type of function we can perform on our database
//This is for the room database
//Vararg basically means multiple arguments
//List of long - the primary key that we defined in Model
//Suspend - so that it is done on a separate thread
@Dao
interface DogDao {
    @Insert
    //List is returning the primary key that we have defined because when they are downloaded, the
    //dogs data does not have a uuid
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    //Gets everything from the list of dogbreeds
    @Query("SELECT * FROM dogbreed")
    suspend fun getAllDogs(): List<DogBreed>

    //Receive single dog
    @Query("SELECT * FROM dogbreed WHERE uuid = :dogId")
    //It's an INT because uuid is an int
    //Return dogbreed associated with it
    suspend fun getDog(dogId: Int): DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllDogs()
}