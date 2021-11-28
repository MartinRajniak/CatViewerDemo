# CatViewerDemo
![Check](https://github.com/MartinRajniak/CatViewerDemo/actions/workflows/push.yml/badge.svg)

Android demo                                                          |  iOS demo
:--------------------------------------------------------------------:|:-------------------------------------------------------------:
<img src="readme/android_demo.gif" alt="Android Demo" width="300" />  |  <img src="readme/ios_demo.gif" alt="iOS Demo" width="300" />



Kotlin Multiplatform Mobile demo for Android and iOS. 
App for viewing Cat pictures from [Cats API](https://thecatapi.com).

This sample showcases:
* [Kotlin Multiplatform Mobile](https://kotlinlang.org/docs/kmm-overview.html)
* [Android Architecture](https://developer.android.com/jetpack/guide)
* Shared state management with [Flow](https://kotlinlang.org/docs/flow.html)
* Networking with [Ktor](https://ktor.io)
* Persisting state with [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings)
* Android UI with [Jetpack Compose](https://developer.android.com/jetpack/compose)
* iOS UI with [SwiftUI](https://developer.apple.com/xcode/swiftui/)
* Android image (GIF) loading with [Coil](https://coil-kt.github.io/coil/)
* UI tests
* Unit tests

## Usage
I leave here some notes that might help anyone who would like to build it themselves.

### Cats API key
The project is using a property named `catsApiKey` to retrieve the key for the API.

You can provide it through `gradle.properties`, for example.

### Kotlin Multiplatform Mobile setup
You should follow [KMM setup](https://kotlinlang.org/docs/kmm-setup.html) 
to build the application, both for Android and iOS.

## Overview
The goal of the exercise was to build Kotlin Multiplatform Mobile applications for Android and iOS,
with only UI written in platform-specific code (ComposeUI and SwiftUI).

Everything from ViewModel to Repository and data sources is shared.

## Architecture
Architecture follows the standard blueprint proposed in
[Guide to app architecture](https://developer.android.com/jetpack/guide).

### Shared module
The shared module consists of Kotlin Multiplatform code that I use to share business and presentation logic
between Android and iOS.

#### ViewModel
<b>CatsViewModel</b> is the class that UI platform-specific code interacts with.
It prepares cats data displayed in the UI. It also listens to the actions that happen in UI.

It extends <b>SharedViewModel</b> that provides shared scope for coroutine work.
I took inspiration from [MOKO MVVM library](https://github.com/icerockdev/moko-mvvm),
which I plan to use instead once there is more time.

#### Repository
<b>CatsStore</b> is responsible for providing Cats model data.
It fetches data from <b>CatsApi</b> and <SettingsStorage> to map it 
to model data used in application.

In the future, I might use the Mapper pattern for mapping data from external systems to domain models.

Data from <b>CatsApi</b> is intentionally not persisted,
to provide up-to-date results.

#### CatsApi
<b>CatsApi</b> uses Ktor to fetch data from [Cats API](https://thecatapi.com).

#### SettingsStorage
<b>SettingsStorage</b> provides platform specific implementations for key value storage.
On Android it is [Datastore](https://developer.android.com/topic/libraries/architecture/datastore).

#### CatViewerServiceLocator
All dependencies are provided by <b>CatViewerServiceLocator</b>.
For such a small application, it perfectly serves its purpose.

For larger applications, I would recommend 
[Hilt (DI library)](https://developer.android.com/training/dependency-injection/hilt-android).

### Android platform-specific UI

<b>MainActivity</b> is a single activity in the application
as the only application entry point. Even with more screens,
I would still recommend using single activity
as screens can be represented as composable functions now.

<b>CatsUI</b> file
and composable function currently represent the only screen in the application.
I divided the file into multiple functions for clarity.

### iOS platform specific UI

<b>CatsView</b> is single screen displaying list of Cats.
Cats are represented by <b>CatItem</b> view. For filtering,
<CatsFilter> view has been created and is displayed as a sheet.

<b>CatsStore</b> serves as adapter between Kotlin Flow world and SwiftUI.

### Tests
Shared code has one test file with a bunch of basic unit tests.
You can find them in <b>CatsTest</b>.

I test the shared code through the ViewModel interface and
I use Fakes for external systems like networking and IO.

It allows us to test most of the business logic from a single interface.
Once I add more logic to tested objects, I might need to write more isolated tests.

There is also one UI instrumentation test in the Android application module. 
It runs with dummy ComponentActivity provided by the test library.
It allows us to test the UI in isolation.

## Special thanks
This project was way simpler thanks to all the resources,
libraries and projects I was able to find on this topic.

### CatsAPI
First and foremost, thanks to the people that created and maintained
a free public service API that I could use to showcase KMM.

### John O'Reilly
His [repositories](https://github.com/joreilly) that showcase Kotlin Multiplatform were crucial to my work. 

Especially, [repository](https://github.com/joreilly/MortyComposeKMM) that demonstrates paging.
I could not use the same multiplatform paging library (time constraints),
but I  want to try it in the future.

### Touchlab
People at [Touchlab](https://touchlab.co) and all the resources they provide publicly
were especially helpful for my understanding of how Kotlin native threading works.

### Jetbrains and community
During the project, I was using Apple M1 Silicon and with it arm64 simulators. 
Support in libraries is getting better, but the community on #kotlin-lang Slack channel,
was extremely helpful when I was fighting with this.

### Others
Special thanks also to all people that share their solutions around Kotlin Multiplatform.
It is hard to find any up-to-date resources, so every single one of them counts. 

Thanks to all the Kotlin Multiplatform libraries out there.
I used:
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings)
- [Kotlin Multiplatform UUID](https://github.com/benasher44/uuid/)
- [BuildKonfig](https://github.com/yshrsmz/BuildKonfig)

## TODO
Some things were left undone.

### GIFs
[Coil](https://coil-kt.github.io/coil/) on Android helps us with displaying GIFs,
but this has not yet been implemented on iOS.

### Caching
[Coil](https://coil-kt.github.io/coil/) on Android is already caching images on disk,
but there is no caching of images on iOS.

### Error Handling
There is almost no error handling implemented at the moment.

It would be nice to show errors when image loading fails,
there is a network issue, etc. 

### Full-screen view
It would be nice to be able to display images on full screen.

I could showcase [Navigation with Compose](https://developer.android.com/jetpack/compose/navigation),
since I would have another screen.

### Continuous Integration
I believe that even when developing a project alone,
Continuous Integration service can help.

I would at least know that the build, tests and static analysis run with every new commit.

Another advantage is that I can be sure that someone else (another machine) can build it.

GitHub should be able to provide this functionality. 

### UI improvements
There is a lot of work on UI.

One thing that stands out is filtering,
I don't think it is intuitive at this point.
But it is there primarily to show work with Flows,
so it is OK for now.

### Bugs
There are probably a lot of bugs, but one stands out.

Pagination, loading of new images is working nicely.
But only when the user scrolls to the bottom of the list.

But new images are not loaded when the user gets to the bottom
by filtering.
