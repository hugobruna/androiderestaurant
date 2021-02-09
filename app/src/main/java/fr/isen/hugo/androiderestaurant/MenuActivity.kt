package fr.isen.hugo.androiderestaurant

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.isen.hugo.androiderestaurant.databinding.LayoutCartMenuBinding


open class MenuActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_menu, menu)

        val cartButton = menu?.findItem(R.id.cart_button)?.actionView
        val textView : TextView? = cartButton?.findViewById(R.id.textBadge)
        textView?.text = getNumberItemCart(applicationContext).toString()
        cartButton?.setOnClickListener {
            val listItemCartActivity = Intent(this, ListItemCartActivity::class.java)
            startActivity(listItemCartActivity)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cart_button -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        invalidateOptionsMenu()
        super.onResume()
    }
}