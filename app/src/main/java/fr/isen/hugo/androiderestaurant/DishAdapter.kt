package fr.isen.hugo.androiderestaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.isen.hugo.androiderestaurant.DishFragment.Companion.ARG_OBJECT



class DishAdapter(activity: AppCompatActivity, val imagesImport: List<String?>):FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return imagesImport.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = DishFragment()
        fragment.arguments = Bundle().apply {
            putString(ARG_OBJECT, imagesImport[position])
        }
        return fragment
    }
}