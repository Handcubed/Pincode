import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.5.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("xyz.jpenilla.run-paper") version "2.0.1"  // For testing purposes.
}

group = "xyz.amayakasa"
version = "0.1.0"
description = "Protect your game profile."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.13.0")
    implementation("org.xerial:sqlite-jdbc:3.41.0.0")
}
repositories {
    mavenCentral()
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    javadoc {
        options.encoding = "UTF-8"
    }
    processResources {
        filteringCharset = "UTF-8"
    }
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    main = "xyz.amayakasa.pincode.Pincode"
    apiVersion = "1.19"
    authors = listOf("Amayakasa", "Vanility")
    website = "code.amayakasa.xyz"
}