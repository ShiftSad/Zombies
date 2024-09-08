plugins {
    id("io.github.goooler.shadow") version "8.1.7"
    id("java")
}

group = "codes.shiftmc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:7b180172ce")
}
