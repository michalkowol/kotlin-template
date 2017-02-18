# Kotlin Template

[![Build Status](https://travis-ci.org/michalkowol/kotlin-template.svg?branch=master)](https://travis-ci.org/michalkowol/kotlin-template)
[![Coverage Status](https://codecov.io/github/michalkowol/kotlin-template/badge.svg?branch=master)](https://codecov.io/github/michalkowol/kotlin-template?branch=master)

## Default

```
gradle
```

## Build

```
gradle build
```

## Run

```
gradle run
```

## Continuous build

```
gradle run -t
```

or

```
gradle run --continuous
```

## FatJar

Sometimes you need to remove jars signatures:

`build.gradle`:

```groovy
// ...
task fatJar(type: Jar) {
    baseName = "${project.name}-assembly"
    manifest = jar.manifest
    exclude('META-INF/*.SF')
    exclude('META-INF/*.DSA')
    exclude('META-INF/*.RSA')
    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
    with jar
}
// ...
```

```
gradle fatJar
java -jar build/libs/${NAME}-assembly-${VERSION}.jar
```

## Code coverage

```
gradle jacocoTestReport
open build/jacocoHtml/index.html
```

## Heroku

```
heroku login
heroku create
git push heroku master
```

## Code snippets 

### Dagger

`build.gradle`:

```groovy
// ...
apply plugin: 'kotlin-kapt'
// ...
sourceSets {
    // for IntelliJ
    main.java.srcDirs += "$buildDir/generated/source/kapt/main"
}
// ...
dependencies {
    // ...
    compile 'com.google.dagger:dagger:2.+'
    kapt 'com.google.dagger:dagger-compiler:2.+'
    // ...
}
// ...
```

```kotlin
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject

interface Pump {
    fun pump(): String
}
interface Heater {
    fun heat(): String
}

class Thermosiphon @Inject constructor(private val heater: Heater) : Pump {
    override fun pump(): String {
        heater.heat()
        return "=> => pumping => =>"
    }
}

class ElectricHeater : Heater {
    override fun heat(): String = "~ ~ ~ heating ~ ~ ~"
}

data class CoffeeMaker @Inject constructor(val heater: Heater, val pump: Pump) {
    fun brew(): String {
        val heat = heater.heat()
        val pump = pump.pump()
        val coffee = " [_]P coffee! [_]P "
        return "$heat\n$pump\n$coffee"
    }
}


@Module
class DripCoffeeModule {
    @Provides fun provideHeater(): Heater = ElectricHeater()
    @Provides fun providePump(pump: Thermosiphon): Pump = pump
}

@Component(modules = arrayOf(DripCoffeeModule::class))
interface CoffeeShop {
    fun maker(): CoffeeMaker
}

fun main(args: Array<String>) {
    val coffeeShop = DaggerCoffeeShop.builder().build()
    val maker = coffeeShop.maker()
    println(maker.brew())
}
```

### Autoclosable

```kotlin
import java.lang.AutoCloseable

inline fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R {
    var exception: Throwable? = null
    try {
        return block(this)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        when {
            this == null -> {}
            exception == null -> close()
            else ->
                try {
                    close()
                } catch (closeException: Throwable) {
                    exception.addSuppressed(closeException)
                }
        }
    }
}
```

```kotlin
someAutoCloseable.use { r ->
    // ...
}
```

### Kovenant (deprecated)

```kotlin
// compile 'nl.komponents.kovenant:kovenant:3.0.0'

import nl.komponents.kovenant.*
import nl.komponents.kovenant.functional.*

fun longOperation(url: String): Promise<String, Exception> = task {
    val result = // ...
    result
}

fun foo(key: String): Promise<String, Exception> {
    val url = getUrl(key) ?: return Promise.ofFail(Exception("Not Found"))
    return longOperation(url)
}

val success = Promise.of("aa")
val result: String = success.get
val bar = foo("google.com").map { result -> "foo $result bar" }
val foo3 = combine(foo("google.com"), foo("google.com"), foo("google.com")).success { it.first + it.second + it.third }
val foofoo = foo("google.com") and foo("google.com") success { it.first + it.second }
val foosFutures: List<Promise<String, Exception>> = listOf(foo("google.com"), foo("google.com"))
val futureFoos: Promise<List<String>, Exception> = all(foosFutures)
val nested: Promise<String, Exception> = foo("google.com").bind { result -> foo(result) }
```

### Injekt

```kotlin
// compile 'uy.kohesive.injekt:injekt-core:1.14.+'

import uy.kohesive.injekt.*
import uy.kohesive.injekt.api.*

interface Animal
class Dog : Animal
class Cat : Animal

class AnimalContainer(val animal: Animal)

class A(val b: B, val c: C)
class B
class C(val d: D)
class D

class Boot {
    companion object : InjektMain() {
        @JvmStatic public fun main(args: Array<String>) {
            Boot().run()
        }

        override fun InjektRegistrar.registerInjectables() {
            addLoggerFactory({ byName -> LoggerFactory.getLogger(byName) }, { byClass -> LoggerFactory.getLogger(byClass) })
            addSingleton(D())
            addSingleton(C(Injekt.get()))
            addSingleton(B())
            addFactory { A(Injekt.get(), Injekt.get()) }

            addSingleton(Dog())
            addSingleton<Animal>(Cat())
            addFactory { AnimalContainer(Injekt.get()) }
        }
    }

    private val log: Logger by injectLogger()

    fun run() {
        val locallog = Injekt.logger<Logger>(Boot)
        log.debug("Hello!")

        val a1 = Injekt.get<A>()
        val a2 = Injekt.get<A>()
        log.debug("a: $a1 b: ${a1.b} c: ${a1.c} d: ${a1.c.d}")
        log.debug("a: $a2 b: ${a2.b} c: ${a2.c} d: ${a2.c.d}")

        val animalContainer = Injekt.get<AnimalContainer>()
        log.debug("$animalContainer ${animalContainer.animal}")
    }
}
```

## References
* [Kovenant - Promises for Kotlin](https://github.com/mplatvoet/kovenant)
* [Injekt - Dependency Injection for Kotlin](https://github.com/kohesive/injekt)
* [kotlinx.coroutines - libraries built upon Kotlin coroutines](https://github.com/Kotlin/kotlinx.coroutines)
