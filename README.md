
# VISION — Sportswear Shopping Application

![Android](https://img.shields.io/badge/Platform-Android-green)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple)
![Android Studio](https://img.shields.io/badge/IDE-Android%20Studio-blue)
![Status](https://img.shields.io/badge/Status-Development-orange)

## About the Project

VISION is an Android sportswear shopping application developed
using Kotlin and Android Studio.

The application allows users to browse products, search for
sportswear, view product details, save favourites, and manage
a shopping cart.

VISION was developed as part of the Software Development
Portfolio of Evidence (POE) at Rosebank College.

## Features

### User Authentication
- User registration
- User login and logout
- Local SQLite account storage
- Salted and hashed passwords
- Persistent user information

### Product Catalogue
- Product grid displaying available items
- Search products by name or category
- View individual product details
- REST API integration using Retrofit
- Fallback product catalogue

### Shopping Cart
- Add products to cart
- View saved cart items
- Persistent cart storage
- Account-specific shopping carts

### Favourites
- Add products to favourites
- View saved favourites
- Persistent favourites storage

### Offline Functionality
- Local SQLite storage
- Offline access to saved favourites
- Persistent shopping cart information
- Fallback products when API requests fail

### User Preferences
- Save preferred sport
- Store account-specific preferences

## Technologies Used

| Technology | Purpose |
|------------|---------|
| Kotlin | Application development |
| Android Studio | Development environment |
| XML | User interface |
| SQLite | Local database |
| Retrofit | REST API communication |
| JSON | Data exchange |
| Gradle | Build automation |
| GitHub | Version control |
| GitHub Actions | Automated builds |

## REST API

VISION uses the DummyJSON API to retrieve product information.

API URL:
https://dummyjson.com/products

The application uses Retrofit to handle API requests.

If the API becomes unavailable, the application displays
fallback product data.

## Installation

### Requirements

- Android Studio
- Android SDK 36
- JDK 17
- Android emulator or physical Android device
- Internet connection for live product data

### Clone the Repository

```bash
git clone https://github.com/zaheerbrown/Vision-Part3.git
```

### Run the Application

1. Open Android Studio.
2. Select Open Project.
3. Choose the Vision project folder.
4. Wait for Gradle synchronisation.
5. Start an Android emulator or connect an Android phone.
6. Click Run.
7. The application will install and launch.

## Project Structure

```text
Vision/
|
|-- app/
|   |-- src/
|   |   |-- main/
|   |   |   |-- java/
|   |   |   |-- res/
|   |   |   |-- AndroidManifest.xml
|   |   |-- test/
|   |   |-- androidTest/
|   |
|   |-- build.gradle.kts
|
|-- gradle/
|-- .github/
|   |-- workflows/
|
|-- build.gradle.kts
|-- settings.gradle.kts
|-- README.md
```

## Testing

The following features should be verified:

| Test | Expected Result |
|------|-----------------|
| Registration | User account created |
| Login | Valid user can log in |
| Invalid Login | Access denied |
| Product Search | Matching products displayed |
| Shopping Cart | Products saved successfully |
| Favourites | Products remain saved |
| Offline Mode | Local data remains available |
| API Failure | Fallback catalogue displayed |
| Logout | User session ends |

Test results should be recorded after verification.

## GitHub Actions

The project includes a GitHub Actions workflow for
automated Android build verification.

To view build results:

1. Open the GitHub repository.
2. Select the Actions tab.
3. Open the Android build workflow.
4. Check whether the build passed or failed.

## Security

VISION stores user accounts locally using SQLite.

Passwords are protected using salted, iterative SHA-256
hashing.

The current authentication system is designed for
educational demonstration purposes.

The application does not process real payments.

## Current Limitations

The following features are not yet fully implemented:

- Firebase Authentication
- Google Single Sign-On
- Cloud NoSQL database
- Offline-to-cloud synchronisation
- Firebase push notifications
- Complete multilingual support
- Online payment processing
- Google Play Store publication

## Future Improvements

Planned enhancements include:

1. Firebase Authentication and Google Sign-In
2. Cloud Firestore database integration
3. Real-time push notifications
4. English, Afrikaans and isiZulu translations
5. Automatic cloud synchronisation
6. Order management and tracking
7. Secure payment gateway integration
8. Google Play Store release

## Academic Information

**Project:** VISION Sportswear Shopping Application

**Module:** Software Development POE — Part 3

**Institution:** Rosebank College

**Programming Language:** Kotlin

**Development Environment:** Android Studio

## Developer

**Mogamat Zaheer Brown**

Diploma in Software Development  
Rosebank College  
Cape Town, South Africa

**GitHub:**  
https://github.com/zaheerbrown

**Portfolio:**  
https://zaheerbrown-portfolio.netlify.app/

## Project Status

Development Build — Part 3 POE

Core shopping functionality and local account management
have been implemented. Cloud services, notifications,
release preparation and final assessment evidence require
further completion or verification.

---

**VISION — Explore Sportswear. Discover Your Style.**
