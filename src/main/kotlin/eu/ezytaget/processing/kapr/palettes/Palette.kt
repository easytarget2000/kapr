package eu.ezytaget.processing.kapr.palettes

import kotlin.random.Random

abstract class Palette {

    internal abstract val rgbColors: List<Int>

    val numberOfColors
        get() = rgbColors.size

    fun colorAtIndex(index: Int) = rgbColors[index % numberOfColors]

    fun randomColor(random: Random) = colorAtIndex(random.nextInt())

    fun nextColor(previousColor: Int) = colorAtIndex(rgbColors.indexOf(previousColor))
}