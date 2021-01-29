package fr.isen.hugo.androiderestaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import fr.isen.hugo.androiderestaurant.databinding.DishItemPictureBinding

private lateinit var binding: DishItemPictureBinding

private const val PARAM = "null"

class DishFragment:Fragment() {

    private val param1: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DishItemPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(ARG_OBJECT)?.let {
            Picasso.get().load(it).into(binding.imageViewCarrouselDish)
        }
    }
    companion object{
        const val ARG_OBJECT = "object"
    }

}