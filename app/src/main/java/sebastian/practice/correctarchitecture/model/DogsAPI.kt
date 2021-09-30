package sebastian.practice.correctarchitecture.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogsAPI {

    //A Single is an observable that emits a single event - a value or error
    //So list of dogs we want to display or will return an error
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}