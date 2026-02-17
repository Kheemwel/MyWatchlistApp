# My Watchlist App 🎬✨

A super simple and friendly app I built to help me (and maybe you!) keep track of all the movies and series I want to watch. No more messy notes or forgetting that one show someone recommended! 🍿😊

### Features

- ⭐ **Track Everything**: Easily add movies and series to your list.
- 🚀 **Stay Organized**: Categorize your watchlist by genre, country, and status.
- 🔍 **Smart Search**: Quickly find what you're looking for with the search bar.
- 🎯 **Filters**: Use filters to drill down and decide what to watch tonight.
- 📱 **Modern Design**: A clean and bouncy Material 3 UI that feels great to use.
- 💾 **Offline First**: Everything is saved on your phone, so you don't need internet!

### Tech Stack

- **Kotlin**: The main language used for the app.
- **Jetpack Compose**: For the modern, reactive UI.
- **Material 3**: For those sleek and clean Google designs.
- **Room Database**: To keep all your movies and series saved locally.
- **Hilt**: To keep the code clean and manageable.
- **Navigation Compose**: To move between screens easily.

### Project Structure

```text
MyWatchlistApp/
├── app/
│   ├── src/main/java/com/kheemwel/mywatchlist/
│   │   ├── data/          # Where the database and storage live 💾
│   │   ├── domain/        # The "brains" - models like Movie and Series 🧠
│   │   └── presentation/  # All the UI screens and design stuff 📱
│   └── build.gradle.kts   # Project configuration and libraries
└── README.md              # This friendly guide!
```

### Usage

1. **Adding**: Tap the big **(+) button** at the bottom right. Choose whether you're adding a Movie or a Series!
2. **Details**: Fill in the title, pick a genre, and set the status (like "Plan to Watch" or "Watching").
3. **Browsing**: Swipe between the **Movies** and **Series** tabs at the top.
4. **Filtering**: Tap the **Filter icon** to see only horror movies, or maybe shows from Japan!
5. **Searching**: Tap the **Search icon** to find that specific movie you added months ago.

### How to install / How to use

1. **Clone**: Download or clone this project to your computer.
2. **Open**: Launch **Android Studio** and open this project folder.
3. **Sync**: Wait a moment for Gradle to do its magic and sync everything.
4. **Run**: Plug in your phone or start an emulator, and hit the green **Play** button!
5. **Enjoy**: Start building your ultimate watchlist! 🎈
