# VISION — Android sportswear catalogue (Part 3 development build)

## Open in Android Studio
1. Extract the ZIP and open the **Vision** folder (the folder containing `settings.gradle.kts`).
2. Let Gradle sync, with Android SDK 36 installed. Use JDK 17 if prompted.
3. Connect an Android phone with USB debugging, select the device and click Run.
4. Internet is required for live catalogue images/products. The fallback catalogue is available when the API fails.

## Implemented
- Product grid, category/name search, product details, favourites and cart.
- Public REST API via Retrofit (`dummyjson.com`), with fallback catalogue.
- Register, login and logout using a local SQLite database; salted iterative SHA-256 password hashing.
- Persistent per-account favourites, cart and preferred sport stored locally for offline access.
- Graceful network fallback and clear non-functional checkout messaging.
- Existing GitHub Actions workflow and Android unit-test setup.

## Important assessment gaps — NOT implemented
- **Hosted authentication / SSO:** registration is **local to the device**; not Firebase Auth, Google SSO or cloud-based.
- **Cloud NoSQL database and offline sync:** SQLite is local only, and there is no server reconciliation or synchronization.
- **Real-time push notifications:** requires Firebase Cloud Messaging project setup and server-side sending.
- **Two South African languages:** translation resources are not yet implemented.
- **Payments / orders:** no payment processor or order backend. Checkout intentionally does not claim success.
- **Google Play submission:** no signing key, signed release AAB, Play Console upload or publishing evidence.
- **Device demonstration video and narration:** must be recorded by the student on a physical phone.
- **Verified build:** this environment does not have an Android SDK/Gradle executable to run the full build; verify Android Studio sync and device build.

## Privacy and security
This is a demonstration app, not production authentication. Passwords are salted and hashed in SQLite, but there is no rate limiting, password recovery, cloud account management, or secure token-based API login. Do not use a real password you use elsewhere. User records are stored on the device. This app does not accept payment information.

## Further work for full Part 3
Set up Firebase Authentication, Firestore offline persistence, Firebase Cloud Messaging, translations, proper user-facing settings, signed release, tests and documentation. Do not commit Firebase service secrets, signing keystores or passwords. Record an on-device walkthrough showing the actual working features and cloud console data.
