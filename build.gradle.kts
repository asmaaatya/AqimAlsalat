plugins {
    id("org.jetbrains.intellij") version "1.17.4" // latest as of 2025
    kotlin("jvm") version "1.9.23"
}

group = "com.asmaaatya"
version = "0.0.6"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.2") // choose your target IDEA version
    type.set("IC") // IC = IntelliJ IDEA Community, IU = Ultimate
    plugins.set(listOf()) // add needed built-in plugins if any
}
dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
tasks {

        patchPluginXml {
            sinceBuild.set("242") // 2024.2 major version
            untilBuild.set("242.*") // allow all 2024.2 minor updates
        }


    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}
