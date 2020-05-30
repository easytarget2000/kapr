package eu.ezytaget.processing.kapr

import eu.ezytaget.processing.kapr.palettes.DuskPalette
import processing.core.PConstants
import kotlin.random.Random

class PApplet : processing.core.PApplet() {

    private lateinit var particleField: ParticleField

    private val random = Random(seed = 0)

    private var waitingForClickToDraw = false

    private var numberOfSlices = 128

    private val backgroundDrawer = BackgroundDrawer(DuskPalette(), alpha = 0.01f)

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
        clearFrame()
        noCursor()
    }

    override fun draw() {
        if (CLICK_TO_DRAW && waitingForClickToDraw) {
            return
        }

        if (DRAW_BACKGROUND_ON_DRAW) {
            backgroundDrawer.draw(pApplet = this)
        }

        updateAndDrawParticleField()

        if (CLICK_TO_DRAW) {
            waitingForClickToDraw = true
        }
    }

    override fun keyPressed() {
        when (key) {
            CLEAR_FRAME_KEY -> {
                clearFrame()
            }
            INIT_PARTICLE_FIELD_KEY -> {
                initParticleField()
            }
            CLEAR_INIT_KEY -> {
                clearFrame()
                initParticleField()
            }
        }
    }

    override fun mouseClicked() {
        if (CLICK_TO_DRAW) {
            waitingForClickToDraw = false
        }
    }

    override fun mouseMoved() {
    }

    private fun clearFrame() {
        backgroundDrawer.drawRandomColor(
                pApplet = this,
                random = random,
                alpha = 1f
        )
    }

    private fun initParticleField() {
        particleField = ParticleField.Builder().apply {
            worldWidth = width.toFloat()
            worldHeight = height.toFloat()
            numberOfParticles = NUMBER_OF_PARTICLES_PER_FIELD
        }.build()
    }

    private fun updateAndDrawParticleField() {
        particleField.update(random)

        val yRotationOffset = frameCount.toFloat() / 10000f

        for (sliceIndex in 0 until numberOfSlices) {
            val yRotation = (sliceIndex.toFloat() / numberOfSlices.toFloat() * PConstants.TWO_PI) + yRotationOffset
            translate(width / 2f, height / 2f, 0f)
            rotateY(yRotation)
            translate(-width / 2f, -height / 2f, 0f)

            particleField.draw(
                    pApplet = this,
                    maxColorValue = MAX_COLOR_VALUE,
                    drawLine = true
            )
        }
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
        private const val NUMBER_OF_PARTICLES_PER_FIELD = 256
        private const val DRAW_BACKGROUND_ON_DRAW = true
        private const val CLEAR_FRAME_KEY = 'x'
        private const val INIT_PARTICLE_FIELD_KEY = 'z'
        private const val CLEAR_INIT_KEY = ' '

        fun runInstance() {
            val instance = PApplet()
            instance.runSketch()
        }
    }
}