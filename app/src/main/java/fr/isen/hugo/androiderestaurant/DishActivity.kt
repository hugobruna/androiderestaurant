package fr.isen.hugo.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.internal.ContextUtils
import com.google.android.material.snackbar.Snackbar
import fr.isen.hugo.androiderestaurant.databinding.ActivityDishBinding
import fr.isen.hugo.androiderestaurant.model.Cart
import fr.isen.hugo.androiderestaurant.model.Dish
import fr.isen.hugo.androiderestaurant.model.ItemCart

private lateinit var binding: ActivityDishBinding

class DishActivity : MenuActivity() {
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
        val total = (dishDetails?.getPrice() ?: 0.0)
        binding.buttonAddCart.text = "Add to cart $total €"

        binding.editNumberDish.setOnClickListener{
            val quantity = binding.editNumberDish.text.toString().toDouble()
            if(quantity > 0) {
                val total = quantity * (dishDetails?.getPrice() ?: 0.0)
                binding.buttonAddCart.text = "Add to cart $total €"
            }
        }

        binding.increaseButton.setOnClickListener {
            val quantity = binding.editNumberDish.text.toString().toInt()+1
            val total = quantity * (dishDetails?.getPrice() ?: 0.0)
            binding.buttonAddCart.text = "Add to cart $total €"
            binding.editNumberDish.setText(quantity.toString())

        }

        binding.discreaseButton.setOnClickListener {
            val quantity = binding.editNumberDish.text.toString().toInt()-1
            if(quantity > 0) {
                val total = quantity * (dishDetails?.getPrice() ?: 0.0)
                binding.buttonAddCart.text = "Add to cart $total €"
                binding.editNumberDish.setText(quantity.toString())
            }
        }

        binding.buttonAddCart.setOnClickListener {
            if (dishDetails != null) {
                addToCart(dishDetails)
                Snackbar.make(binding.root, "Added to cart", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun addToCart(dish: Dish){
        val quantity : Int = binding.editNumberDish.text.toString().toInt()
        val itemCart = ItemCart(dish, quantity)
        val cart = cartReadFromFile(applicationContext)
        setSharedPrefs(applicationContext, "cart_item_number", (quantity + getNumberItemCart(applicationContext)).toString())
        if(cart.itemCarts != null){
            cart.itemCarts.add(itemCart)
        } else {
            cart.itemCarts = mutableListOf(itemCart)
        }
        cartWriteToFile(cart, applicationContext)
        invalidateOptionsMenu()
    }


}