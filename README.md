<!-- Plugin description -->
# 🕌 Aqim As-Salat

**Aqim As-Salat** is a productivity and spiritual well-being plugin designed for Muslim developers using Android Studio or IntelliJ IDEA. It helps you stay mindful of your daily Islamic prayers (Salat) without disrupting your workflow or missing important prayer times.

This plugin provides:

🕰️ Daily Prayer Notifications – Get timely reminders for all five daily prayers (Fajr, Dhuhr, Asr, Maghrib, Isha) based on your local time and location.

🔔 Custom Alerts – Choose between silent alerts, pop-up messages, or sound notifications.

🕌 IDE Auto-Close (Optional) – Configure the plugin to automatically close or prompt to close the IDE at prayer times, helping you pause work and focus on worship.

🌍 Location-Based Timing – Automatically fetches your prayer times based on your city and timezone, with support for multiple calculation methods (e.g., Umm al-Qura, ISNA, MWL).

🧭 Qibla Direction (Planned) – Future updates will include an integrated Qibla compass.

⚙️ User-Friendly Settings – Customize prayer time offsets, calculation methods, notification styles, and more through an intuitive settings panel.
<!-- Plugin description end -->
---

## 📌 Features

- ✅ Automatically fetches daily prayer times using [Aladhan API](https://aladhan.com/prayer-times-api).
- ✅ Displays system tray icon with next prayer info.
- ✅ Optionally shuts down the IDE at the time of prayer.
- ✅ Configurable settings: City, Country, Calculation Method, Language.
- ✅ Supports localization: English & Arabic.

---

## ⚙️ Settings

Go to: `Settings → Aqim As-Salat`

- **City**: Choose from predefined cities like Cairo, Riyadh, Makkah, etc.
- **Country**: Choose from available countries.
- **Calculation Method**: ISNA, Egyptian, MWL.
- **Language**: English or Arabic.
- **Auto Shutdown**: Enable or disable IDE shutdown at prayer time.

All settings are saved automatically using `PrayerSettingsState`.

---

## 🖼️ Tray Icon

The plugin adds a system tray icon with options:
- View next prayer time
- Exit IDE manually

---

## 🌐 Localization

The plugin supports:
- 🇬🇧 English (default)
- 🇸🇦 Arabic (العربية)

Localization is applied in all dialogs, tray tooltips, and settings.

---

## 🔧 Requirements

- Android Studio / IntelliJ IDEA 2022.3+
- Internet connection (to fetch prayer times)
- JDK 17+

---
## 📦 src
├── main
│   ├── kotlin
│   │   └── com
│   │       └── ilaasalaty
│   │           ├── api
│   │           │   ├── PrayerApiClient.kt
│   │           │   └── PrayerTimesApi.kt
│   │           │   └── PrayerTimesResponse.kt
│   │           │
│   │           ├── core
│   │           │   └── PrayerTimeManager.kt
│   │           │
│   │           ├── tray
│   │           │   └── TrayIconManager.kt
│   │           │
│   │           ├── settings
│   │           │   ├── PrayerSettingsConfigurable.kt
│   │           │   └── PrayerSettingsState.kt
│   │           │
│   │           └── lang
│   │               └── Messages.kt
│   │
│   └── resources
│       ├── META-INF
│       │   └── plugin.xml
│       │
│       ├── messages.properties              # English (default)
│       ├── messages_ar.properties           # Arabic
│       └── icons
│           └── icon.png


## 🛠️ Build & Install

1. Open the project in IntelliJ IDEA.
2. Use **Gradle Plugin Dev** or `buildPlugin` task.
3. Install `.zip` from `Plugins → Install Plugin from Disk`.


## 🙏 Contribution

Feel free to open issues or pull requests for ideas or improvements.

---

## 📜 License

MIT License

