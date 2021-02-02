package fr.isen.hugo.androiderestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import com.squareup.picasso.Picasso
import fr.isen.hugo.androiderestaurant.databinding.ActivityItemCartBinding
import fr.isen.hugo.androiderestaurant.model.Cart

class ListItemCartAdapter(val cart: Cart, val buttonPay:Button, val menuActivity:  MenuActivity):
        RecyclerView.Adapter<ListItemCartAdapter.CartHolder>() {
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewTypee: Int
    ): CartHolder {
        val itemBinding = ActivityItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int){
        holder.title.text = cart.itemCarts[position].dish.name
        Picasso.get().load(cart.itemCarts[position].dish.getFirstImage()).into(holder.photo)
        holder.price.text = (cart.itemCarts[position].dish.getPrice() * cart.itemCarts[position].quantity).toString() + " €"
        holder.quantity.text = "x " + cart.itemCarts[position].quantity.toString()
        holder.delete.setOnClickListener {
            setSharedPrefs(holder.layout.context, "cart_item_number", (getSharedPrefs(holder.layout.context, "cart_item_number").toInt() - cart.itemCarts[position].quantity).toString())
            cart.itemCarts.removeAt(position)
            cartWriteToFile(cart, holder.layout.context)
            buttonPay.text = "Pay " + getTotalPrice(cart).toString() + " €"
            notifyDataSetChanged()
            menuActivity.invalidateOptionsMenu()
        }
    }
    override fun getItemCount(): Int = cart.itemCarts.size

    class CartHolder(binding: ActivityItemCartBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.textTitleCartDish
        var photo = binding.imageCartDish
        val price = binding.textPriceCartDishTotal
        val delete = binding.buttonDeleteDishCart
        val quantity = binding.textQuantityCartDish
        val layout = binding.root
    }

    fun getTotalPrice(cart : Cart): Double{
        var total = 0.0
        for (e in cart.itemCarts) {
            total += e.quantity.toDouble() * e.dish.getPrice()
        }
        return total
    }

}