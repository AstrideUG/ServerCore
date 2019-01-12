/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import org.jetbrains.kotlin.resolve.calls.inference.CapturedType
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "4.0.3"
}

dependencies {
    compileKotlin()
    compileTest()
    compile(project(":ServerCore-Common"))
    compile("org.spigotmc", "spigot-api", extra["versions.spigot"].toString())
}

artifacts {
    //    task("shadowJar")
}


private val shadowJar: ShadowJar by tasks
shadowJar.apply {

    appendix = "with-dependencies"
    classifier = ""

    dependencies {
        this.include(dependency("de.astride.minecraft::"))
//        include(
//            dependency(
//                findProject(":ServerCore-Common")!!.sourceSets["main"].output
//            )
//        )
//        from(findProject(":ServerCore-Common")!!.sourceSets["main"].output)
    }

}


fun DependencyHandlerScope.compileKotlin() = (extra["compileKotlin"] as Function1<DependencyHandlerScope, *>)(this)
fun DependencyHandlerScope.compileTest() = (extra["compileTest"] as Function1<DependencyHandlerScope, *>)(this)
