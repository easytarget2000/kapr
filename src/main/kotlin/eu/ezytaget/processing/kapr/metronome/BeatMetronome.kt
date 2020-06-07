package eu.ezytaget.processing.kapr.metronome

import kotlin.math.floor

class BeatMetronome {

    private val bpmCounter = BpmCounter()

    var bpm = 150f
        set(value) {
            field = value
            initTickLengthMillis()
            if (VERBOSE) {
                println("DEBUG: BeatMetronome: bpm: $bpm")
            }
        }

    private var startMillis = nowMillis

    private val nowMillis
        get() = System.currentTimeMillis()

    private var numberOfAcknowledgedTicks = 0

    private var tickLengthMillis = 0L

    lateinit var intervalNumbers: Map<BeatInterval, Int>

    fun start() {
        startMillis = nowMillis
        initTickLengthMillis()
    }

    fun update(): Boolean {
        val nowMillis = nowMillis
        val numberOfTicks = ((nowMillis - startMillis) / tickLengthMillis).toInt()
        if (numberOfTicks == numberOfAcknowledgedTicks) {
            return false
        }

        numberOfAcknowledgedTicks = numberOfTicks
        intervalNumbers = intervals.map {
            it to floor(numberOfAcknowledgedTicks.toDouble() / it.numberOfTicks.toDouble()).toInt()
        }.toMap()

        return true
    }

    fun tapBpm() {
        val tappedBpm = bpmCounter.tap(nowMillis)
        if (tappedBpm != null) {
            bpm = tappedBpm
        }
    }

    private fun initTickLengthMillis() {
        tickLengthMillis = ((MILLIS_PER_MINUTE / bpm) / BeatInterval.Whole.numberOfTicks.toFloat()).toLong()
    }

    companion object {
        private const val VERBOSE = true
        private const val MILLIS_PER_MINUTE = 60_000f
        private val intervals = listOf(
                BeatInterval.Sixteenth,
                BeatInterval.Eigth,
                BeatInterval.Half,
                BeatInterval.Quarter,
                BeatInterval.Whole,
                BeatInterval.TwoWhole,
                BeatInterval.FourWhole,
                BeatInterval.EightWhole,
                BeatInterval.SixteenWhole,
                BeatInterval.ThirstyTwoWhole
        )

        private val smallestBeatDuration = intervals.first()
        private val smallestNoteToWholeNoteRatio = smallestBeatDuration.numberOfTicks
    }
}