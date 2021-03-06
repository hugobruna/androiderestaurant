package fr.isen.hugo.androiderestaurant


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.hugo.androiderestaurant.databinding.CategoryCellBinding
import fr.isen.hugo.androiderestaurant.model.Dish
import java.io.Serializable


class CategoryListAdapter(val categories: List<Dish>, private val categoriesClickListener: (Dish) -> Unit):
    RecyclerView.Adapter<CategoryListAdapter.CategoryHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewTypee: Int
    ): CategoryHolder {
        val itemBinding = CategoryCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int){
        holder.title.text = categories[position].name
        Picasso.get().load(categories[position].getFirstImage()).into(holder.photo)
        holder.price.text = categories[position].getFormattedPrice()
        holder.layout.setOnClickListener { categoriesClickListener.invoke(categories[position])

        }

    }
    override fun getItemCount(): Int = categories.size

    class CategoryHolder(binding: CategoryCellBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.textTitleDishCategory
        var photo = binding.imageDish
        val price = binding.textPriceDishCategory
        val layout = binding.root
    }


}
