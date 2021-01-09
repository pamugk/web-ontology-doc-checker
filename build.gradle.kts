plugins {
    kotlin("js") version "1.4.20"
    kotlin("plugin.serialization") version "1.4.20"
}

group = "ru.psu"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
}

dependencies {
    val kotlinVersion = "1.4.20"
    val kotlinJsVersion = "pre.129-kotlin-$kotlinVersion"

    implementation(kotlin("stdlib-js", kotlinVersion))
    implementation("org.jetbrains", "kotlin-styled", "5.2.0-$kotlinJsVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    implementation("com.ccfraser.muirwik:muirwik-components:0.6.3")
}

kotlin {
    js(LEGACY) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
        }
    }
}