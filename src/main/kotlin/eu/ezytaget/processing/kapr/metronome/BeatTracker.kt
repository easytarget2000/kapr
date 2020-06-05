package eu.ezytaget.processing.kapr.metronome

class BeatTracker(
        val beatDuration: BeatDuration,
        wholeNoteDuration: Long,
        private val startMillis: Long
) {
    var counter = 0
        private set

    private val periodLengthMillis = (wholeNoteDuration.toFloat() * beatDuration.ratioToWhole).toLong()

    fun updateCounter(nowMillis: Long = System.currentTimeMillis()): Boolean {
        val numberOfPeriods = ((nowMillis - startMillis) / periodLengthMillis).toInt()
        return if (numberOfPeriods > counter) {
            counter = numberOfPeriods
            true
        } else {
            false
        }
    }
}