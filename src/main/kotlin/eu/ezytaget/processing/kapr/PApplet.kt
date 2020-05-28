package eu.ezytaget.processing.kapr

import processing.core.PConstants
import kotlin.random.Random

class PApplet : processing.core.PApplet() {

    private lateinit var particleField: ParticleField

    private val random = Random(seed = 0)

    override fun settings() {
        size(WIDTH, HEIGHT, RENDERER)
    }

    override fun setup() {
        frameRate(FRAME_RATE)
        initParticleField()
    }

    override fun draw() {
        particleField.updateAndDraw(pApplet = this, random = random)
    }

    private fun initParticleField() {
        particleField = ParticleField.Builder().apply {
            worldWidth = width.toFloat()
            worldHeight = height.toFloat()
            numberOfParticles = NUMBER_OF_PARTICLES_PER_FIELD
        }.build()
    }

    companion object {
        private const val WIDTH = 800
        private const val HEIGHT = 600
        private const val RENDERER = PConstants.P3D
        private const val FRAME_RATE = 60f
        private const val NUMBER_OF_PARTICLES_PER_FIELD = 64

        fun runInstance() {
            val instance = PApplet()
            instance.runSketch()
        }
    }
}