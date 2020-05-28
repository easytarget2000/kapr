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
        initParticleField()
    }

    override fun draw() {
        particleField.updateAndDraw(pApplet = this, random = random)
    }

    private fun initParticleField() {
        particleField = ParticleField.Builder().apply {
            worldWidth = width.toFloat()
            worldHeight = height.toFloat()
            numberOfParticles = 128
        }.build()
    }

    companion object {
        private const val WIDTH = 800
        private const val HEIGHT = 600
        private const val RENDERER = PConstants.P3D

        fun runInstance() {
            val instance = PApplet()
            instance.runSketch()
        }
    }
}