package com.michalkowol.cars

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Calc {
    fun add(a: Int, b: Int): Int = a + b
}

class CalcSpec {

    @Test
    fun `it should add number`() {
        // given
        val calc = Calc()
        // when
        val sum = calc.add(2, 4)
        // then
        assertThat(sum).isEqualTo(6)
    }

    @Test
    fun `it should mock Calc`() {
        // given
        val calc = mock<Calc> {
            on { add(any(), any()) } doReturn 3
        }
        // when
        val sum = calc.add(3, 1)
        // then
        assertThat(sum).isEqualTo(3)
    }
}
