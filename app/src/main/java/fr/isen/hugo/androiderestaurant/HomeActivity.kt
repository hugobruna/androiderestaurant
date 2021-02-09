package fr.isen.hugo.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fr.isen.hugo.androiderestaurant.databinding.ActivityHomeBinding
import fr.isen.hugo.androiderestaurant.databinding.LayoutCartMenuBinding
import fr.isen.hugo.androiderestaurant.payment.demo.PaymentActivity

private lateinit var binding: ActivityHomeBinding
private lateinit var bindingCartMenu: LayoutCartMenuBinding


class HomeActivity : MenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        invalidateOptionsMenu()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonStarter.setOnClickListener{
            Toast.makeText(applicationContext, "STARTER", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CategoryListActivity::class.java)
            intent.putExtra("category", "Starter")
            startActivity(intent)
        }

        binding.buttonMain.setOnClickListener {
            Toast.makeText(applicationContext, "MAIN", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CategoryListActivity::class.java)
            intent.putExtra("category", "Main")
            startActivity(intent)
        }

        binding.buttonDessert.setOnClickListener {
            Toast.makeText(applicationContext, "DESSERT", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CategoryListActivity::class.java)
            intent.putExtra("category", "Dessert")
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("INFO APP","Home Activity is killed")
    }

    override fun onBackPressed() {

    }
}