package pl.michalkowol

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test

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
}
