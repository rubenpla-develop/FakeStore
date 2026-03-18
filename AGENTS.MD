# AGENTS.md

## Project overview
This repository contains an Android application built with Kotlin and Jetpack Compose.

The project follows a modular architecture and aims to keep a clean separation of concerns across app, feature, domain, data, and shared UI layers.

Primary goals:
- Keep the codebase maintainable and easy to review
- Preserve clear architectural boundaries
- Prefer small, safe, incremental changes
- Avoid introducing unnecessary complexity

## Tech stack
- Kotlin
- Jetpack Compose
- Android Jetpack
- Gradle Kotlin DSL
- Version catalog with `libs.versions.toml`
- CI/CD already configured in the repository

## General working rules
- Always understand the existing structure before proposing changes
- Prefer adapting to the current patterns already used in the codebase
- Keep changes minimal and focused on the requested task
- Do not refactor unrelated code unless it is necessary to complete the task safely
- Do not introduce speculative improvements unless explicitly requested
- If a task requires assumptions, make conservative assumptions and state them clearly

## Architecture rules
- Respect the existing modularization
- Do not move responsibilities across layers without a strong reason
- UI logic belongs in presentation layer components
- Business logic belongs in use cases / domain layer
- Data access logic belongs in repositories / data layer
- Avoid placing business logic inside composables
- Keep composables as stateless as reasonably possible
- Prefer state hoisting when appropriate
- ViewModels should expose UI state clearly and predictably

## Module boundaries
- Do not create unnecessary dependencies between feature modules
- Shared code should only be extracted when it is genuinely reused or clearly reusable
- Reusable UI components belong in shared/core UI modules, not in feature modules
- Do not bypass domain abstractions just for convenience
- Respect package and namespace consistency across modules

## Compose rules
- Keep composables small, readable, and focused
- Prefer explicit parameters over hidden dependencies
- Add `@Preview` for reusable UI components when practical
- Avoid overly large route/screen files if logic can be split cleanly
- Prefer clear UI state models over scattered booleans
- Keep accessibility in mind when adding or changing UI elements

## Dependency rules
- Prefer the existing project stack over introducing new libraries
- Do not add a new dependency unless it is necessary for the requested task
- If a dependency must be added:
    - update the version catalog
    - use the existing Gradle conventions
    - explain briefly why it is needed
- Do not change plugin or library versions unless the task explicitly requires it

## Gradle and build rules
- Use existing Gradle conventions already present in the repo
- Do not modify CI/CD, Gradle wrappers, signing, secrets, or workflow files unless the task is specifically about them
- Do not rename modules, package names, or namespaces unless explicitly requested
- Do not change build logic just to make a local workaround pass unless the reason is documented

## Testing rules
- Every non-trivial logic change should be accompanied by tests or updates to existing tests
- Follow the project testing stack already in use
- Prefer focused unit tests over broad, fragile tests
- Update tests when behavior changes
- Do not remove tests unless they are invalid, obsolete, or explicitly requested to be removed
- If test coverage is not possible for a change, explain why

## Validation rules
Before considering a task complete, validate the affected scope whenever possible.

Prefer running:
- relevant unit tests for affected modules
- formatting checks
- lint checks if relevant
- build verification for impacted modules

If some command cannot be run, state it explicitly.

## Code style rules
- Follow the existing naming and style conventions in the repository
- Prefer readability over cleverness
- Avoid unnecessary abstractions
- Avoid large multi-purpose classes
- Keep functions focused and cohesive
- Prefer explicit and intention-revealing names
- Do not leave commented-out code behind
- Avoid TODOs unless they are explicitly requested or genuinely useful

## Git and change management
- Prefer changes that are easy to review in a pull request
- Keep file diffs as small as possible
- Do not touch unrelated files
- When editing code, preserve formatting and style consistency
- Do not rewrite working code only for personal preference

## Change approval policy
- Do not assume that code changes should be committed automatically
- By default, leave all applied changes uncommitted for developer review
- Do not create a commit unless the developer explicitly asks for it
- When the developer requests code changes but does not explicitly request a commit, apply the changes and stop before committing
- Prefer a review-first workflow: edit, validate, summarize, then wait for developer decision on commit

## Review expectations before commit
Before creating any commit, always:
- summarize the scope of the changes
- mention the affected modules and/or files
- mention any important assumptions
- mention validation performed
- mention any validation that could not be run

If the requested commit would include unrelated or suspicious changes:
- warn the developer
- exclude unrelated files when possible
- do not silently include extra changes

## Git behavior
- Treat commits as an explicit developer decision, not as the default final step
- Code modification and commit creation are separate actions
- Unless explicitly asked, stop after applying and validating the requested changes

## Commit policy
- Only create a commit when the developer explicitly requests it
- Never create multiple commits unless explicitly requested
- Never split changes into multiple commits based on personal preference
- Do not amend existing commits unless explicitly requested
- Do not rebase, squash, force-push, or rewrite git history unless explicitly requested
- Do not stage unrelated files
- Do not commit generated noise, secrets, credentials, local config, or environment-specific files

## Commit preparation workflow
When a commit is requested, follow this order:
1. inspect the modified scope
2. summarize what is about to be committed
3. present the proposed commit message
4. ensure the commit only contains relevant files
5. create the commit only if the developer explicitly requested the commit action

If the developer already explicitly asked to commit, you may proceed directly after summarizing the scope and preparing the message.

## Commit message format
Use the following structure for commit messages:

[TITLE]

[SHORT DESCRIPTION]

- [CHANGE 1]
- [CHANGE 2]
- [CHANGE 3]

## Commit title rules
- Keep the title concise, specific, and descriptive
- The title must describe the intent of the change, not just the files touched
- Avoid vague titles such as:
    - changes
    - fixes
    - update
    - misc
    - cleanup
- Prefer a conventional prefix when appropriate, such as:
    - feat:
    - fix:
    - refactor:
    - test:
    - docs:
    - build:
    - chore:

## Commit description rules
- The short description must explain the goal of the change in 1 or 2 sentences
- Focus on why the change was made and what behavior or structure was improved
- Do not repeat the title verbatim

## Commit bullet rules
- Use bullets to list the main changes included in the commit
- Keep bullets concrete and reviewable
- Prefer 3 to 5 bullets when practical
- Do not mention trivial formatting-only edits unless they are relevant to the purpose of the commit

## Commit safety rules
- Never commit secrets, tokens, signing files, credential files, keystores, or private keys
- Never commit failing code intentionally unless the developer explicitly requests a WIP commit
- If validation could not be completed, state it clearly before committing
- If the working tree contains unrelated changes, avoid including them automatically

## WIP commit policy
- Do not create WIP commits unless the developer explicitly requests them
- If a WIP commit is requested, mark it clearly in the title
- WIP commits must still include a meaningful description of the current state

## Safety and caution
- Never hardcode secrets, tokens, credentials, or environment-specific sensitive values
- Never modify production/release behavior casually
- Be cautious with persistence, concurrency, navigation, and dependency injection changes
- Call out any risky change explicitly

## Response expectations
When proposing or applying changes:
- briefly explain what was changed
- mention which modules/files were affected
- mention important assumptions
- mention validation performed
- call out trade-offs if relevant

## Task priority order
When multiple valid solutions exist, prefer this order:
1. correctness
2. architectural consistency
3. readability
4. testability
5. minimal surface area of change
6. implementation speed

## Project-specific guidance
- Respect the current Android modular setup
- Respect existing MVI / presentation-state patterns if already established in the project
- Keep navigation changes isolated
- Keep feature-specific code inside its feature module unless clearly shared
- Use the current dependency injection approach already present in the codebase
- Preserve CI/CD compatibility with every change

## What to avoid
- Unrequested large refactors
- Unnecessary dependency additions
- Cross-module shortcuts
- Hidden behavior changes
- Mixing UI, domain, and data responsibilities
- Fixing unrelated code while touching a file

## Examples

Example 1:
fix(profile): restore state correctly after navigation

Prevent the profile screen from staying in a loading state when returning
from other tabs after initial data has already been loaded.

- add loaded-once guard in profile state handling
- preserve previously loaded UI state across navigation
- update related unit tests for repeated screen entry

Example 2:
test(products): add viewmodel coverage for success and error states

Expand test coverage for ProductsViewModel to verify expected state emissions
for successful data loading and repository failure cases.

- add success state test coverage
- add error state test coverage
- improve mock setup readability in test suite

## Project-specific module structure
This project follows a modular Android architecture.

Expected modules and responsibilities:

- `:app`
    - Application entry point
    - App-level navigation wiring
    - Dependency injection bootstrap
    - No feature business logic

- `:core:ui`
    - Shared reusable UI components
    - Theme, previews, design primitives, and common UI helpers
    - No feature-specific business logic

- `:core:domain`
    - Domain models
    - Use cases
    - Repository contracts
    - Business rules and abstractions

- `:core:data`
    - Repository implementations
    - Data sources
    - DTO/entity mapping
    - Networking and persistence details

- `:feature:products`
    - Products screens, routes, ViewModels, UI state, and feature interactions

- `:feature:favorites`
    - Favorites screens, routes, ViewModels, UI state, and feature interactions

- `:feature:profile`
    - Profile screens, routes, ViewModels, UI state, and feature interactions

## Module boundary rules
- `:app` may orchestrate app setup and navigation, but should not absorb feature logic
- Feature modules must not depend on each other unless explicitly required by the architecture
- Shared code should only be extracted when it is genuinely reusable
- Domain contracts belong in `:core:domain`
- Data implementations belong in `:core:data`
- Shared composables and theme primitives belong in `:core:ui`
- Screen-specific UI should remain inside its feature module

## Layer responsibilities
- Composables render state and emit UI events
- ViewModels manage UI state and coordinate use cases
- Use cases contain business intent
- Repositories abstract data access
- Mappers keep conversion logic explicit and testable

## Placement guidance
When adding new code:
- prefer feature-local placement first
- extract to shared/core only after clear reuse or architectural need
- avoid introducing cross-feature dependencies
- do not place networking or persistence logic in presentation code

## Project-specific anti-patterns to avoid
- repository logic inside ViewModels
- API/service calls directly from presentation
- feature-specific code inside shared/core modules
- unrelated miscellaneous logic inside `:app`
- direct coupling between products, favorites, and profile features

