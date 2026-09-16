# 💱 Currency Converter & Community Chat (Android App)


## 📌 Overview
A dynamic, **Cloud-First** Android application that combines real-time financial utility with community interaction. The app provides live currency exchange rates, persistent user profile management, and a real-time global chat room.

Built around **Firebase** for cloud persistence, ensuring seamless data availability across sessions and devices.

---

## 🚀 Main Features
- 🔐 **Authentication:** Secure Sign-Up/Login using **Firebase Auth** with session persistence (Auto-login).
  <img width="623" height="285" alt="image" src="https://github.com/user-attachments/assets/ce842df1-296c-43e5-9212-d5e6d809db2c" />  <img width="226" height="501" alt="image" src="https://github.com/user-attachments/assets/8ee8c9d5-b30a-407b-a2de-73c97218c448" />

  
- 💰 **Real-Time Currency Converter:** Live exchange rates via REST API integration (**Volley**) and dynamic JSON parsing.
  <img width="690" height="346" alt="image" src="https://github.com/user-attachments/assets/cf8041e0-d953-4610-9ba1-aee924a37b95" />
  <img width="165" height="367" alt="image" src="https://github.com/user-attachments/assets/59a3b868-d482-4b17-a23d-4491ebfe98da" />


- 💬 **Global Chat Pool:** Real-time messaging powered by **Firestore** `addSnapshotListener` and a custom `RecyclerView` adapter.
  <img width="182" height="406" alt="image" src="https://github.com/user-attachments/assets/cdaf5040-66ae-4267-937a-5fd9aa3850f1" />

- 👤 **Profile Management:** Fragment-based system with **Firestore** sync for metadata and profile images.
  <img width="627" height="354" alt="image" src="https://github.com/user-attachments/assets/99bba4a4-01ac-4ad5-90b7-fbf4f83882e9" />
  <img width="222" height="492" alt="image" src="https://github.com/user-attachments/assets/c69d7f8f-90f3-45ac-84f1-459c37095e10" />


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
