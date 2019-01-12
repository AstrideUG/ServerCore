rootProject.name = "ServerCore"
rootProject.buildFileName = "build.gradle.kts"

val subs = arrayOf(
    //    "api",
    "spigot",
//    "sponge",
//    "bungee",
    "common"//,
//    "velocity"
)

include(*subs)

subs.forEach {
    findProject(":$it")?.name = "${rootProject.name}-${it.capitalize()}"
}