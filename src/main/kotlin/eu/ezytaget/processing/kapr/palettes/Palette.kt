package eu.ezytaget.processing.kapr.palettes

import kotlin.random.Random
import kotlin.random.nextUInt

abstract class Palette {

    internal abstract val rgbColors: List<Int>

    val numberOfColors
        get() = rgbColors.size

    fun colorAtIndex(index: Int) = rgbColors[index % (numberOfColors - 1)]

    fun randomColor(random: Random) = colorAtIndex(random.nextInt(0, numberOfColors))

    fun nextColor(previousColor: Int) = colorAtIndex(rgbColors.indexOf(previousColor))
}