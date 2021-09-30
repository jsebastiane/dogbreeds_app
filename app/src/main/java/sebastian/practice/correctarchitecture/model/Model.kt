package sebastian.practice.correctarchitecture.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//The room library will treat this as an entity that can be put into our database
@Entity //We can put a table name here if we want - it will take DogBreed as default
data class DogBreed(
    //connect variables with fields in API database for dogs
    //This are matching with fields in a json
    //If name if variable matches exactly the name in the json then we do not need to include
    //@SerializedName
    //We're adding column names because we have conjoined words, otherwise default is set to val name

    //We need to create a Primary Key
    //Each item will only get ID if it is put into the local database
    @ColumnInfo(name = "breed_id")
    @SerializedName("id")
    val breedId: String?,

    @ColumnInfo(name = "dog_breed")
    @SerializedName("name")
    val dogBreed: String?,

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifeSpan: String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    val breedGroup: String?,

    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    val bredFor: String?,

    @SerializedName("temperament")
    val temperament: String?,

    @ColumnInfo(name = "dog_url")
    @SerializedName("url")
    val imageUrl: String?
) {
    //This will be stored in the database as well
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}