package sebastian.practice.correctarchitecture.view

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_hippo.view.*
import sebastian.practice.correctarchitecture.R
import sebastian.practice.correctarchitecture.databinding.ItemHippoBinding
import sebastian.practice.correctarchitecture.model.DogBreed
import sebastian.practice.correctarchitecture.util.getProgressDrawable
import sebastian.practice.correctarchitecture.util.loadImage

// We will push a list through to this class of type DogBreed
class DogsListAdapter(val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener {

    fun updateDogList(newDogsList: List<DogBreed>){
        dogsList.clear()
        // does not add the list...it adds all elements of the list
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_hippo, parent, false)
        val view = DataBindingUtil.inflate<ItemHippoBinding>(inflater, R.layout.item_hippo, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog = dogsList[position]
        holder.view.listener = this
        // dogsList is declared as an arrayList of DogBreed with dogBreed being a variable from that class
//        holder.view.name.text = dogsList[position].dogBreed
//        holder.view.lifespan.text = dogsList[position].lifeSpan
//        holder.view.setOnClickListener {
//            val action = ListFragmentDirections.actionDetailFragment()
//            //passing Uuid to the next fragment
//            action.hippoUuid = dogsList[position].uuid
//            //for any view created it will take you to the detailFragment
//            Navigation.findNavController(it).navigate(action)
//        }
//        //loadImage() can be used as it is a functions with type ImageView in Util - pretty cool
//        //ImageView is last
//        holder.view.image_view.loadImage(dogsList[position].imageUrl, getProgressDrawable(holder.view.image_view.context))

    }

    override fun onDogCliked(v: View) {
        val action = ListFragmentDirections.actionDetailFragment()
        val uuid = v.dog_id.text.toString().toInt()
            //passing Uuid to the next fragment
            action.hippoUuid = uuid
            //for any view created it will take you to the detailFragment
            Navigation.findNavController(v).navigate(action)
    }

    class DogViewHolder(var view: ItemHippoBinding): RecyclerView.ViewHolder(view.root)

}