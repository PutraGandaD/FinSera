# FinSera-Android
![Screen Shot 2024-08-27 at 5 26 08 PM](https://github.com/user-attachments/assets/251159b8-adb0-4175-b8c7-65d376e1ee4e)

## Group Members (Android Team) :
- Putra Ganda Dewata (Tech Lead)
- Muhammad Ramadhan
- Wisnu Aryo
- Laila Dwi Latifa

## How to try this app?
Download the application from this [Releases](https://github.com/FinSera-SYNRGY7/FinSera-Android/releases/tag/pre-release) page.
And the credential for login =
Username : johndoe
Password : password123
Transaction PIN : 123456
**Notes:**
**Application PIN is created by user itself when user first login!**
**Fingerprint Auth will be turned on automatically when user devices having fingerprint set.**
**Use Transaction PIN to proceed transaction in Transfer Sesama Bank/Antar Bank/VA/E-Wallet/QRIS**

## Features :
- [x] Login + Create APP Pin + Fingerprint Authentication
- [x] Transfer Sesama Bank
- [x] Transfer Antar Bank
- [x] Transaksi Virtual Account
- [x] Transaksi E-Wallet (GoPay, OVO, ShopeePay, DANA, PayPal)
- [x] Info Saldo
- [x] Mutasi Rekening
- [x] QRIS Merchant (semua merchant) dan sesama pengguna FinSera
- [x] Daftar Tersimpan di semua transaksi (Transfer Antar/Sesama/VA/E-Wallet)
- [x] Ganti PIN Aplikasi        

## Tech Stack
- UI With Views (XML) and Material Design 3 (https://m3.material.io)
- Built with Clean Architecture
- Implemented Dependency Injection (Koin)
- With Reactive Programming Approach (with StateFlow, Kotlin Flow, Coroutines, UI State, etc...)
- Single Activity Approach with multiple Fragments
- Using Android Jetpack Navigation Component for Navigation between Fragments
- Using Shared Preferences for saving local constant data (Access/Refresh Token, Login State, etc)
- Using Room Database for storing local database (Daftar Tersimpan)
- Using AndroidX Biometric library for backward compability from Android M (6.0) to Android 10
- Using Retrofit for API Connectivity in the app
- Using Chucker for debugging API Request
- Using JUnit4 and Mock Web Server for Unit Tests
- and many more...

## Architecture Overview :
![338453153-4e436873-9f2a-4e6f-8a7c-022f2cb0f24b](https://github.com/user-attachments/assets/f877fc03-2176-4214-a179-16bf02037df2)

## CI/CD
This project utilize CI/CD with GitHub Actions to automatically build the APK and AAB. Please go to Actions tab above (next to Pull requests tab).

## Firebase
This project utilize Firebase Crashlytics and Firebase Performance Monitoring.
See this commit for details :
https://github.com/FinSera-SYNRGY7/FinSera-Android/commit/aa9af205a2b41101f224c4b51958450b3b073df9
![Screen Shot 2024-08-27 at 8 25 55 PM](https://github.com/user-attachments/assets/c37ab59d-ba1b-4404-ae78-4a438461c214)
![Screen Shot 2024-08-27 at 8 26 38 PM](https://github.com/user-attachments/assets/817472c2-9d74-4c69-babf-fc82d6e10a57)
