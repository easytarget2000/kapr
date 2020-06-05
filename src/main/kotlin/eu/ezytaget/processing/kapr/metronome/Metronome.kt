package eu.ezytaget.processing.kapr.metronome

class Metronome {

    interface SteadyListener {
        fun onBeat(duration: BeatDuration)
    }

    var steadyListener: SteadyListener? = null

    var bpm = 133f
        set(value) {
            field = value
//            wholeNoteDuration = wholeNoteDuration()
//            beatTracker = beatTracker()
        }

    private var startMillis = nowMillis

    private val nowMillis
        get() = System.currentTimeMillis()

    private lateinit var beatTracker: List<BeatTracker>

    private var wholeNoteDuration = 500L

    fun start() {
        startMillis = nowMillis
        wholeNoteDuration = wholeNoteDurationMillis()
        beatTracker = beatTracker()
    }

    fun update() {
        val nowMillis = nowMillis
        beatTracker.forEach {
            if (it.updateCounter(nowMillis)) {
                steadyListener?.onBeat(it.beatDuration)
            }
        }
    }

    private fun beatTracker() = durations.map {
        BeatTracker(beatDuration = it, wholeNoteDuration = wholeNoteDuration, startMillis = startMillis)
    }

    private fun wholeNoteDurationMillis() = (MILLIS_PER_MINUTE / bpm).toLong()

    companion object {
        private const val MILLIS_PER_MINUTE = 60_000f
        private val durations = listOf(
                BeatDuration.Sixteenth,
                BeatDuration.Eigth,
                BeatDuration.Half,
                BeatDuration.Quarter,
                BeatDuration.Whole,
                BeatDuration.DoubleWhole,
                BeatDuration.QuadrupleWhole,
                BeatDuration.OctupleWhole,
                BeatDuration.SexdecupleWhole,
                BeatDuration.DuotrigintupleWhole
        )

        private val smallestBeatDuration = durations.first()
        private val smallestNoteToWholeNoteRatio = smallestBeatDuration.ratioToWhole
    }
}