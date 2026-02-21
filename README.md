# Multitransfer Automation Script

This project contains a Kotlin script that automates interactions with multitransfer.ru using HtmlUnit.

## What the script does:
1. Navigates to multitransfer.ru
2. Selects Türkiye as the destination country
3. Enters 50000 in the specified input field
4. Clicks the "Выбрать банк Get Money Global" button

## Prerequisites

- Java 11 or higher
- Kotlin installed (for running the script directly)

## Running the script

To run the script directly:
```bash
kotlin multitransfer.kts
```

Alternatively, you can use Gradle to manage dependencies:
```bash
./gradlew run
```

Note: The build.gradle.kts file includes HtmlUnit dependency which is required for the script to work.

## Dependencies

The script uses HtmlUnit library (version 2.70.0) for web automation tasks.