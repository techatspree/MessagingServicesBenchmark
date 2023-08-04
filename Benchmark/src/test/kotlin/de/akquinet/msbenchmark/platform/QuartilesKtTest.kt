package de.akquinet.msbenchmark.platform

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class QuartilesKtTest {
    @Test
    fun testListOf1To4() {
        val quartiles = (1..4 ).toList().computeExcludedQuartiles()

        assertSame(1.0, quartiles.min)
        assertSame(1.5, quartiles.first)
        assertSame(2.5, quartiles.median)
        assertSame(3.5, quartiles.third)
        assertSame(4.0, quartiles.max)
    }

    @Test
    fun testListOf1To5() {
        val quartiles = (1..5 ).toList().computeExcludedQuartiles()

        assertSame(1.0, quartiles.min)
        assertSame(1.5, quartiles.first)
        assertSame(3.0, quartiles.median)
        assertSame(4.5, quartiles.third)
        assertSame(5.0, quartiles.max)
    }

    @Test
    fun testWikipediaExample1() {
        val quartiles = listOf<Int>( 6, 7, 15, 36, 39, 40, 41, 42, 43, 47, 49).computeExcludedQuartiles()

        assertSame(6.0, quartiles.min)
        assertSame(15.0, quartiles.first)
        assertSame(40.0, quartiles.median)
        assertSame(43.0, quartiles.third)
        assertSame(49.0, quartiles.max)
    }

    @Test
    fun testWikipediaExample2() {
        val quartiles = listOf<Int>( 7, 15, 36, 39, 40, 41).computeExcludedQuartiles()

        assertSame(7.0, quartiles.min)
        assertSame(15.0, quartiles.first)
        assertSame(37.5, quartiles.median)
        assertSame(40.0, quartiles.third)
        assertSame(44.0, quartiles.max)
    }
}