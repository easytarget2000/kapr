package eu.ezytaget.processing.kapr

import eu.ezytaget.processing.kapr.palettes.Palette
import kotlin.random.Random

class BackgroundDrawer(private val palette: Palette, var alpha: Float = 0f) {

    var rgbColor: Int = 0

    fun drawRandomColor(pApplet: PApplet, random: Random, alpha: Float = this.alpha) {
        rgbColor = palette.randomColor(random)
        draw(pApplet, alpha)
    }

    fun draw(pApplet: PApplet, alpha: Float = this.alpha) {
        // CONTINUE HERE
        pApplet.background(this.rgbColor, 10f)
    }
}