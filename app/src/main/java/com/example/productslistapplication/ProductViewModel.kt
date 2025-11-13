package com.example.productslistapplication

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // LiveData for products
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val originalProductsList = listOf(
        // Populate with actual product data (Original list of products)
        Product(
            "iPhone 15",
            R.drawable.iphone_image,
            "The latest flagship smartphone from Apple, featuring cutting-edge technology and a sleek design.",
            899.99,
            10,
            "4.5 stars",
            "iOS 18",
            "6.2 Inch Screen",
            "Pink",
            151
        ),
        Product(
            "Samsung Galaxy Fold 2",
            R.drawable.samsung_image,
            "A premium foldable smartphone that combines the functionality of a phone and a tablet in one device.",
            2099.99,
            14,
            "4 stars",
            "Android 20",
            "6.27 Inch Unfolded Screen",
            "Gold",
            29
        ),
        Product(
            "iPad Pro",
            R.drawable.ipad_image,
            "A powerful tablet designed for professionals, with a stunning Liquid Retina display and support for Apple Pencil.",
            999.99,
            19,
            "4.8 stars",
            "iPadOS 15",
            "12.9 Inch Screen",
            "Space Gray",
            87
        ),
        Product(
            "Oral-B Electric Toothbrush",
            R.drawable.toothbrush_image,
            "An advanced electric toothbrush that removes up to 100% more plaque along the gumline than a regular manual toothbrush.",
            79.99,
            13,
            "4.7 stars",
            "Electric Toothbrush",
            "Medium",
            "White",
            128
        ),
        Product(
            "LG TV",
            R.drawable.lg_tv_image,
            "An ultra-slim 4K OLED TV with AI ThinQ technology, offering stunning picture quality and immersive sound.",
            1799.99,
            25,
            "4.6 stars",
            "webOS 6.0",
            "55 Inch Screen",
            "Black",
            63
        ),
        Product(
            "Toy Soldier",
            R.drawable.toy_soldier_image,
            "A classic toy soldier figurine, perfect for collectors and imaginative play.",
            9.99,
            5,
            "4.2 stars",
            "Plastic",
            "Small",
            "Green",
            301
        ),
        Product(
            "Teddybear",
            R.drawable.teddy_bear_image,
            "A cuddly teddy bear made from soft, high-quality materials, ideal for children and gift-giving.",
            19.99,
            7,
            "4.5 stars",
            "Plush",
            "Medium",
            "Brown",
            209
        ),
        Product(
            "Nerf Blaster",
            R.drawable.nerf_blaster_image,
            "A high-performance Nerf blaster for exciting outdoor battles, featuring rapid-fire action and a sleek design.",
            39.99,
            13,
            "4.3 stars",
            "Foam Dart Gun",
            "Large",
            "Blue",
            127
        ),
        Product(
            "Barbie Doll",
            R.drawable.barbie_doll_image,
            "A glamorous Barbie doll with stylish outfits and accessories, inspiring creativity and storytelling.",
            24.99,
            16,
            "4.4 stars",
            "Plastic",
            "Standard",
            "Pink",
            182
        ),
        Product(
            "Furby",
            R.drawable.furby_image,
            "An interactive Furby toy that speaks and interacts with kids, fostering friendship and fun playtimes.",
            29.99,
            18,
            "4.1 stars",
            "Electronic Toy",
            "Medium",
            "Purple",
            94
        ),
        Product(
            "Harry Potter and the Philosopher's Stone",
            R.drawable.harry_potter_book_image,
            "The first book in the Harry Potter series, where young wizard Harry Potter discovers the magical world of Hogwarts.",
            12.99,
            19,
            "4.9 stars",
            "Paperback",
            "Standard",
            "Multicolor",
            283
        ),
        Product(
            "A Promised Land",
            R.drawable.promised_land_book_image,
            "Former President Barack Obama's memoir, offering insights into his presidency and personal journey.",
            29.99,
            10,
            "4.7 stars",
            "Hardcover",
            "Standard",
            "Gray",
            174
        ),
        Product(
            "Fire and Blood",
            R.drawable.fire_and_blood_book_image,
            "A fantasy novel by George R.R. Martin, chronicling the history of House Targaryen in the world of Game of Thrones.",
            18.99,
            17,
            "4.6 stars",
            "Hardcover",
            "Standard",
            "Red",
            142
        ),
        Product(
            "12 Rules for Life",
            R.drawable.rules_for_life_book_image,
            "A self-help book by Jordan B. Peterson, offering principles for living a meaningful and fulfilling life.",
            15.99,
            12,
            "4.5 stars",
            "Paperback",
            "Standard",
            "Black",
            195
        ),
        Product(
            "Frying Pan",
            R.drawable.frying_pan_image,
            "A durable non-stick frying pan, perfect for cooking delicious meals with ease.",
            39.99,
            11,
            "4.7 stars",
            "Aluminum",
            "Medium",
            "Silver",
            98
        ),
        Product(
            "Kenwood Blender",
            R.drawable.blender_image,
            "A powerful blender for making smoothies, soups, and sauces, with multiple speed settings and a robust design.",
            49.99,
            11,
            "4.6 stars",
            "Blender",
            "Large",
            "Black",
            81
        ),
        Product(
            "Knife Set",
            R.drawable.knife_set_image,
            "A high-quality stainless steel knife set, featuring a variety of knives for all your culinary needs.",
            69.99,
            5,
            "4.8 stars",
            "Stainless Steel",
            "Standard",
            "Silver",
            63
        ),
        Product(
            "Pot Set",
            R.drawable.pot_set_image,
            "A comprehensive set of non-stick pots and pans, perfect for everyday cooking and culinary experiments.",
            89.99,
            9,
            "4.7 stars",
            "Non-Stick Aluminum",
            "Standard",
            "Black",
            56
        ),
        Product(
            "Peeler",
            R.drawable.peeler_image,
            "A versatile vegetable peeler with a comfortable grip, making peeling fruits and vegetables quick and easy.",
            9.99,
            17,
            "4.4 stars",
            "Stainless Steel",
            "Standard",
            "Silver",
            138
        ),
        Product(
            "Pillow Set",
            R.drawable.pillow_set_image,
            "A plush pillow set with hypoallergenic filling, providing comfort and support for a restful sleep.",
            29.99,
            20,
            "4.5 stars",
            "Cotton",
            "Standard",
            "White",
            112
        ),
        Product(
            "Lamp",
            R.drawable.lamp_image,
            "A modern LED lamp with adjustable brightness levels, perfect for reading, studying, or ambient lighting.",
            34.99,
            14,
            "4.6 stars",
            "LED Lamp",
            "Medium",
            "Black",
            95
        ),
        Product(
            "Couch",
            R.drawable.couch_image,
            "A comfortable fabric couch with deep cushions and sturdy construction, ideal for relaxing and lounging.",
            499.99,
            13,
            "4.8 stars",
            "Fabric",
            "3-Seater",
            "Gray",
            42
        ),
        Product(
            "Chair",
            R.drawable.chair_image,
            "An ergonomic office chair with lumbar support and adjustable height, promoting good posture and comfort.",
            149.99,
            11,
            "4.7 stars",
            "Mesh",
            "Standard",
            "Black",
            87
        ),
        Product(
            "Table",
            R.drawable.table_image,
            "A stylish dining table made from solid wood, perfect for family meals and gatherings.",
            279.99,
            25,
            "4.6 stars",
            "Wood",
            "6-Seater",
            "Brown",
            54
        ),
        Product(
            "Lip Kit",
            R.drawable.lip_kit_image,
            "A trendy lip kit featuring a matte lipstick and matching lip liner, adding color and definition to your lips.",
            14.99,
            11,
            "4.3 stars",
            "Cosmetics",
            "Standard",
            "Pink",
            204
        ),
        Product(
            "Fragrance",
            R.drawable.fragrance_image,
            "A captivating fragrance with floral and woody notes, perfect for everyday wear or special occasions.",
            49.99,
            20,
            "4.7 stars",
            "Eau de Parfum",
            "Standard",
            "Clear",
            79
        ),
        Product(
            "Mascara",
            R.drawable.mascara_image,
            "A volumizing mascara that adds length and definition to your lashes, enhancing your eye makeup look.",
            12.99,
            25,
            "4.5 stars",
            "Cosmetics",
            "Standard",
            "Black",
            183
        ),
        Product(
            "Eyeliner",
            R.drawable.eyeliner_image,
            "A long-lasting liquid eyeliner with a precise brush tip, perfect for creating dramatic or subtle eye looks.",
            9.99,
            21,
            "4.4 stars",
            "Cosmetics",
            "Standard",
            "Black",
            215
        ),
        Product(
            "Foundation",
            R.drawable.foundation_image,
            "A lightweight foundation with buildable coverage, leaving your skin looking flawless and natural.",
            19.99,
            10,
            "4.6 stars",
            "Cosmetics",
            "Standard",
            "Beige",
            157
        ),
        Product(
            "Baseball Bat",
            R.drawable.baseball_bat_image,
            "A durable aluminum baseball bat for recreational and competitive play, designed for power and precision.",
            29.99,
            19,
            "4.5 stars",
            "Aluminum",
            "Standard",
            "Silver",
            112
        ),
        Product(
            "Jordan Basketball",
            R.drawable.jordan_basketball_image,
            "A high-performance basketball endorsed by NBA legend Michael Jordan, featuring superior grip and durability.",
            39.99,
            10,
            "4.6 stars",
            "Rubber",
            "Standard",
            "Orange",
            87
        ),
        Product(
            "Soccer Ball",
            R.drawable.soccer_ball_image,
            "A professional-grade soccer ball with a durable synthetic leather cover, designed for optimal performance.",
            24.99,
            11,
            "4.4 stars",
            "Synthetic Leather",
            "Standard",
            "White",
            129
        ),
        Product(
            "Golf Club Set",
            R.drawable.golf_club_set_image,
            "A complete set of golf clubs including irons, woods, and a putter, suitable for golfers of all skill levels.",
            299.99,
            7,
            "4.7 stars",
            "Steel",
            "Standard",
            "Black",
            38
        ),
        Product(
            "Tennis Racket",
            R.drawable.tennis_racket_image,
            "A high-quality graphite tennis racket with enhanced stability and control, perfect for competitive matches.",
            89.99,
            15,
            "4.5 stars",
            "Graphite",
            "Standard",
            "Blue",
            74
        ),
        Product(
            "Nike Hoodie",
            R.drawable.nike_hoodie_image,
            "A comfortable Nike hoodie made from soft fleece fabric, ideal for workouts or casual wear.",
            49.99,
            24,
            "4.6 stars",
            "Cotton Blend",
            "Standard",
            "Gray",
            112
        ),
        Product(
            "Jordan Shorts",
            R.drawable.jordan_shorts_image,
            "Stylish Jordan basketball shorts with breathable fabric and a comfortable fit, perfect for on and off the court.",
            34.99,
            22,
            "4.5 stars",
            "Polyester",
            "Standard",
            "Black",
            157
        ),
        Product(
            "Supreme Jacket",
            R.drawable.supreme_jacket_image,
            "An exclusive Supreme jacket featuring a bold design and premium materials, making a statement in streetwear.",
            199.99,
            17,
            "4.7 stars",
            "Nylon",
            "Standard",
            "Red",
            63
        ),
        Product(
            "Louis Vuitton Cardigan",
            R.drawable.louis_vuitton_cardigan_image,
            "A luxurious Louis Vuitton cardigan crafted from fine wool, combining elegance and comfort in designer fashion.",
            599.99,
            9,
            "4.8 stars",
            "Wool",
            "Standard",
            "Beige",
            28
        ),
        Product(
            "Gucci Bucket Hat",
            R.drawable.gucci_bucket_hat_image,
            "An iconic Gucci bucket hat made from premium canvas, featuring the classic GG pattern and adjustable fit.",
            299.99,
            23,
            "4.6 stars",
            "Canvas",
            "Standard",
            "Brown",
            42
        ),
        Product(
            "Call of Duty III",
            R.drawable.call_of_duty_iii_image,
            "A thrilling first-person shooter game with intense multiplayer battles and a gripping single-player campaign.",
            59.99,
            21,
            "4.7 stars",
            "Action",
            "Standard",
            "Black",
            92
        ),
        Product(
            "Helldivers",
            R.drawable.helldivers_image,
            "A cooperative twin-stick shooter game where players defend humanity against alien threats in procedurally generated missions.",
            29.99,
            16,
            "4.5 stars",
            "Action",
            "Standard",
            "Green",
            117
        ),
        Product(
            "NBA2K19",
            R.drawable.nba2k19_image,
            "An authentic basketball simulation game with realistic gameplay and immersive graphics, featuring NBA stars and teams.",
            49.99,
            18,
            "4.6 stars",
            "Sports",
            "Standard",
            "Blue",
            86
        ),
        Product(
            "Overwatch 2",
            R.drawable.overwatch_2_image,
            "An action-packed team-based shooter game with diverse heroes and dynamic maps, offering exciting multiplayer battles.",
            69.99,
            17,
            "4.7 stars",
            "Action",
            "Standard",
            "Orange",
            74
        ),
        Product(
            "FIFA 20",
            R.drawable.fifa_20_image,
            "A popular soccer simulation game with realistic player movements and strategic gameplay, featuring top leagues and teams.",
            39.99,
            24,
            "4.5 stars",
            "Sports",
            "Standard",
            "White",
            103
        ),
        Product(
            "Dog Food",
            R.drawable.dog_food_image,
            "Nutritious dry dog food formulated with real meat and essential vitamins, supporting overall health and vitality.",
            29.99,
            21,
            "4.6 stars",
            "Dry Food",
            "Standard",
            "Brown",
            156
        ),
        Product(
            "Cat Food",
            R.drawable.cat_food_image,
            "Premium cat food with a balanced blend of proteins and nutrients, promoting healthy digestion and shiny coat.",
            24.99,
            9,
            "4.5 stars",
            "Dry Food",
            "Standard",
            "Yellow",
            192
        ),
        Product(
            "Bird Food",
            R.drawable.bird_food_image,
            "High-quality bird food enriched with seeds and vitamins, attracting a variety of wild birds to your garden.",
            14.99,
            4,
            "4.4 stars",
            "Seed Mix",
            "Standard",
            "Green",
            241
        ),
        Product(
            "Aquarium",
            R.drawable.aquarium_image,
            "A spacious glass aquarium kit with LED lighting and filtration system, creating a vibrant underwater environment.",
            99.99,
            8,
            "4.7 stars",
            "Glass",
            "Standard",
            "Clear",
            78
        ),
        Product(
            "Dog Bone Toy",
            R.drawable.dog_bone_toy_image,
            "A durable chew toy for dogs, designed to withstand chewing and promote dental health, available in various sizes.",
            12.99,
            10,
            "4.5 stars",
            "Rubber",
            "Large",
            "Red",
            183
        )

    )

    init {
        // Initializing LiveData with original list of products
        _products.value = originalProductsList
    }

    // Filter products based on query and filter option
    fun filterProducts(query: String, filterOption: String) {
        _products.value = originalProductsList.filter {
            (it.name.contains(query, true) ||
                    it.description.contains(query, true) ||
                    it.price.toString().contains(query, true) ||
                    it.reviews.contains(query, true) ||
                    it.specifications.contains(query, true) ||
                    it.color.contains(query, true)) &&
                    (filterOption == "All" || it.matchesFilter(filterOption))
        }
    }

    // Filter products based on price range
    fun filterProductsPrice(query: String, filterOption: String) {
        _products.value = originalProductsList.filter {
            (it.name.contains(query, true) ||
                    it.description.contains(query, true) ||
                    it.price.toString().contains(query, true) ||
                    it.reviews.contains(query, true)) &&
                    (filterOption == "Price" || it.matchesPriceRange(filterOption))
        }
    }

    // Filter products based on brand
    fun filterProductsBrand(query: String, filterOption: String) {
        _products.value = originalProductsList.filter {
            (it.name.contains(query, true) ||
                    it.description.contains(query, true) ||
                    it.price.toString().contains(query, true) ||
                    it.reviews.contains(query, true)) &&
                    (filterOption == "Brand" || it.matchesBrand(filterOption))
        }
    }

    // Flag to check if Firestore has been initialized
    private var isFirestoreInitialized = false

    // Save products to Firestore if not already saved (They are saved only once)
    fun saveProductsToFirestoreIfNotSaved(context: Context) {
        if (!isFirestoreInitialized) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Check if the products collection already exists
                    val querySnapshot = db.collection("products").get().await()
                    if (querySnapshot.isEmpty) {
                        val productsToSave = originalProductsList.map {
                            mapOf(
                                "name" to it.name,
                                "imageResId" to it.imageResId,
                                "description" to it.description,
                                "price" to it.price,
                                "reviews" to it.reviews,
                                "specifications" to it.specifications,
                                "size" to it.size,
                                "color" to it.color,
                                "availability" to it.availability
                            )
                        }

                        val batch = db.batch()
                        for (product in productsToSave) {
                            val docRef = db.collection("products").document()
                            batch.set(docRef, product)
                        }
                        batch.commit().await()
                        isFirestoreInitialized = true
                    } else {
                        isFirestoreInitialized = true
                    }
                } catch (e: Exception) {
                    // Handle error
                    Log.e(ContentValues.TAG, "Error saving documents", e)
                }
            }
        }
    }
}

// Data class representing a product
data class Product(
    val name: String,
    val imageResId: Int,
    val description: String,
    val price: Double,
    val discount: Int,
    val reviews: String,
    val specifications: String,
    val size: String,
    val color: String,
    val availability: Int,
    var inWishlist: Boolean = false // property for wishlist status, default is false

) {
    // Function to check if product matches the given filter option
    fun matchesFilter(filterOption: String): Boolean {
        return when (filterOption) {
            // Define categories and associated products
            "Electronics" -> {
                name in listOf(
                    "iPhone 15",
                    "Samsung Galaxy Fold 2",
                    "iPad Pro",
                    "Oral-B Electric Toothbrush",
                    "LG TV"
                )
            }

            "Toys" -> {
                name in listOf(
                    "Toy Soldier",
                    "Teddybear",
                    "Nerf Blaster",
                    "Barbie Doll",
                    "Furby"
                )
            }

            "Books" -> {
                name in listOf(
                    "Harry Potter and the Philosopher's Stone",
                    "A Promised Land",
                    "Fire and Blood",
                    "12 Rules for Life"
                )
            }

            "Kitchen" -> {
                name in listOf(
                    "Frying Pan",
                    "Kenwood Blender",
                    "Knife Set",
                    "Pot Set",
                    "Peeler"
                )
            }

            "Home" -> {
                name in listOf(
                    "Pillow Set",
                    "Lamp",
                    "Couch",
                    "Chair",
                    "Table"
                )
            }

            "Beauty" -> {
                name in listOf(
                    "Lip Kit",
                    "Fragrance",
                    "Mascara",
                    "Eyeliner",
                    "Foundation"
                )
            }

            "Sports" -> {
                name in listOf(
                    "Baseball Bat",
                    "Jordan Basketball",
                    "Soccer Ball",
                    "Golf Club Set",
                    "Tennis Racket"
                )
            }

            "Clothing" -> {
                name in listOf(
                    "Nike Hoodie",
                    "Jordan Shorts",
                    "Supreme Jacket",
                    "Louis Vuitton Cardigan",
                    "Gucci Bucket Hat"
                )
            }

            "Video Games" -> {
                name in listOf(
                    "Call of Duty III",
                    "Helldivers",
                    "NBA2K19",
                    "Overwatch 2",
                    "FIFA 20"
                )
            }

            "Pets" -> {
                name in listOf(
                    "Dog Food",
                    "Cat Food",
                    "Bird Food",
                    "Aquarium",
                    "Dog Bone Toy"
                )
            }

            else -> false // Return false for any unknown filter option
        }
    }

    // Function to check if product matches the given price range
    fun matchesPriceRange(filterOption: String): Boolean {
        return when (filterOption) {

            "\$1000+" -> {
                name in listOf(
                    "Samsung Galaxy Fold 2",
                    "LG TV"
                )
            }

            "\$500 - \$1000" -> {
                name in listOf(
                    "iPhone 15",
                    "iPad Pro",
                    "Louis Vuitton Cardigan"
                )
            }

            "\$100 - \$500" -> {
                name in listOf(
                    "Couch",
                    "Chair",
                    "Table",
                    "Supreme Jacket",
                    "Gucci Bucket Hat"
                )
            }

            "\$0 - \$100" -> {
                name in listOf(
                    "Oral-B Electric Toothbrush",
                    "Toy Soldier",
                    "Teddybear",
                    "Nerf Blaster",
                    "Barbie Doll",
                    "Furby",
                    "Harry Potter and the Philosopher's Stone",
                    "A Promised Land",
                    "Fire and Blood",
                    "12 Rules for Life",
                    "Frying Pan",
                    "Kenwood Blender",
                    "Knife Set",
                    "Pot Set",
                    "Peeler",
                    "Pillow Set",
                    "Lamp",
                    "Lip Kit",
                    "Fragrance",
                    "Mascara",
                    "Eyeliner",
                    "Foundation",
                    "Baseball Bat",
                    "Jordan Basketball",
                    "Soccer Ball",
                    "Golf Club Set",
                    "Tennis Racket",
                    "Nike Hoodie",
                    "Jordan Shorts",
                    "Call of Duty III",
                    "Helldivers",
                    "NBA2K19",
                    "Overwatch 2",
                    "FIFA 20",
                    "Dog Food",
                    "Cat Food",
                    "Bird Food",
                    "Aquarium",
                    "Dog Bone Toy"
                )
            }

            else -> false // Return false for any unknown filter option
        }
    }

    // Function to check if product matches the given brand
    fun matchesBrand(filterOption: String): Boolean {
        return when (filterOption) {

            "Activision" -> {
                name in listOf(
                    "Call of Duty III",
                    "Overwatch 2"
                )
            }

            "Amazon" -> {
                name in listOf(
                    "A Promised Land",
                    "Fire and Blood",
                    "Harry Potter and the Philosopher's Stone",
                    "12 Rules for Life"
                )
            }

            "Apple" -> {
                name in listOf(
                    "iPhone 15",
                    "iPad Pro"
                )
            }

            "Blizzard" -> {
                name in listOf(
                    "Helldivers"

                )
            }

            "EA" -> {
                name in listOf(
                    "FIFA 20",
                    "NBA2K19"
                )
            }

            "Gucci" -> {
                name in listOf(
                    "Gucci Bucket Hat"

                )
            }

            "Hasbro" -> {
                name in listOf(
                    "Nerf Blaster",
                    "Teddybear",
                    "Furby"
                )
            }

            "Hondos Center" -> {
                name in listOf(
                    "Fragrance",
                    "Mascara",
                    "Eyeliner",
                    "Foundation"
                )
            }

            "IKEA" -> {
                name in listOf(
                    "Couch",
                    "Chair",
                    "Table",
                    "Lamp",
                    "Pillow Set"
                )
            }

            "Jordan" -> {
                name in listOf(
                    "Jordan Basketball",
                    "Jordan Shorts"

                )
            }

            "Kenwood" -> {
                name in listOf(
                    "Kenwood Blender",
                    "Frying Pan",
                    "Knife Set",
                    "Pot Set",
                    "Peeler"

                )
            }

            "Kylie Cosmetics" -> {
                name in listOf(
                    "Lip Kit"

                )
            }

            "LG" -> {
                name in listOf(
                    "LG TV"
                )
            }

            "Louis Vuitton" -> {
                name in listOf(
                    "Louis Vuitton Cardigan"
                )
            }

            "Mattel" -> {
                name in listOf(
                    "Toy Soldier",
                    "Barbie Doll"
                )
            }

            "Nike" -> {
                name in listOf(
                    "Nike Hoodie",
                    "Soccer Ball",
                    "Golf Club Set",
                    "Baseball Bat",
                    "Tennis Racket"
                )
            }

            "Oral-B" -> {
                name in listOf(
                    "Oral-B Electric Toothbrush"

                )
            }

            "PetCo" -> {
                name in listOf(
                    "Dog Food",
                    "Cat Food",
                    "Bird Food",
                    "Aquarium",
                    "Dog Bone Toy"

                )
            }

            "Samsung" -> {
                name in listOf(
                    "Samsung Galaxy Fold 2"

                )
            }

            "Supreme" -> {
                name in listOf(
                    "Supreme Jacket"

                )
            }

            else -> false // Return false for any unknown filter option
        }
    }


}

