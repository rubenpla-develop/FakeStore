# FakeStore (Tech Test) — Android (Jetpack Compose)

Android project built on top of the public FakeStore API, using **Jetpack Compose** and a **feature + core modularization** approach. The goal is to provide a clear entry point so a reviewer can quickly understand where things live and how the app flows.

---

## What the app does

The app exposes a main flow with a bottom navigation containing 3 sections:

- **Products**: product list fetched from the API.
- **Favorites**: favorites list (similar list experience).
- **Profile**: a simple profile/status screen.
- 
---

## Architecture

The project follows a ViewModel-driven presentation approach with a **MVI / MVVM hybrid** flavor:

- **UI (Compose)**: renders an observable `uiState` and emits user events.
- **ViewModel**: receives events, updates state, and calls use cases.
- **Use Cases**: encapsulate domain actions (e.g., fetch products).
- **Repository**: abstracts data access (remote now, easy to extend to local later).
- **Network models + Mappers**: keep API DTOs isolated from domain/UI models.

### Typical flow
1. UI triggers an event (e.g., *Load* or *Retry*).
2. `ViewModel` calls a `UseCase`.
3. `UseCase` invokes a `Repository`.
4. `Repository` calls the network layer.
5. Responses are mapped into domain/UI models.
6. `ViewModel` publishes a new state and Compose recomposes.

---

## Modularization

Modularization is designed to:
- Separate responsibilities
- Improve scalability and build performance
- Reduce coupling between features

### Main modules

**App**
- `:app`: entry point, main navigation, and feature composition.

**Features**
- `:feature-products`: Products UI + ViewModel.
- `:feature-favorites`: Favorites UI + ViewModel.
- `:feature-profile`: Profile UI + ViewModel.

**Core (shared)**
- `:core-ui`: reusable UI components (e.g., list item, theme, etc.).
- `:core-model`: shared models.
- `:core-domain`: repository contracts + use cases (where applicable).
- `:core-network`: Retrofit/OkHttp setup + services.
- `:core-coroutines`: coroutine utilities/dispatchers (where applicable).
- `:core-di`: dependency injection modules (Hilt).

> General rule: **features depend on core**, and `app` composes features.  
> Avoid feature-to-feature dependencies.

---

## UI and navigation

Navigation is structured around a **BottomBar** with 3 destinations (Products, Favorites, Profile).  
`MainActivity` hosts the `NavHost` and bottom bar, while each feature provides its own entry screen.

- **Navigation**: `androidx.navigation.compose`
- **UI**: Material 3 + Compose
- **Strings**: user-facing text moved to module-level `strings.xml` (better i18n and accessibility)

---

## HomeRoute / Main container

The “Home” of the project can be understood as the bottom-navigation container:

- Defines bottom bar destinations
- Keeps the selected tab state
- Routes to each feature via a `NavHost`

Each feature owns its main screen and ViewModel, keeping `:app` as thin as possible.

---

## Networking

The networking layer uses:
- **Retrofit** + **OkHttp**
- An interceptor ready to extend (common headers, logging, etc.)
- DTOs and mappers to prevent leaking API models into UI

---

## Dependency Injection

**Hilt** is used to:
- Provide Retrofit/OkHttp/services
- Provide repositories and use cases
- Inject ViewModels in features

The intent is to keep each module as self-contained as possible and place dependencies in the right layer (`core-network`, `core-di`, etc.).

---

## Testing

Main focus is unit testing with:
- **JUnit 5**
- **MockK**
- **Kotest assertions**
- `kotlinx.coroutines.test` for coroutine testing

> UI/Compose tests can be added as a final stage if required, but the focus here is on unit-testability of presentation and domain layers.

---

## How to run

1. Open the project in Android Studio
2. Gradle sync
3. Run `:app` (debug)
