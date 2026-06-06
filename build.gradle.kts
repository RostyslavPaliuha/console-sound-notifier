import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "2.3.20"
  id("org.jetbrains.intellij.platform") version "2.16.0"
}

group = "com.rostyslav"
version = "0.0.3"

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(25))
  }
}

kotlin {
  jvmToolchain(25)
}

dependencies {
  intellijPlatform {
    intellijIdea("2026.1")
  }
    testImplementation(kotlin("test"))
}

intellijPlatform {
  pluginConfiguration {
    ideaVersion {
      sinceBuild = "261"
      untilBuild = "261.*"
    }
  }

  signing {
    certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
    privateKey = providers.environmentVariable("PRIVATE_KEY")
    password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
  }

  publishing {
    token = providers.environmentVariable("PUBLISH_TOKEN")
  }
}

tasks {
  // IntelliJ 2026.1 runs on JBR 21, so keep emitted plugin bytecode compatible.
  withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
    options.release.set(21)
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_21
    }
  }
}
