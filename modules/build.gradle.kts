/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

dependencies {
    "jarLibs"(project(":ServerCore-Spigot"))
    compileOnly("org.spigotmc", "spigot-api", extra["versions.spigot"].toString())
    compileOnly("me.lucko.luckperms", "luckperms-api", "4.0")
}
