package eu.ezytaget.processing.kapr.metronome

enum class BeatDuration(val ratioToWhole: Float) {
    Sixteenth(ratioToWhole = 1f / 16f),
    Eigth(ratioToWhole = 1f / 8f),
    Quarter(ratioToWhole = 1f / 4f),
    Half(ratioToWhole = 1f / 2f),
    Whole(ratioToWhole = 1f),
    DoubleWhole(ratioToWhole = 2f),
    QuadrupleWhole(ratioToWhole = 4f),
    OctupleWhole(ratioToWhole = 8f),
    SexdecupleWhole(ratioToWhole = 16f),
    DuotrigintupleWhole(ratioToWhole = 32f)
}