package sebastian.practice.correctarchitecture.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DogsAPIService {

    private val BASE_URL = "https://raw.githubusercontent.com"

    private val api = Retrofit.Builder()
        .baseUrl((BASE_URL))
        //Gson converter factory is helping us convert the json into the format (DogBreed) that we want
        .addConverterFactory(GsonConverterFactory.create())
            //Call adapter factory helps return a Single/Observable
            //Converts list of objects/elements into an observable
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DogsAPI::class.java)

    fun getDogs(): Single<List<DogBreed>> {
        return api.getDogs()
    }
}