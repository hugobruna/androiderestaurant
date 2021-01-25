package fr.isen.hugo.androiderestaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.hugo.androiderestaurant.databinding.ActivityCategoryListBinding
import fr.isen.hugo.androiderestaurant.databinding.CategoryCellBinding
import java.util.*


class CategoryListAdapter(val categories: List<String>):
    RecyclerView.Adapter<CategoryListAdapter.CategoryHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewTypee: Int
    ): CategoryHolder {
        val itemBinding = CategoryCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int){
        holder.title.text = categories[position]
    }
    override fun getItemCount(): Int = categories.size

    class CategoryHolder(binding: CategoryCellBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.textTitleDish
        val photo = binding.imageDish
        val price = binding.textPriceDish
    }


}
