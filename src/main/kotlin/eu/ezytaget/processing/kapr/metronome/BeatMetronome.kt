package eu.ezytaget.processing.kapr.metronome

import kotlin.math.floor

class BeatMetronome {

    interface Listener {
        fun onIntervalNumbersChanged(intervalNumbers: Map<BeatInterval, Int>)
    }

    var listener: Listener? = null

    private var bpm = 108f
        set(value) {
            field = value
            initTickLengthMillis()
        }

    private var startMillis = nowMillis

    private val nowMillis
        get() = System.currentTimeMillis()

    private var numberOfAcknowledgedTicks = 0

    private var tickLengthMillis = 0L

    fun start() {
        startMillis = nowMillis
        initTickLengthMillis()
    }

    fun update() {
        val nowMillis = nowMillis
        val numberOfTicks = ((nowMillis - startMillis) / tickLengthMillis).toInt()
        if (numberOfTicks == numberOfAcknowledgedTicks) {
            return
        }

        numberOfAcknowledgedTicks = numberOfTicks
        val intervalNumbers = intervals.map {
            it to floor(numberOfAcknowledgedTicks.toDouble() / it.numberOfTicks.toDouble()).toInt()
        }.toMap()

        listener?.onIntervalNumbersChanged(intervalNumbers)
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