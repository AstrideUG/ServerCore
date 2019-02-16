package de.astride.minecraft.servercore.common

import de.astride.darkbedrock.apis.modules.common.loader.ClassModuleLoader
import net.darkdevelopers.darkbedrock.darkness.general.modules.manager.ClassJavaModuleManager
import java.io.File
import java.io.File.separator
import kotlin.properties.Delegates

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 12.01.2019 19:02.
 * Current Version: 1.0 (12.01.2019 - 12.01.2019)
 */
class ServerCore(private val dataFolder: File) {

    private var moduleManager: ClassJavaModuleManager by Delegates.notNull()

    init {
        start()
    }

    private fun start() {
        //Old Module System
        println("Enable Old Module System")
        moduleManager = ClassJavaModuleManager(File("$dataFolder${separator}modules${separator}old"))
        println("Enabled Old Module System")

        //New Module System
        println("Enable New Module System")
        val directory = File("$dataFolder${separator}modules${separator}new")
        val loader = setOf(ClassModuleLoader(directory)/*, JavaModuleLoader(directory)*/)
        loader.forEach {
            it.detectModules()
            it.loadModules()
        }
        println("Enabled New Module System")
    }

}