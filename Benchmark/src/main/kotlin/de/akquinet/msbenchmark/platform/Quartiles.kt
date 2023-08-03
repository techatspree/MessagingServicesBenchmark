package de.akquinet.msbenchmark.platform

class Quartiles<T:Number>(val first:T, val second:T, val third:T, val fourth:T )

fun <T:Number> Collection<T>.computeQuartiles() : Quartiles<T> {
    TODO("Not implemented yet")
}