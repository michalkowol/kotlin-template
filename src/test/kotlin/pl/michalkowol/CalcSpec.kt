package pl.michalkowol

import org.junit.Assert.assertEquals
import org.junit.Test

class CalcSpec {

    @Test
    fun `it should add number`() {
        // given
        val calc = Calc()
        // when
        val sum = calc.add(2, 4)
        // then
        assertEquals(6, sum)
    }
}
