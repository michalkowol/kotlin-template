package pl.michalkowol

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Calc {
    fun add(a: Int, b: Int): Int = a + b
}

class FooSpec {

    @Test
    @DisplayName("it should test")
    fun test() {
        // given
        val calc = mock<Calc> {
            on { add(any(), any()) } doReturn 3
        }
        // when
        val sum = calc.add(3, 1)
        // then
        assertEquals(3, sum)
    }
}