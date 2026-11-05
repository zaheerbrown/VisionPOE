package com.example.vision

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vision.data.Product
import com.example.vision.data.ProductRepository
import com.example.vision.data.Store
import com.example.vision.data.VisionDatabase
import android.text.InputType
import android.widget.LinearLayout
import com.example.vision.ui.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var progress: ProgressBar
    private lateinit var empty: TextView
    private lateinit var search: SearchView
    private lateinit var title: TextView
    private var allProducts = listOf<Product>()
    private lateinit var database: VisionDatabase
    private val prefs by lazy { getSharedPreferences("vision_session", MODE_PRIVATE) }
    private var account: String? = null
    private fun persist() { account?.let(database::save) }
    private fun setAccount(email: String?) {
        account = email
        prefs.edit().putString("email", email).apply()
        if (email == null) { Store.favourites.clear(); Store.cart.clear() } else database.load(email)
        if (::adapter.isInitialized) { adapter.notifyDataSetChanged(); showHome() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = VisionDatabase(this)
        account = prefs.getString("email", null)
        account?.let(database::load)
        recycler = findViewById(R.id.productRecycler)
        progress = findViewById(R.id.progress)
        empty = findViewById(R.id.emptyText)
        search = findViewById(R.id.searchView)
        title = findViewById(R.id.screenTitle)
        adapter = ProductAdapter(emptyList(), ::showProduct, ::persist)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter
        setupSearch()
        setupNavigation()
        loadProducts()
    }

    private fun loadProducts() {
        progress.visibility = View.VISIBLE
        lifecycleScope.launch {
            allProducts = try { ProductRepository.loadProducts() } catch (_: Exception) { ProductRepository.fallbackProducts() }
            Store.products = allProducts
            progress.visibility = View.GONE
            showHome()
        }
    }

    private fun setupSearch() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                val text = q.orEmpty()
                adapter.submit(allProducts.filter { it.title.contains(text, true) || it.category.contains(text, true) })
                return true
            }
        })
    }

    private fun setupNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomNav).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> showHome()
                R.id.nav_favourites -> showFavourites()
                R.id.nav_cart -> showCart()
                R.id.nav_profile -> showProfile()
            }
            true
        }
    }

    private fun showHome() {
        title.text = "DISCOVER VISION"
        search.visibility = View.VISIBLE
        recycler.visibility = View.VISIBLE
        empty.visibility = View.GONE
        adapter.submit(allProducts)
    }

    private fun showFavourites() {
        title.text = "YOUR FAVOURITES"
        search.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        val favs = allProducts.filter { Store.favourites.contains(it.id) }
        adapter.submit(favs)
        empty.text = "No favourites yet. Tap the star on a product you love."
        empty.visibility = if (favs.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showCart() {
        val cartItems = allProducts.filter { Store.cart.containsKey(it.id) }
        val lines = cartItems.joinToString("\n\n") { "${it.title}  ×${Store.cart[it.id]}\nR %.2f".format(it.price * 18 * (Store.cart[it.id] ?: 1)) }
        MaterialAlertDialogBuilder(this)
            .setTitle("Your Cart (${Store.cartCount()} items)")
            .setMessage(if (lines.isBlank()) "Your cart is empty." else "$lines\n\nTOTAL: R %.2f".format(Store.cartTotal() * 18))
            .setNegativeButton("Close", null)
            .setNeutralButton(if (lines.isBlank()) "Browse" else "Clear cart") { _, _ ->
                Store.cart.clear(); persist(); showHome()
            }
            .setPositiveButton("Checkout") { _, _ ->
                MaterialAlertDialogBuilder(this).setTitle("Checkout not connected")
                    .setMessage("This demo does not process payments or place real orders. Your items remain in the cart.")
                    .setPositiveButton("OK", null).show()
            }.show()
    }

    private fun showProfile() {
        val email = account
        if (email == null) {
            MaterialAlertDialogBuilder(this).setTitle("VISION account")
                .setMessage("Sign in or register to save your cart, favourites and sport on this device. Internet account syncing is not enabled.")
                .setPositiveButton("Sign in") { _, _ -> accountDialog(false) }
                .setNeutralButton("Register") { _, _ -> accountDialog(true) }
                .setNegativeButton("Close", null).show()
            return
        }
        val sports = arrayOf("Football", "Basketball", "Running", "Gym & Training", "Casual Sportswear")
        MaterialAlertDialogBuilder(this).setTitle("Profile: $email")
            .setMessage("Main sport: ${database.sport(email)}\nSaved locally for offline access.")
            .setPositiveButton("Change sport") { _, _ ->
                MaterialAlertDialogBuilder(this).setTitle("Choose your sport")
                    .setSingleChoiceItems(sports, sports.indexOf(database.sport(email))) { dialog, which ->
                        database.setSport(email, sports[which]); dialog.dismiss(); showProfile()
                    }.setNegativeButton("Cancel", null).show()
            }
            .setNeutralButton("Sign out") { _, _ -> persist(); setAccount(null); Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show() }
            .setNegativeButton("Close", null).show()
    }

    private fun accountDialog(register: Boolean) {
        val form = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(48, 12, 48, 4) }
        val email = EditText(this).apply { hint = "Email address"; inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS }
        val password = EditText(this).apply { hint = "Password (at least 8 characters)"; inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD }
        form.addView(email); form.addView(password)
        val dialog = MaterialAlertDialogBuilder(this).setTitle(if (register) "Register" else "Sign in")
            .setView(form).setNegativeButton("Cancel", null)
            .setPositiveButton(if (register) "Create account" else "Sign in", null).create()
        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val address = email.text.toString().trim().lowercase()
                val pass = password.text.toString()
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(address).matches()) { email.error = "Enter a valid email"; return@setOnClickListener }
                if (pass.length < 8) { password.error = "At least 8 characters"; return@setOnClickListener }
                val ok = if (register) database.register(address, pass) else database.authenticate(address, pass)
                if (ok) { setAccount(address); dialog.dismiss(); Toast.makeText(this, "Welcome to VISION", Toast.LENGTH_SHORT).show() }
                else Toast.makeText(this, if (register) "Account already exists" else "Incorrect email or password", Toast.LENGTH_LONG).show()
            }
        }
        dialog.show()
    }

    private fun showProduct(p: Product) {
        val view = layoutInflater.inflate(R.layout.dialog_product, null)
        val image = view.findViewById<ImageView>(R.id.detailImage)
        if (p.thumbnail.isNotBlank()) image.load(p.thumbnail) { placeholder(R.drawable.product_placeholder); error(R.drawable.product_placeholder) }
        else image.setImageResource(R.drawable.product_placeholder)
        view.findViewById<TextView>(R.id.detailName).text = p.title
        view.findViewById<TextView>(R.id.detailCategory).text = p.category.uppercase()
        view.findViewById<TextView>(R.id.detailPrice).text = "R %.2f".format(p.price * 18)
        view.findViewById<TextView>(R.id.detailDescription).text = p.description
        MaterialAlertDialogBuilder(this).setView(view).setNegativeButton("Close", null)
            .setPositiveButton("Add to cart") { _, _ -> Store.addToCart(p.id); persist(); Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show() }.show()
    }
}
