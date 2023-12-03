# Write Away

Write away is a mobile note-taking app (developed for the Android operating system).
You can create, edit, delete and view your notes completely offline.
You can modify your list using some customizations presented on the app's settings screen:
- List view mode (list or grid);
- Font size;
- Ordering (creation order or editing order);

___

### Technologies:

This app was developed for the native Android operating system in the Kotlin language using a single activity design pattern.
Some of the main technologies used:
- [MVVM Architecture](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=pt-br);
- [Kotlin Language](https://kotlinlang.org/);
- [Room - SQLite database](https://developer.android.com/jetpack/androidx/releases/room?hl=pt-br);
- [Hilt - Dependency Injection](https://dagger.dev/hilt/);
- [Material Design](https://m3.material.io/);
- [Navigation](https://developer.android.com/guide/navigation?hl=pt-br);

___

### Repositories

Repositories are created by context and fetch the data (internal or external) requested by the view model. This is a good way to avoid duplication functions and repository classes.

___

### Data flow

The data flow must occur as follows:

[View] >> [ViewModel] >> [Repository]
[View] << [ViewModel] << [Repository]

In this case, the view makes a call to the view model, which in turn requests the data from the repository.
It is important to mention that there must be no logic in the view (everything must be exported to the view model) to facilitate the implementation of unit tests in the future.

___

### Future Implementation

I will list some features that I intend to add in the future to complement the app and carry out a first production release on the Google Play Console:

 - Note Password with user device password (add password block feature in notes);
 - Dark Mode (add default native dark mode to app);
 - [Unit tests](https://developer.android.com/training/testing/local-tests?hl=pt-br);