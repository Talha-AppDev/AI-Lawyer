import junit.framework.TestCase.assertEquals
import org.junit.Test

class FunctionTests {

    // A sample function that you want to test
    fun add(a: Int, b: Int): Int = a + b

    @Test
    fun testAdd() {
        assertEquals(5, add(2, 3))
    }
}

fun main() {
    println("Running test functions on the console!")
    // Call your functions here, for example:
    println("2 + 3 = ${2 + 3}")
}
