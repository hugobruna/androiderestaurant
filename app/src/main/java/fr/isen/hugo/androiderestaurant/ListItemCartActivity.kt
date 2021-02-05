package fr.isen.hugo.androiderestaurant

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.hugo.androiderestaurant.databinding.ActivityListItemCartBinding
import fr.isen.hugo.androiderestaurant.model.Cart
import fr.isen.hugo.androiderestaurant.payment.demo.PaymentActivity

private lateinit var binding: ActivityListItemCartBinding

class ListItemCartActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        invalidateOptionsMenu()
        title = "Order"
        binding = ActivityListItemCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listItemCart.layoutManager = LinearLayoutManager(this.parent)
        val cart = cartReadFromFile(applicationContext)
        binding.buttonPay.text = "Pay " + getTotalPrice(cart).toString() + " €"
        binding.listItemCart.adapter = ListItemCartAdapter(cart, binding.buttonPay, this)
        binding.buttonPay.setOnClickListener {
            if(getNumberItemCart(applicationContext) > 0) {
                if (getSharedPrefs(applicationContext, "id_login") != "") {
                    val intent = Intent(this, PaymentActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Invalid Information")
                builder.setMessage("No item in c")
                builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
                builder.show()
            }
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