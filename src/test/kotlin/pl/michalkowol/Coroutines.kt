package pl.michalkowol

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class CoroutineSpec {

    @Test
    fun `your first coroutine`() {
        launch(CommonPool) { // create new coroutine in common thread pool
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello,") // main function continues while coroutine is delayed
        Thread.sleep(2000L)
    }

    @Test
    fun `run blocking`() = runBlocking { // start main coroutine
        launch(CommonPool) { // create new coroutine in common thread pool
            delay(1000L)
            println("World!")
        }
        println("Hello,") // main coroutine continues while child is delayed
        delay(2000L) // non-blocking delay for 2 seconds to keep JVM alive
    }

    @Test
    fun `test my suspending function`() = runBlocking {
        // here we can use suspending functions using any assertion style that we like
    }

    @Test
    fun `waiting for a job`() = runBlocking {
        val job = launch(CommonPool) { // create new coroutine and keep a reference to its Job
            delay(1000L)
            println("World!")
        }
        println("Hello,")
        job.join() // wait until child coroutine completes
    }

    @Test
    fun `extract function refactoring`() = runBlocking {
        val job = launch(CommonPool) { doWorld() }
        println("Hello,")
        job.join()
    }

    // this is your first suspending function
    suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

    @Test
    fun `coroutines ARE light-weight`() = runBlocking {
        val jobs = List(100_000) { // create a lot of coroutines and list their jobs
            launch(CommonPool) {
                delay(1000L)
                print(".")
            }
        }
        jobs.forEach { it.join() } // wait for all jobs to complete
    }

    @Test
    fun `coroutines are like daemon threads`() = runBlocking {
        launch(CommonPool) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // just quit after delay
    }

    @Test
    fun `cancelling coroutine execution`() = runBlocking {
        val job = launch(CommonPool) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
        job.cancel() // cancels the job
        delay(1300L) // delay a bit to ensure it was cancelled indeed
        println("main: Now I can quit.")
    }
}

