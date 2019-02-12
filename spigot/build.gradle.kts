/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

dependencies {
    "jarLibs"(project(":ServerCore-Common"))
    compileOnly("org.spigotmc", "spigot-api", extra["versions.spigot"].toString())
}
