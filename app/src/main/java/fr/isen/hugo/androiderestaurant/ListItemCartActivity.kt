package fr.isen.hugo.androiderestaurant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.hugo.androiderestaurant.databinding.ActivityListItemCartBinding
import fr.isen.hugo.androiderestaurant.model.Cart

private lateinit var binding: ActivityListItemCartBinding

class ListItemCartActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Order"
        binding = ActivityListItemCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listItemCart.layoutManager = LinearLayoutManager(this)
        val cart = cartReadFromFile(applicationContext)
        binding.buttonPay.text = "Pay " + getTotalPrice(cart).toString() + " €"
        binding.listItemCart.adapter = ListItemCartAdapter(cart, binding.buttonPay)
        binding.buttonPay.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun getTotalPrice(cart : Cart): Double{
        var total = 0.0
        for (e in cart.itemCarts) {
            total += e.quantity.toDouble() * e.dish.getPrice()
        }
        return total
    }
}