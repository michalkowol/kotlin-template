//package pl.michalkowol
//
//import com.nhaarman.mockito_kotlin.any
//import com.nhaarman.mockito_kotlin.doReturn
//import com.nhaarman.mockito_kotlin.mock
//import org.junit.Assert.assertEquals
//import org.junit.Test
//
//class MockitoSpec {
//
//    @Test
//    fun `it should test`() {
//        // given
//        val calc = mock<Calc> {
//            on { add(any(), any()) } doReturn 3
//        }
//        // when
//        val sum = calc.add(3, 1)
//        // then
//        assertEquals(3, sum)
//    }
//}