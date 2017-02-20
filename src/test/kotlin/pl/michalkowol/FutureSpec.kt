package pl.michalkowol

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import java.time.Duration
import java.util.concurrent.CompletableFuture

class FutureSpec {

    @Test
    fun `return result of two futrues into one`() = runBlocking {
        val asyncdd: Deferred<String> = async(CommonPool) {
            val original = asyncLoadImage(Duration.ofSeconds(1))
            val overlay = asyncLoadImage(Duration.ofSeconds(2))
            applyOverlay(original.await(), overlay.await(), Duration.ofMillis(500)).await()
        }
        val image: CompletableFuture<String> = future {
            val original = asyncLoadImage(Duration.ofSeconds(1))
            val overlay = asyncLoadImage(Duration.ofSeconds(2))
            applyOverlay(original.await(), overlay.await(), Duration.ofMillis(500)).await()
        }
        println(image.await())
    }

    fun asyncLoadImage(delay: Duration): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            Thread.sleep(delay.toMillis())
            "image after ${delay.toMillis()}ms"
        }
    }

    fun applyOverlay(original: String, overlay: String, delay: Duration): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            Thread.sleep(delay.toMillis())
            "overlay of [$original] and [$overlay] after ${delay.toMillis()}ms"
        }
    }
}
