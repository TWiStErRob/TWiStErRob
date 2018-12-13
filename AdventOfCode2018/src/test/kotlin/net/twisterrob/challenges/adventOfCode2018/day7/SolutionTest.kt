package net.twisterrob.challenges.adventOfCode2018.day7

import org.junit.Test
import kotlin.test.assertEquals

class SolutionTest {

    @Test
    fun `sorts linear dependencies`() {
        val result = solve('B' to 'C', 'A' to 'B')

        assertEquals("ABC", result)
    }

    @Test
    fun `sample in challenge works`() {
        val input = SolutionTest::class.java.getResourceAsStream("sampleInput.txt")

        val result = solve(input.use { it.reader().readText() })

        assertEquals("CABDFE", result)
    }

    @Test
    fun `real input produces an output`() {
        val input = SolutionTest::class.java.getResourceAsStream("input.txt")

        val result = solve(input.use { it.reader().readText() })

        assertEquals("OCPUEFIXHRGWDZABTQJYMNKVSL", result)
    }
}
