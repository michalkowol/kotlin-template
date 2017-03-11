package com.michalkowol.cars

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test

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
        assertThat(6, equalTo(sum))
        6 shouldMatch equalTo(sum)
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
        assertEquals(3, sum)
    }
}
