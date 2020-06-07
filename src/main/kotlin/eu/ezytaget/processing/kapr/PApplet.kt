package eu.ezytaget.processing.kapr

import eu.ezytaget.processing.kapr.metronome.BeatInterval
import eu.ezytaget.processing.kapr.metronome.BeatMetronome
import eu.ezytaget.processing.kapr.palettes.DuskPalette
import processing.core.PConstants
import kotlin.random.Random

class PApplet : processing.core.PApplet() {

    private lateinit var particleField: ParticleField

    private val random = Random(seed = 0)

    private val metronome = BeatMetronome()

    private var waitingForClickToDraw = false

    private var numberOfSlices = 128

    private val backgroundDrawer = BackgroundDrawer(DuskPalette(), alpha = 0.01f)

    private var particleGray = 0f

    private val particleAlpha = 0.01f

    override fun settings() {
        if (FULL_SCREEN) {
            fullScreen(RENDERER)
        } else {
            size(WIDTH, HEIGHT, RENDERER)
        }
    }

    private var lastBarCount = 0

    override fun setup() {
        frameRate(FRAME_RATE)
        colorMode(COLOR_MODE, MAX_COLOR_VALUE)
        initParticleField()
        clearFrameWithRandomColor()
        noCursor()
        metronome.start()
    }

    override fun draw() {
        if (CLICK_TO_DRAW && waitingForClickToDraw) {
            return
        }

        if (DRAW_BACKGROUND_ON_DRAW) {
            backgroundDrawer.draw(pApplet = this)
        }

        val metronomeDidAdvance = metronome.update()
        if (metronomeDidAdvance) {
            handleMetronomeValue()
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
                clearFrameWithRandomColor()
                initParticleField()
                metronome.start()
            }
            TAP_BPM_KEY -> {
                metronome.tapBpm()
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
        backgroundDrawer.draw(pApplet = this, alpha = 1f)
    }

    private fun clearFrameWithRandomColor() {
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

        if (random.nextFloat() < CHANGE_PARTICLE_GRAY_PROBABILITY) {
            particleGray = if (random.nextBoolean()) {
                0f
            } else {
                MAX_COLOR_VALUE
            }
        }
        stroke(particleGray, particleAlpha)

        for (sliceIndex in 0 until numberOfSlices) {
            val yRotation = (sliceIndex.toFloat() / numberOfSlices.toFloat() * PConstants.TWO_PI) + yRotationOffset
            translate(width / 2f, height / 2f, 0f)
            rotateY(yRotation)
            translate(-width / 2f, -height / 2f, 0f)

            particleField.drawConfigured(
                    pApplet = this,
                    drawLine = true
            )
        }
    }

    private fun handleMetronomeValue() {
        val intervalNumbers = metronome.intervalNumbers
        if (lastBarCount != intervalNumbers.getValue(BeatInterval.FourWhole)) {
            clearFrame()
            lastBarCount = intervalNumbers.getValue(BeatInterval.FourWhole)
        }
    }

    private fun showBeatCounter(intervalNumbers: Map<BeatInterval, Int>) {
        var formattedCounter = ""

        formattedCounter += intervalNumbers[BeatInterval.Whole]?.rem(4)?.plus(1)
        formattedCounter += BEAT_COUNTER_DIVIDER
        formattedCounter += intervalNumbers[BeatInterval.Eigth]?.rem(8)?.plus(1)
        formattedCounter += BEAT_COUNTER_DIVIDER
        formattedCounter += intervalNumbers[BeatInterval.Sixteenth]?.rem(16)?.plus(1)

        noStroke()
        fill(0.5f, 0.1f)
        textSize(height.toFloat())
        text(formattedCounter, 0f, height.toFloat())
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
        private const val CHANGE_PARTICLE_GRAY_PROBABILITY = 0.01f

        private const val CLEAR_FRAME_KEY = 'x'
        private const val INIT_PARTICLE_FIELD_KEY = 'z'
        private const val CLEAR_INIT_KEY = 'c'
        private const val TAP_BPM_KEY = ' '

        private const val BEAT_COUNTER_DIVIDER = ' '

        fun runInstance() {
            val instance = PApplet()
            instance.runSketch()
        }
    }
}