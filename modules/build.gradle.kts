/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */
repositories {
    maven("http://nexus.hc.to/content/repositories/pub_releases/")
}

dependencies {
    "jarLibs"(project(":ServerCore-Spigot"))
    compileOnly("org.spigotmc", "spigot-api", extra["versions.spigot"].toString())
    compileOnly("me.lucko.luckperms", "luckperms-api", "4.0")
    compileOnly("net.milkbowl.vault", "VaultAPI", "1.6")

}
