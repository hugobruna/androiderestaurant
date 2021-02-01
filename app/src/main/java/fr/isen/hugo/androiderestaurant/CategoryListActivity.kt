package fr.isen.hugo.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.hugo.androiderestaurant.databinding.ActivityCategoryListBinding
import fr.isen.hugo.androiderestaurant.model.Dish
import fr.isen.hugo.androiderestaurant.model.dataResult
import org.json.JSONException
import org.json.JSONObject


private lateinit var binding: ActivityCategoryListBinding

class CategoryListActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val category = intent.getStringExtra("category")
        title = category
        binding.listCategory.layoutManager = LinearLayoutManager(this)
        val categoryFR = if(category.toString() == "Starter") "Entrées" else if(category.toString() == "Main") "Plats" else "Desserts"
        loadDataFromCategory(categoryFR)
    }

    private fun loadDataFromCategory(category: String) {
        val postUrl = "http://test.api.catering.bluecodegames.com/" + "menu"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        postData.put("id_shop", "1")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, postUrl, postData, {
            val gson: dataResult = Gson().fromJson(it.toString(), dataResult::class.java)
            gson.data.firstOrNull { it.name == category }?.dishes?.let { dishes ->
                displayDishes(dishes)
            } ?: run {
                Log.e("CategoryListActivity", "Noting found :(")
                /*
                binding.categoryLoading.visibility = View.GONE
                binding.categoryErrorMessage.text = "Nothing found :("
                 */
            }
        },{
            Log.e("CategoryListActivity",  it.toString())
        })
        requestQueue.add(jsonObjectRequest)
    }

    private fun displayDishes(dishes: List<Dish>){
        // binding.categoryLoading.visibility = View.GONE
        binding.listCategory.layoutManager = LinearLayoutManager(this)
        binding.listCategory.adapter = CategoryListAdapter(dishes) {
            val intent = Intent(this, DishActivity::class.java)
            intent.putExtra("Dish", it)
            startActivity(intent)
        }
    }

}