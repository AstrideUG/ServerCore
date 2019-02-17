repositories {
    maven("https://jitpack.io")
}

dependencies {
    "jarLibs"(project(":ServerCore-Spigot"))
    compileOnly("org.spigotmc", "spigot-api", extra["versions.spigot"].toString())
    compileOnly("me.lucko.luckperms", "luckperms-api", "4.0")
    compileOnly("com.github.MilkBowl", "VaultAPI", "68f14eca20")
}
