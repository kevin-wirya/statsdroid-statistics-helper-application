# 📊 StatsDroid — Probability & Statistics Helper Application

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=for-the-badge&logo=android)
![Android SDK](https://img.shields.io/badge/Android%20SDK-36.1-green?style=for-the-badge&logo=android)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**StatsDroid** is a modern, native Android application crafted to simplify, calculate, and interactively visualize fundamental concepts in Probability and Statistics. Built using **Jetpack Compose**, **Material 3**, and **Clean Architecture**, StatsDroid bridges the gap between theoretical statistics and practical application for informatics students, researchers, and engineers.

---

## ✨ Features

### 1. 📈 Distribution Lookup & Calculators (`LookupScreen`)
- **Supported Distributions**: Binomial, Poisson, Uniform, Normal (Gaussian), Student's t, Chi-Square ($\chi^2$), and F-Distribution.
- **Interactive Computation**: Calculate cumulative probabilities ($P(X \le x)$, $P(X > x)$), probability mass/density functions ($P(X = x)$), $z$-scores, and critical values.
- **Dynamic Visualizations**: Real-time distribution curve rendering and parameter sliders.

### 2. 🧪 Hypothesis Testing Engine (`HypothesisScreen`)
- **Comprehensive Tests**: One-sample & Two-sample Z-Test, T-Test, Proportion Test, Chi-Square Goodness-of-Fit / Independence Test, and One-Way ANOVA.
- **Automated Results**: Computes exact Test Statistics ($z$, $t$, $\chi^2$, $F$), $p$-values, critical bounds, and presents unambiguous statistical decisions (*Reject $H_0$* or *Fail to Reject $H_0$*).

### 3. 🎲 Central Limit Theorem (CLT) Interactive Simulator (`CltScreen`)
- **Live Empirical Simulation**: Simulates drawing samples from uniform or exponential distributions.
- **Configurable Parameters**: Quick sample count selection ($M = 100, 500, 1000, 5000$) and variable sample size ($N$).
- **Live Canvas Visualizer**: Renders empirical sample mean histograms overlaid against theoretical Gaussian curves to demonstrate convergence in real-time.

### 4. 📚 Hybrid Reference & Course Materials (`ReferenceScreen`)
- **Google Drive Slides**: Direct access to pre-loaded lecture presentation slides.
- **Live Web Scraper**: Integrated Jsoup scraper fetching real-time course materials directly from Dr. Ir. Rinaldi Munir's Probability & Statistics portal.
- **Embedded In-App Viewer**: Seamless PDF and slide viewer powered by an optimized WebView.

### 5. 👤 About Developer & IRK Assistant Vision (`AboutScreen`)
- **Developer Biography**
- **Motivation**
- **Vision & Mission**
- **Socials**

---

## 🛠️ Technology Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with **Material 3 Design System**
- **Typography**: Custom offline-bundled [Lato](https://fonts.google.com/specimen/Lato) font family
- **Architecture Pattern**: **MVVM (Model-View-ViewModel)** + **Clean Architecture**
  - **UiState / Flow**: Asynchronous state management via Kotlin `StateFlow` and `Coroutines`.
  - **Dependency Injection**: [Hilt / Dagger](https://dagger.dev/hilt/) for modular, testable dependency management.
  - **Web Scraping**: [Jsoup](https://jsoup.org/) HTML parser for dynamic resource scraping.

---

## 📁 Project Structure

```
statsdroid-statistics-helper-application/
├── app/src/main/java/com/kevin/statsdroid/
│   ├── data/
│   │   └── repository/        # Repository implementations & data providers
│   ├── domain/
│   │   ├── calculators/       # Core mathematical & statistical computation logic
│   │   └── model/             # Domain entities & data models
│   ├── ui/
│   │   ├── components/        # Reusable UI components (Histogram, Navigation Bar, Cards)
│   │   ├── navigation/        # Jetpack Navigation routing & bottom navigation setup
│   │   ├── screens/           # Composable Screens & ViewModels
│   │   │   ├── LookupScreen.kt / LookupViewModel.kt
│   │   │   ├── HypothesisScreen.kt / HypothesisViewModel.kt
│   │   │   ├── CltScreen.kt / CltViewModel.kt
│   │   │   ├── ReferenceScreen.kt / ReferenceViewModel.kt
│   │   │   └── AboutScreen.kt
│   │   └── theme/             # Material3 Color Palette, Theme, Typography
│   └── MainActivity.kt        # Main Application Entry Point
└── res/
    └── font/                  # Bundled Lato TTF Font Resources
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug / Koala or newer
- **JDK**: Java 17 or higher
- **Android SDK**: Compile SDK 36 (Minimum SDK 24)

### Building & Running

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/kevin-wirya/Seleksi-IRK.git
   cd Seleksi-IRK/statsdroid-statistics-helper-application
   ```

2. **Build the Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```

3. **Install on Device / Emulator**:
   ```bash
   ./gradlew installDebug
   ```

---

## 📷 Application Screenshots

| Distribution Lookup | Hypothesis Testing | CLT Simulation | Reference Materials |
| :---: | :---: | :---: | :---: |
| *Lookup Screen* | *Hypothesis Screen* | *CLT Histogram Visualizer* | *Hybrid Course Viewer* |

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).
MIT License

---