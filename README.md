# ProductKeeper - Android App for Browsing Products, Managing Wishlists, Purchases, and Reviews.

This Android application showcases a complete product catalog system with Firebase integration, user authentication, product browsing, reviews, wishlists, and purchase management.

Developed by **Kyriakos Papoutsis**

Bachelor of Science (BSc) in Digital Systems
*Specialization: Software and Data Systems*  
*Secondary Track: Information Systems*  
Department of Digital Systems, University of Piraeus

---

## 📱 Features

### 🔐 User Authentication
- Register and log in using Firebase Authentication
- Email-based login system

### 🛍️ Product Catalog
- Browse a collection of products with:
  - Name, description, price, discount, image
  - Specifications (size, color, material, etc.)
- Filter by:
  - Brand  
  - Price range  
  - Type  
- Search products by name
- Navigation handled via Android Jetpack Navigation Component

### ⭐ Wishlist
- Add/remove products from a personal wishlist
- Wishlist items stored in Firebase Firestore

### 🧾 Purchases
- Submit purchase quantities using a Material Slider
- Update purchase quantities directly from the list
- Purchases stored in Firestore

### 📝 Reviews
- Leave reviews only for previously purchased products
- 5-star rating bar and text review input
- View all reviews associated with a product

### 👤 User Profile
- Display logged-in user’s email
- Logout functionality with session clearing

---

## 🏗 Architecture & Tech Stack

| Layer                | Technologies                                                             |
| -------------------- | ------------------------------------------------------------------------ |
| **Language**         | Kotlin                                                                   |
| **IDE**              | Android Studio                                                           |
| **Architecture**     | MVVM (ViewModel, LiveData)                                               |
| **Backend Services** | Firebase Authentication • Firebase Firestore                             |
| **UI Components**    | RecyclerView (Products, Purchases, Reviews) • Material Design Components |
| **Navigation**       | Jetpack Navigation Component                                             |
| **Async Operations** | Kotlin Coroutines                                                        |
| **Image Loading**    | Glide                                                                    |
| **Build System**     | Gradle                                                                   |

---

## 📱 Screenshots

Below are selected screenshots from the **ProductKeeper** app, showcasing its main features, UI, and product listing system.

> The app includes over 20 unique views and components.  
> For clarity, screenshots are grouped by category.

---

### 🔐 Sign In & Register
<p align="center">
  <img src="screenshots/signin.png" width="200" />
  <img src="screenshots/signin-invalidpassword.png" width="200" />
  <img src="screenshots/signin-usedemail.png" width="200" />
  <img src="screenshots/registration.png" width="200" />
</p>

---

### 🏠 Homepage & Filtering
<p align="center">
  <img src="screenshots/homepage-1.png" width="200" />
  <img src="screenshots/homepage-2.png" width="200" />
  <img src="screenshots/homepage-3.png" width="200" />
  <img src="screenshots/homepage-4.png" width="200" />
  <img src="screenshots/homepage-5.png" width="200" />
  <img src="screenshots/homepage-6.png" width="200" />
  <img src="screenshots/homepage-7.png" width="200" />
  <img src="screenshots/homepage-8.png" width="200" />
  <img src="screenshots/filtering.png" width="200" />
</p>

---

### 🔍 Search Functionality
<p align="center">
  <img src="screenshots/search-1.png" width="200" />
  <img src="screenshots/search-2.png" width="200" />
  <img src="screenshots/search-3.png" width="200" />
  <img src="screenshots/search-4.png" width="200" />
  <img src="screenshots/search-5.png" width="200" />
  <img src="screenshots/search-6.png" width="200" />
</p>

---

### 🛍️ Products List, Wishlist & Reviews
<p align="center">
  <img src="screenshots/productslist.png" width="200" />
  <img src="screenshots/wishlist.png" width="200" />
  <img src="screenshots/reviews-1.png" width="200" />
  <img src="screenshots/reviews-2.png" width="200" />
  <img src="screenshots/reviews-3.png" width="200" />
  <img src="screenshots/reviews-4.png" width="200" />
</p>

---

Contact: kyriakosiam@outlook.com

