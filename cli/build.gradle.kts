plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}


dependencies {
    implementation(project(":core"))
    implementation("info.picocli:picocli:4.7.7")
}

application {
    mainClass.set("com.albertsen.cli.Main")
}