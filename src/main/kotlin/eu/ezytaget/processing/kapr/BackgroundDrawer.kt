package eu.ezytaget.processing.kapr

import eu.ezytaget.processing.kapr.palettes.Palette
import kotlin.random.Random

class BackgroundDrawer(private val palette: Palette) {

    var alpha = 1f

    fun drawRandomColor(pApplet: PApplet, random: Random, alpha: Float = this.alpha) {
        val randomRgbColor = palette.randomColor(random)
        pApplet.background(randomRgbColor, alpha)
    }

    fun draw(pApplet: PApplet, rgbColor: Int, alpha: Float = this.alpha) {
        pApplet.background(rgbColor, alpha)
    }
}