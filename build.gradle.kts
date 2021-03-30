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
    val ktor_version = "1.5.1"

    implementation(kotlin("stdlib-js", kotlinVersion))
    implementation("org.jetbrains:kotlin-styled:5.2.1-$kotlinJsVersion")
    implementation("io.ktor:ktor-client-js:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
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