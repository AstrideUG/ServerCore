/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import org.jetbrains.kotlin.resolve.calls.inference.CapturedType

plugins {
    java
    kotlin("jvm")
}

dependencies {
    compileKotlin()
    compileTest()
//    compile("org.spigotmc", "spigot-api", extra["versions.spigot"].toString())
//    compile("net.md-5", "bungeecord-api", extra["versions.bungee"].toString())
//    compile("com.velocitypowered", "velocity-api", extra["versions.velocity"].toString())
    compile("org.mongodb", "mongodb-driver-async", extra["versions.mongodb"].toString())
    compile("com.google.inject", "guice", extra["versions.guice"].toString())
    compile("com.google.code.gson", "gson", extra["versions.gson"].toString())

//    compile("org.spongepowered:spongeapi:7.1.0")
}

fun DependencyHandlerScope.compileKotlin() = (extra["compileKotlin"] as Function1<DependencyHandlerScope, *>)(this)
fun DependencyHandlerScope.compileTest() = (extra["compileTest"] as Function1<DependencyHandlerScope, *>)(this)
