package eu.ezytaget.processing.kapr

import eu.ezytaget.processing.kapr.palettes.DuskPalette
import eu.ezytarget.clapper.BeatInterval
import eu.ezytarget.clapper.Clapper
import processing.core.PConstants
import kotlin.random.Random

class PApplet : processing.core.PApplet() {

    private var waitingForClickToDraw = false
    private val random = Random(seed = 0)
    private val clapper = Clapper()
    private val backgroundDrawer = BackgroundDrawer(DuskPalette(), alpha = 0.01f)
    private var terrainWidth = 800f
    private var terrainDepth = 800f
    private var terrainCellSize = 32f
    private lateinit var terrainHeights: List<List<Float>>
    private val numberOfColumns
        get() = (terrainWidth / terrainCellSize).toInt()
    private val numberOfRows
        get() = (terrainDepth / terrainCellSize).toInt()
    private var clearOnTap = false
    private var minTerrainZ = 0f
    private var maxTerrainZ = 400f
    private var terrainZFactor = 1f
    private var terrainBaseRoughness = 0.05f
    private var terrainRoughnessOffset = 0.01f

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
        noCursor()
        lights()
        clearFrameWithRandomColor()

        clapper.bpm = 64f
        clapper.start()

        setTerrainValues()
    }

    override fun draw() {
        if (CLICK_TO_DRAW && waitingForClickToDraw) {
            return
        }

        if (DRAW_BACKGROUND_ON_DRAW) {
            backgroundDrawer.draw(pApplet = this)
        }
//        background(0f)

        updateClapper()
        moveTerrainHeights()
        drawTerrain()

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
            }
            CLEAR_INIT_KEY -> {
                clearFrameWithRandomColor()
                clapper.start()
            }
            TAP_BPM_KEY -> {
                tapBpm()
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
        backgroundDrawer.draw(pApplet = this)
    }

    private fun clearFrameWithRandomColor() {
        backgroundDrawer.drawRandomColor(
                pApplet = this,
                random = random,
                alpha = 1f
        )
    }

    private fun setTerrainValues() {
        terrainWidth = width * 2f
        terrainDepth = height * 4f
    }

    private fun moveTerrainHeights() {
        val movementOffset = frameCount / -6f
        val terrainRoughness = terrainBaseRoughness + terrainRoughnessOffset
        val maxTerrainZ = maxTerrainZ * terrainZFactor

        terrainHeights = (0 until numberOfRows).map { rowIndex ->
            val rowHeights = (0 until numberOfColumns).map { columnIndex ->
                val noise = noise(
                        columnIndex.toFloat() * terrainRoughness,
                        (rowIndex + movementOffset) * terrainRoughness
                )
                map(noise, 0f, 1f, minTerrainZ, maxTerrainZ)
            }
            rowHeights
        }
    }

    private fun drawTerrain() {
        pushMatrix()
        translate(width / 2f, height / 2f)
        rotateX(PConstants.THIRD_PI)
        translate(-terrainWidth / 2f, -terrainDepth / 2f)

        noStroke()

        (0 until numberOfRows - 1).forEach { rowIndex ->
            beginShape(PConstants.TRIANGLE_STRIP)
            (0 until numberOfColumns).forEach { columnIndex ->
                val progress = rowIndex.toFloat() / numberOfRows.toFloat()
//                fill(MAX_COLOR_VALUE * progress)
                stroke(MAX_COLOR_VALUE * progress, 0.1f)
                vertex(
                        columnIndex * terrainCellSize,
                        rowIndex * terrainCellSize,
                        terrainHeights[rowIndex][columnIndex]
                )
                vertex(
                        columnIndex * terrainCellSize,
                        (rowIndex + 1) * terrainCellSize,
                        terrainHeights[rowIndex + 1][columnIndex]
                )
            }
            endShape()
        }


        popMatrix()
    }

    private fun tapBpm() {
        clapper.tapBpm()

        if (clearOnTap) {
            clearFrame()
        }
    }

    private fun updateClapper() {
        val reactToClapper = clapper.update(BeatInterval.Whole)
        if (!reactToClapper) {
            return
        }

        clearFrameWithRandomColor()

//        if (!maybe { clearFrame() }) {
//            maybe {
//            }
//        }
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
        private const val DRAW_BACKGROUND_ON_DRAW = true

        private const val CLEAR_FRAME_KEY = 'x'
        private const val INIT_PARTICLE_FIELD_KEY = 'z'
        private const val CLEAR_INIT_KEY = 'c'
        private const val TAP_BPM_KEY = ' '

        fun runInstance() {
            val instance = PApplet()
            instance.runSketch()
        }
    }

}
