## Project Overview

This repository contains an Android application built with Kotlin and Jetpack Compose.

The project follows a modular architecture with clear separation between application, presentation, domain, data, and shared UI layers.

Primary goals:

- Maintain architectural consistency
- Keep changes small and reviewable
- Preserve clear module boundaries
- Avoid unnecessary complexity
- Prioritize correctness and maintainability

---

## Tech Stack

- Kotlin
- Jetpack Compose
- Android Jetpack
- Gradle Kotlin DSL
- Version Catalog (`libs.versions.toml`)
- CI/CD configured in repository

---

## Documentation

- Read relevant README and project documentation before implementing changes
- Follow documented project conventions when available
- Update documentation when behavior, APIs, architecture, or developer workflows change

---

## Development Principles

- Understand the existing implementation before proposing changes
- Prefer existing project patterns over introducing new approaches
- Keep changes focused on the requested scope
- Avoid refactoring unrelated code unless required for correctness or safety
- Make conservative assumptions and state them when necessary
- Prefer explicit and intention-revealing code over custom abstractions
- Keep functions, classes, and files focused on a single responsibility
- Split files when responsibilities become difficult to understand, navigate, or test
- Avoid unnecessary abstractions and indirection
- Follow existing naming and style conventions
- Do not leave commented-out code behind
- Avoid TODOs unless explicitly requested or genuinely useful

---

## Architecture

- Respect the existing modular architecture
- Do not move responsibilities across layers without a strong reason
- UI logic belongs in presentation components
- Business logic belongs in use cases and domain layer
- Data access belongs in repositories and data layer
- Keep conversion and mapping logic explicit and testable

### Layer Responsibilities

- Composables render state and emit UI events
- ViewModels manage UI state and coordinate use cases
- Use cases express business intent
- Repositories abstract data access
- Mappers handle model transformations

---

## Module Boundaries

- Respect existing modularization
- Feature modules should remain independent
- Do not introduce unnecessary cross-feature dependencies
- Shared code should only be extracted when genuinely reusable
- Reusable UI components belong in shared/core UI modules
- Domain contracts belong in `:domain`
- Data implementations belong in `:data`
- Screen-specific code belongs inside its feature module
- Maintain package and namespace consistency

---

## Compose Guidelines

- Avoid business logic inside composables
- Keep composables focused and easy to understand
- Prefer stateless composables when practical
- Prefer state hoisting when appropriate
- Prefer explicit parameters over hidden dependencies
- Prefer clear UI state models over multiple boolean flags
- Prefer immutable UI state models
- Keep state ownership explicit
- Avoid storing business state inside composables
- Split composables when responsibilities become difficult to understand or test
- Add `@Preview` for reusable UI components when practical
- Keep accessibility considerations in mind

---

## Coroutines and Flows

- Avoid `GlobalScope`
- Prefer structured concurrency
- Inject dispatchers when appropriate
- Expose immutable flows from ViewModels
- Avoid exposing mutable state holders publicly
- Keep coroutine scope ownership explicit

---

## Dependency Rules

- Prefer the existing project stack
- Do not introduce new dependencies unless necessary

If a dependency is added:

- Update the version catalog
- Follow existing Gradle conventions
- Briefly justify the addition
- Prefer actively maintained libraries
- Prefer widely adopted solutions
- Avoid introducing dependencies with unclear maintenance status

Do not upgrade library or plugin versions unless required by the task.

---

## Gradle and Build Rules

- Follow existing Gradle conventions
- Do not modify CI/CD, signing, secrets, workflows, or build infrastructure unless explicitly requested
- Do not rename modules, packages, or namespaces unless explicitly requested
- Avoid build-system workarounds that are not properly justified

---

## Testing

- Every non-trivial logic change should include appropriate test coverage
- Bug fixes should include regression tests when feasible
- Follow the testing stack already used by the project
- Prefer focused and maintainable tests
- Update tests when behavior changes
- Do not remove tests unless they are invalid, obsolete, or explicitly requested
- If test coverage is not feasible, explain why

---

## Validation

Before considering a task complete, validate the affected scope whenever possible.

Prefer running:

- Relevant unit tests
- Formatting checks
- Lint checks
- Build verification for impacted modules

Additionally:

- Prefer validating complete user flows when practical, not only compilation success

If validation cannot be performed, state it explicitly.

---

## Performance

- Avoid unnecessary recompositions
- Avoid premature optimization
- Consider performance impact when introducing shared state or expensive UI operations
- Prefer simple solutions before introducing complexity for performance reasons

---

## Git and Commit Rules

- Keep diffs small and reviewable
- Do not modify unrelated files
- Preserve existing formatting and conventions
- Code changes and commits are separate actions

Commits require explicit developer approval.

When a commit is requested:

- Summarize the scope of changes
- Mention affected modules/files
- Mention validation performed
- Present the proposed commit message

Use Conventional Commits when appropriate:

- feat:
- fix:
- refactor:
- test:
- docs:
- build:
- chore:

Never:

- Amend commits unless requested
- Rebase unless requested
- Force push unless requested
- Include unrelated files in commits
- Commit secrets, credentials, or environment-specific files

---

## Safety

- Never hardcode secrets, tokens, credentials, or sensitive values
- Be cautious with persistence, concurrency, navigation, and dependency injection changes
- Call out risky changes explicitly
- Avoid hidden behavior changes

---

## Response Expectations

When proposing or applying changes:

- Briefly explain what changed
- Mention affected modules/files
- Mention important assumptions
- Mention validation performed
- Call out relevant trade-offs

---

## Task Priority Order

When multiple valid solutions exist, prefer:

1. Correctness
2. Architectural consistency
3. Readability
4. Testability
5. Minimal change surface
6. Implementation speed

---

## Project Structure

### :app

- Application entry point
- Navigation wiring
- Dependency injection bootstrap
- No feature business logic

### :core

- Shared UI components
- Theme and design primitives
- Common UI helpers

### :domain

- Domain models
- Use cases
- Repository contracts
- Business rules

### :data

- Repository implementations
- Data sources
- DTO/entity mapping
- Networking and persistence details

### :feature:<feature-name>

- Feature-specific UI
- ViewModels
- State management
- Feature interactions
- Feature-specific presentation logic

---

## Project-Specific Guidance

- Respect the current Android modular setup
- Respect existing MVI and presentation-state patterns
- Keep navigation changes isolated
- Keep feature-specific code inside its feature module
- Follow the project's dependency injection approach
- Preserve CI/CD compatibility

---

## Anti-Patterns to Avoid

- Business logic inside composables
- Repository logic inside ViewModels
- API calls directly from presentation layer
- Feature-specific code inside shared/core modules
- Cross-feature shortcuts and coupling
- Unrequested large refactors
- Unnecessary dependencies
- Hidden behavior changes
- Mixing presentation, domain, and data responsibilities
- Fixing unrelated code while touching a file