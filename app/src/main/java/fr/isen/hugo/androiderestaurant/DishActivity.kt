package fr.isen.hugo.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import fr.isen.hugo.androiderestaurant.databinding.ActivityDishBinding
import fr.isen.hugo.androiderestaurant.model.Dish

private lateinit var binding: ActivityDishBinding

class DishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.textDishDesc.text = dishDetails.ingredients.map { it.name }.joinToString(", ")
        }
        if(dishDetails != null){
            dishDetails.getAllImages()?.let {
                binding.carousselPictureDish.adapter = DishAdapter(this, it)
            }

        }

        binding.editNumberDish.setOnClickListener{
            val quantity = binding.editNumberDish.text.toString().toDouble()
            if(quantity > 1) {
                val total = quantity * (dishDetails?.getPrice() ?: 0.0)
                binding.buttonAddCart.text = "Add to cart $total €"
            }
        }

        binding.increaseButton.setOnClickListener {
            val quantity = binding.editNumberDish.text.toString().toInt()
            binding.editNumberDish.setText((quantity + 1).toString())
        }

        binding.increaseButton.setOnClickListener {
            val quantity = binding.editNumberDish.text.toString().toInt()
            if(quantity > 1) {
                binding.editNumberDish.setText(quantity - 1)
            }
        }

        binding.buttonAddCart.setOnClickListener {
            Snackbar.make(binding.root, "Added to cart", Snackbar.LENGTH_LONG).show()
        }
    }


}