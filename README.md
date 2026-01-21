# Band-AId

Band-AId is a local-only Android application for medication reminders and intake tracking. It is a single-module project using XML layouts with ViewBinding and a single Activity entry point.

## Scope and Non-Goals
Scope:
- Local medication schedules and intake logs
- Calendar-based review of expected and recorded doses
- Subjective daily notes tied to dates

Non-goals:
- Cloud synchronization or external services
- Background execution or automated reminders at this stage
- Medical advice or clinical decision making

## Architecture Reference
- High-level functional and domain design is documented in `docs/` with numbered files for guided reading.

## Build Prerequisites
- JDK 17 installed
- Android SDK installed and configured
- Windows 10/11 is the primary development target
- Gradle Wrapper is included and required

Current build status:
- `.\gradlew assembleDebug` fails until the Android SDK is linked via `ANDROID_HOME` or `local.properties`.

## Commit Readiness Checklist
- No secrets are present in the repository
- No SDK paths are committed
- No external services are configured
- Build requires a local Android SDK

## Disclaimer
- This project does not provide medical advice and does not replace professional guidance.
