package sebastian.practice.correctarchitecture.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

//AndroidViewModel is used instead of ViewModel because it can take the application context
//We do not want to take activity context because that can change/be destroyed
abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    //We will have a job running and when it is done we will return to our main thread
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    //When baseViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}