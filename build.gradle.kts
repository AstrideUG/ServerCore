import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    java
    kotlin("jvm") version "1.3.11"
}

repositories {
    mavenCentral()
}

subprojects {

    apply {
        plugin("java")
        plugin("kotlin")
    }

    group = "de.astride.minecraft"
    version = "1.0-SNAPSHOT"

    extra["libsDirName"] = "libraries"
    extra["libsDir"] = project.file(extra["libsDirName"].toString())

    extra["KotlinDependencies"] = listOf(
        "org.jetbrains.kotlin:kotlin-stdlib",
        "org.jetbrains.kotlin:kotlin-reflect",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1"
    )
    extra["TestDependencies"] = listOf(
        "org.jetbrains.kotlin:kotlin-test",
        "org.jetbrains.kotlin:kotlin-test-junit5",
        "org.mockito:mockito-all:2.0.2-beta",
        "org.junit.jupiter:junit-jupiter-api:5.3.1"
    )
    extra["compileKotlin"] = fun(p0: DependencyHandlerScope) {
        val dependencies = extra["KotlinDependencies"] as List<String>
        dependencies.forEach { p0.compile(it) }
    }
    extra["compileTest"] = fun(p0: DependencyHandlerScope) {
        val dependencies = extra["TestDependencies"] as List<String>
        dependencies.forEach { p0.testCompile(it) }
    }

    repositories {
        mavenCentral()
        jcenter()
        google()
        maven { url = URI("http://nexus.hc.to/content/repositories/pub_releases/") }
        maven { url = URI("https://hub.spigotmc.org/nexus/content/groups/public/") }
        maven { url = URI("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
        maven { url = URI("https://oss.sonatype.org/content/groups/public/") }
        maven { url = URI("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = URI("http://repo.dmulloy2.net/content/groups/public/") }
        maven { url = URI("https://repo.velocitypowered.com/snapshots/") }
        maven { url = URI("http://repo.spongepowered.org/maven") }
    }

    dependencies {
        compile(fileTree(extra["libsDirName"].toString()))
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.suppressWarnings = true
    }

}