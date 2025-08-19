
<!-- Plugin description -->
Aqim As-Salat is a spiritual productivity plugin for Muslim developers that provides prayer time notifications, IDE shutdown reminders, and location-based Salat schedules.
<!-- Plugin description end -->

---

## 📌 Features

- ✅ Automatically fetches daily prayer times using [Aladhan API](https://aladhan.com/prayer-times-api).
- ✅ Optionally shuts down the IDE at the time of prayer.
- ✅ Configurable settings: City, Country, Calculation Method, Language.

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
## 🔧 Requirements

- Android Studio / IntelliJ IDEA 2022.3+
- Internet connection (to fetch prayer times)
- JDK 17+

---
## 📦 src
\---src
    +---main
    |   +---kotlin
    |   |   \---com
    |   |       \---github
    |   |           \---asmaaatya
    |   |               \---aqimsalat
    |   |                   +---api
    |   |                   |       PrayerApiClient.kt
    |   |                   |       PrayerTimesApi.kt
    |   |                   |       PrayerTimesResponse.kt
    |   |                   |
    |   |                   +---core
    |   |                   |   |   PrayerTimeManager.kt
    |   |                   |   |
    |   |                   |   \---dialog
    |   |                   |           FocusModeDialog.kt
    |   |                   |
    |   |                   +---lang
    |   |                   |       MyBundle.kt
    |   |                   |
    |   |                   +---reminder
    |   |                   |       MyReminder.kt
    |   |                   |
    |   |                   +---services
    |   |                   |       MyProjectService.kt
    |   |                   |
    |   |                   +---setting
    |   |                   |       PrayerSettingsConfigurable.kt
    |   |                   |       PrayerSettingsState.kt
    |   |                   |
    |   |                   +---startup
    |   |                   |       MyProjectActivity.kt
    |   |                   |
    |   |                   \---tray
    |   |                           TrayIconManager.kt
    |   |
    |   \---resources
    |       +---icons
    |       |       icon.svg
    |       |
    |       +---messages
    |       |       MyBundle.properties
    |       |       MyBundle_ar.properties
    |       |
    |       +---META-INF
    |       |       plugin.xml
    |       |
    |       \---sounds
    |               main_sound.mp3
    |



## 🛠️ Build & Install

1. Open the project in IntelliJ IDEA.
2. Use **Gradle Plugin Dev** or `buildPlugin` task.
3. Install `.zip` from `Plugins → Install Plugin from Disk`.


## 🙏 Contribution

Feel free to open issues or pull requests for ideas or improvements.

---

## 📜 License

MIT License

