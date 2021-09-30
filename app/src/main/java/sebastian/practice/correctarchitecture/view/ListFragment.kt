package sebastian.practice.correctarchitecture.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*

import sebastian.practice.correctarchitecture.R
import sebastian.practice.correctarchitecture.viewmodel.ListViewModel


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    // Going to use this to instantiate our listview method
    private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogsListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //With viewModel we do not have to concern ourselves with the app lifecycle
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        //triggers creation of new livedata that will be used for backing exposed data and replacing
        //with previous one

        //creates our dog data
        viewModel.refresh()
        //dogsList is the Recycler view int he fragment
        dogsList.apply{
            //Allows us to order our items sequentially
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        refresh_layout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            list_error.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refresh_layout.isRefreshing = false
        }

        observeViewModel()

    }

    //Going to use values from ListViewModel to update the layout based on the values that we get
    fun observeViewModel(){

        //We are observing the MutableLiveData variable dogs
        viewModel.dogs.observe(viewLifecycleOwner, Observer {dogs ->
            //check if not null
            dogs?.let {
                //dogsList is the recyclerView
                dogsList.visibility = View.VISIBLE
                //dogs is a mutableLiveData list - how does arrayList pass through it
                //sending the data to the recyclerView Adapter which populates the recyclerView
                dogsListAdapter.updateDogList(dogs)
            }
        })

        //variable does not need to have a new name -- isError, can just be called 'it'
        viewModel.dogsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            //if we have an error this runs
            isError?.let {
                list_error.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                //loading view is the progressBar in the fragment_list layout
                //loading view is visible if returning true. If it is true we hide list_error
                //and dogsList - I guess whichever one is being displayed
                loading_view.visibility = if(it) View.VISIBLE else View.GONE
                //if it is loading all other views are hidden
                if(it){
                    list_error.visibility = View.GONE
                    dogsList.visibility = View.GONE
                }
            }
        })
    }


}
