plugins {
    id("org.jetbrains.intellij") version "1.17.4" // latest
    kotlin("jvm") version "1.9.23"
}

repositories {
    mavenCentral()
}


group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()
val target = project.findProperty("target") ?: "ic"

intellij {
    pluginName.set(providers.gradleProperty("pluginName"))

    when (target) {
        "ic" -> {
            version.set(providers.gradleProperty("ideaPlatformVersion"))
            type.set(providers.gradleProperty("ideaPlatformType"))
        }
        "ai" -> {
            version.set(providers.gradleProperty("asPlatformVersion"))
            type.set(providers.gradleProperty("asPlatformType"))
        }
    }
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
        when (target) {
            "ic" -> {
                sinceBuild.set(providers.gradleProperty("ideaSinceBuild"))
                untilBuild.set(providers.gradleProperty("ideaUntilBuild"))
            }
            "ai" -> {
                sinceBuild.set(providers.gradleProperty("asSinceBuild"))
                untilBuild.set(providers.gradleProperty("asUntilBuild"))
            }
        }
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}
