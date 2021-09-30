package sebastian.practice.correctarchitecture.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_detail.*
import sebastian.practice.correctarchitecture.R
import sebastian.practice.correctarchitecture.databinding.FragmentDetailBinding
import sebastian.practice.correctarchitecture.util.getProgressDrawable
import sebastian.practice.correctarchitecture.util.loadImage
import sebastian.practice.correctarchitecture.viewmodel.DetailViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var hippoUuid = 0

    private lateinit var dataBinding: FragmentDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            hippoUuid = DetailFragmentArgs.fromBundle(it).hippoUuid
        }

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.fetch(hippoUuid)



        observeViewModel()
    }

    //Here we are setting values for elements on the detail fragment
    //We did not do this before because the list was a recyclerView, this is just a single page fragment
    private fun observeViewModel(){
        viewModel.dogLiveData.observe(viewLifecycleOwner, Observer { dog ->
            dog?.let{
                dataBinding.dog = dog
            }
        })
    }

}
