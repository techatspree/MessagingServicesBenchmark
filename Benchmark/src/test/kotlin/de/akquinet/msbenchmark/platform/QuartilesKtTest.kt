package de.akquinet.msbenchmark.platform

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class QuartilesKtTest {
    @Test
    fun testListOf1To4() {
        val quartiles = (1L..4L ).toList().computeExcludedQuartiles()

        assertEquals(1.0, quartiles.min)
        assertEquals(1.5, quartiles.first)
        assertEquals(2.5, quartiles.median)
        assertEquals(3.5, quartiles.third)
        assertEquals(4.0, quartiles.max)
    }

    @Test
    fun testListOf1To5() {
        val quartiles = (1L..5L ).toList().computeExcludedQuartiles()

        assertEquals(1.0, quartiles.min)
        assertEquals(1.5, quartiles.first)
        assertEquals(3.0, quartiles.median)
        assertEquals(4.5, quartiles.third)
        assertEquals(5.0, quartiles.max)
    }

    @Test
    fun testWikipediaExample1() {
        // from https://en.wikipedia.org/wiki/Quartile
        val quartiles = listOf( 6L, 7, 15, 36, 39, 40, 41, 42, 43, 47, 49).computeExcludedQuartiles()

        assertEquals(6.0, quartiles.min)
        assertEquals(15.0, quartiles.first)
        assertEquals(40.0, quartiles.median)
        assertEquals(43.0, quartiles.third)
        assertEquals(49.0, quartiles.max)
    }

    @Test
    fun testWikipediaExample2() {
        // from https://en.wikipedia.org/wiki/Quartile
        val quartiles = listOf( 7L, 15, 36, 39, 40, 41).computeExcludedQuartiles()

        assertEquals(7.0, quartiles.min)
        assertEquals(15.0, quartiles.first)
        assertEquals(37.5, quartiles.median)
        assertEquals(40.0, quartiles.third)
        assertEquals(41.0, quartiles.max)
    }
}