package eu.ezytaget.processing.kapr

import processing.core.PConstants
import kotlin.random.Random

class PApplet : processing.core.PApplet() {

    private lateinit var particleField: ParticleField

    private val random = Random(seed = 0)

    private var waitingForClickToDraw = false

    override fun settings() {
        if (FULL_SCREEN) {
            fullScreen(RENDERER)
        } else {
            size(WIDTH, HEIGHT, RENDERER)
        }
    }

    override fun setup() {
        frameRate(FRAME_RATE)
        colorMode(COLOR_MODE, MAX_COLOR_VALUE)
        initParticleField()
        background(0)
    }

    override fun draw() {
        if (CLICK_TO_DRAW && waitingForClickToDraw) {
            return
        }

        if (CLEAR) {
            background(0)
        }

        particleField.updateAndDraw(
                pApplet = this,
                maxColorValue = MAX_COLOR_VALUE,
                random = random,
                rounds = ROUNDS_PER_DRAW_CALL
        )

        if (CLICK_TO_DRAW) {
            waitingForClickToDraw = true
        }
    }

    override fun keyPressed() {
        when (key) {
            RESET_KEY -> {
                background(0)
                initParticleField()
            }
        }
    }

    override fun mouseClicked() {
        if (CLICK_TO_DRAW) {
            waitingForClickToDraw = false
        }
    }

    private fun initParticleField() {
        particleField = ParticleField.Builder().apply {
            worldWidth = width.toFloat()
            worldHeight = height.toFloat()
            numberOfParticles = NUMBER_OF_PARTICLES_PER_FIELD
        }.build()
    }

    companion object {
        private const val CLICK_TO_DRAW = false
        private const val FULL_SCREEN = true
        private const val WIDTH = 800
        private const val HEIGHT = 600
        private const val RENDERER = PConstants.P3D
        private const val COLOR_MODE = PConstants.HSB
        private const val MAX_COLOR_VALUE = 1f
        private const val FRAME_RATE = 60f
        private const val NUMBER_OF_PARTICLES_PER_FIELD = 512
        private const val CLEAR = true
        private const val RESET_KEY = ' '
        private const val ROUNDS_PER_DRAW_CALL = 8

        fun runInstance() {
            val instance = PApplet()
            instance.runSketch()
        }
    }
}