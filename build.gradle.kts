plugins {
    kotlin("js") version "1.4.30"
    kotlin("plugin.serialization") version "1.4.30"
}

group = "ru.psu"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
}

dependencies {
    val kotlinVersion = "1.4.30"
    val kotlinJsVersion = "pre.148-kotlin-$kotlinVersion"

    implementation(kotlin("stdlib-js", kotlinVersion))
    implementation("org.jetbrains:kotlin-styled:5.2.1-$kotlinJsVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    implementation("com.ccfraser.muirwik:muirwik-components:0.6.3")

    implementation(npm("natural", "4.0.0"))
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