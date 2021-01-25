package fr.isen.hugo.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.hugo.androiderestaurant.databinding.ActivityCategoryListBinding

private lateinit var binding: ActivityCategoryListBinding

class CategoryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        title = intent.getStringExtra("category")
        binding.listCategory.adapter = CategoryListAdapter(listOf("p1","p2","p3"))
    }
}