package de.akquinet.msbenchmark.platform

data class Quartiles(val min: Double, val first: Double, val median: Double, val third: Double, val max: Double)

/**
 * uses https://en.wikipedia.org/wiki/Quartile
 * method 1
 */
fun List<Long>.computeExcludedQuartiles(): Quartiles {
    val sortedList = this.sorted()
    val min = sortedList[0].toDouble()
    val max = sortedList[sortedList.size - 1].toDouble()
    val median = medianInRange(sortedList, 0, sortedList.size -1)

    val oddNumberOfElements = sortedList.size.rem(2) == 1

    val first = medianInRange(sortedList,0, sortedList.size.div(2)-1)

    val third = if (oddNumberOfElements)
        medianInRange(sortedList, sortedList.size.div(2)+1, sortedList.size-1)
    else
        medianInRange(sortedList, sortedList.size.div(2), sortedList.size-1)

    return Quartiles(min, first, median, third, max)
}

private fun  medianInRange(sortedList: List<Long>, indexA: Int, indexB: Int) : Double {
    val sizeOfRange = (indexB - indexA) + 1
    val oddNumberOfElements = sizeOfRange.rem(2) == 1

    val middleIndex = indexA + sizeOfRange.div(2)

    val median =
        if (oddNumberOfElements)
            sortedList[middleIndex].toDouble()
        else
            (sortedList[middleIndex] + sortedList[middleIndex-1]) / 2.0

    return median
}