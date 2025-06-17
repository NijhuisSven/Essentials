plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "nl.nijhuissven"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.spongepowered.org/maven")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    /* Configuration */
    compileOnly("org.spongepowered:configurate-yaml:4.1.2")
    compileOnly("org.spongepowered:configurate-core:4.1.2")

    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("org.reflections:reflections:0.10.2")

    /* Lombok */
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    // Configurate YAML
    implementation("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")
    
    // Storm
    implementation("com.github.Mindgamesnl:storm:e1f961b480")

    // HikariCP for MySQL
    implementation("com.zaxxer:HikariCP:5.0.1")
    
    // MariaDB Driver
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    
    // SQLite JDBC
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
}

tasks {
    runServer {
        minecraftVersion("1.21")
    }
    shadowJar {
        relocate("co.aikar.commands", "nl.nijhuissven.acf")
        relocate("co.aikar.locales", "nl.nijhuissven.locales")
    }
}

