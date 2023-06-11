data class Metronome(var bpm: Int = 128) {
    val millisPerBeat: Long
        get() = (secsPerBeat * 1000).toLong()

    private val secsPerBeat: Double
        get() = 60.0 / bpm
}