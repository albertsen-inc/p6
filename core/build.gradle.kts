plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // or whatever you're using
    }
}

dependencies {
    // put shared dependencies here if needed
}