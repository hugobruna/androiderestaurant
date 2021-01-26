package fr.isen.hugo.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.hugo.androiderestaurant.databinding.ActivityDishBinding
import fr.isen.hugo.androiderestaurant.model.Dish

private lateinit var binding: ActivityDishBinding

class DishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dishDetails = (intent.getSerializableExtra("Dish") as? Dish)
        if (dishDetails != null) {
            title = dishDetails.name
        }
        if (dishDetails != null) {
            binding.textPriceDish.text = dishDetails.getFormattedPrice()
        }
        if (dishDetails != null) {
            binding.textDishTitle.text = dishDetails.name
        }
        if (dishDetails != null) {
            binding.textDishDesc.text = dishDetails.ingredients.joinToString { ", " }
        }
        setContentView(R.layout.activity_dish)
    }
}