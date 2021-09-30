package sebastian.practice.correctarchitecture.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import sebastian.practice.correctarchitecture.model.DogBreed
import sebastian.practice.correctarchitecture.model.DogDatabase
import sebastian.practice.correctarchitecture.model.DogsAPIService
import sebastian.practice.correctarchitecture.util.SharedPreferencesHelper
import sebastian.practice.correctarchitecture.view.DogsListAdapter

private const val TAG = "POOPMAN"

class ListViewModel(application: Application): BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val dogsService = DogsAPIService()
    //Allow us to observe the observable the api gives us
    //Not have to worry about getting rid of observable - avoid memory leaks
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    // will notify there is an error
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


    //refresh will decide whether to get data from remote or local database
    fun refresh(){
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        }else {
            fetchFromRemote()
        }
    }

    fun refreshBypassCache(){
        fetchFromRemote()
    }

    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }
    //Return data from remote endpoint
    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(
            //Retrieving dog data from api
            //we don't want this on the api thread because
            //it can block application while call is finished
            //make call on separate thread
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>(){

                    //When we get data we will first store it in the database then update the UI
                    //These functions are attached to the subscribeWith mentioned above
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                        Toast.makeText(getApplication(), "Dogs retrieved from endpoint", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        //this will log the error in our logs
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>){
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>){
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
                dao.deleteAllDogs()
            //We can send multiple arguments one by one but not as a list altogether
            //WWe are getting the uuids back into result
            val result = dao.insertAll(*list.toTypedArray())
            //UUID is currently set to 0
            var i = 0
            while(i < list.size){
                //Remove After
                Log.d(TAG, "Result: ${result[i]} ---- List[i]: ${list[i].uuid}")
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}