package de.akquinet.msbenchmark.platform

class Quartiles(val min:Double, val first:Double, val median:Double, val third:Double, val max:Double )

fun <T:Number> Collection<T>.computeExcludedQuartiles() : Quartiles {
    val sortedList = this.sortedWith(NumberComparator())

    TODO("Not implemented yet")
}