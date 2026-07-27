# 💱 Currency Converter & Community Chat (Android App)


## 📌 Overview
A dynamic, **Cloud-First** Android application that combines real-time financial utility with community interaction. The app provides live currency exchange rates, persistent user profile management, and a real-time global chat room.

Built around **Firebase** for cloud persistence, ensuring seamless data availability across sessions and devices.

---

## 🚀 Main Features
- 🔐 **Authentication:** Secure Sign-Up/Login using **Firebase Auth** with session persistence (Auto-login).
- 💰 **Real-Time Currency Converter:** Live exchange rates via REST API integration (**Volley**) and dynamic JSON parsing.
- 💬 **Global Chat Pool:** Real-time messaging powered by **Firestore** `addSnapshotListener` and a custom `RecyclerView` adapter.
- 👤 **Profile Management:** Fragment-based system with **Firestore** sync for metadata and profile images.
- 🖼️ **Persistent Image Support:** Uses Android `takePersistableUriPermission` to keep user avatars loaded across device reboots.
- 🚪 **Secure Logout:** Complete Back-Stack cleanup using `FLAG_ACTIVITY_NEW_TASK` & `FLAG_ACTIVITY_CLEAR_TASK`.

---

## 🛠️ Tech Stack & Architecture
- **Language & Environment:** Java / Android SDK
- **UI Architecture:** `ConstraintLayout`, `Material Design`, `RecyclerView`, `Fragments`, `Navigation Component`
- **Backend Services:** Firebase Authentication, Cloud Firestore
- **Networking:** Volley HTTP Library, ExchangeRate-API (REST)
- **Data Handling:** Dynamic JSON Parsers & HashMaps for $O(1)$ calculations

---

## 📱 Core Modules & Logic Highlights

### 1. Authentication (`MainActivity`)
- Session check (`currentUser != null`) before `setContentView()` to prevent layout inflation lag.
- Creates a 1-to-1 Firestore document mapping upon user registration using the Auth UID.

### 2. Currency Converter (`CurrencyConverterActivity`)
- Uses `JsonObjectRequest` to query `ExchangeRate-API`.
- Iterates dynamically through JSON keys so new currencies scale automatically without code changes.
- Caches exchange rates in a `HashMap<String, Double>` for fast computation.

### 3. Community Chat (`ChatPoolActivity`)
- Dynamic UI layout algorithm (sent vs. received message alignment and colors).
- Real-time listener (`addSnapshotListener`) with chronological ordering.
- Smart date formatting inside the adapter (`isToday` logic).

### 4. Profile Management (`ProfileActivity`, `ViewProfileFragment`, `EditProfileFragment`)
- Single-activity container with Android Jetpack `Navigation Component`.
- Solved Uri permission expiration by applying `takePersistableUriPermission()`.

---

## ⚙️ Setup & Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/your-username/currency-converter-android.git](https://github.com/your-username/currency-converter-android.git)
