# Contributing to ShareDrop

First off, thanks for taking the time to contribute. Every bit helps, whether it's fixing a bug, adding a feature, or just improving the docs.

---

## Before you start

If you're planning something big, open an issue first and describe what you want to do. This avoids the situation where you spend hours on something that's already being worked on or goes in a different direction than the project.

For small fixes like typos, broken links, or obvious bugs, just go ahead and open a pull request directly.

---

## Setting up the project

You'll need:

- Android Studio (latest version)
- Xcode 15 or later (for iOS or macOS builds)
- JDK 17 or higher
- Kotlin Multiplatform plugin installed in Android Studio

Clone the repo:

```bash
git clone https://github.com/nitish058/sharedrop.git
cd sharedrop
```

Open in Android Studio and wait for Gradle to sync. Once it's done, you're good to go.

To run on Desktop, create a Gradle run configuration with:

```
hotRunJvm -DmainClass=org.nitish.project.sharedrop.MainKt
```

To run on Android, connect a device or start an emulator and hit Run, or:

```bash
./gradlew assembleDebug
adb install -r composeApp/build/outputs/apk/debug/composeApp-debug.apk
```

---

## How the code is organized

The project uses Kotlin's `expect/actual` pattern. Here's the short version:

- `commonMain` — shared code that runs on every platform. UI lives here. Interfaces for platform features live here.
- `androidMain` — Android-specific implementations
- `jvmMain` — Desktop implementations (Mac, Windows, Linux)
- `iosMain` — iOS implementations (work in progress)
- `jsMain` and `wasmJsMain` — Web implementations (work in progress)

If you're adding a new feature that needs platform-specific behavior (like accessing the file system differently on each OS), define the interface in `commonMain` using `expect`, then implement it in each platform folder using `actual`.

If the feature is purely UI or shared logic with no platform differences, it goes entirely in `commonMain`.

---

## Good places to start

These are well-scoped tasks that don't require deep knowledge of the whole codebase:

- Transfer progress indicator — the bytes are already being streamed over TCP, it just needs a UI counter
- Dark mode support
- Transfer history screen — show a list of files sent and received in the current session
- iOS discovery implementation using the Network framework or Bonjour
- Notification when a file is received in the background
- Send multiple files at once
- Let the user choose where received files are saved

---

## Code style

- Follow standard Kotlin conventions
- Keep platform-specific code in the correct `*Main` source set, not in `commonMain`
- Remove debug `println` statements before submitting a PR
- Keep functions focused — if a function is doing too many things, split it up

---

## Submitting a pull request

1. Fork the repo
2. Create a branch with a descriptive name: `git checkout -b feature/transfer-progress`
3. Make your changes
4. Test on at least one platform — ideally the one most relevant to your change
5. Commit with a clear message that explains what and why, not just what: `git commit -m "feat: add transfer progress bar to file send flow"`
6. Push and open a pull request against the `main` branch
7. In the PR description, explain what you changed and why, and mention which platform(s) you tested on

---

## Reporting bugs

Open an issue and include:

- What you were doing
- What you expected to happen
- What actually happened
- Which platform (Android, Mac, Windows, Linux)
- Any relevant logs or error messages

---

## License

By contributing, you agree that your contributions will be licensed under the same MIT license that covers this project.
