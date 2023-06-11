package synth

fun Int.toMusicalRepresentation() : String {
    val octave = (this / 12) - 1
    val noteName = when (this % 12) {
        0 -> "C"
        1 -> "C#"
        2 -> "D"
        3 -> "D#"
        4 -> "E"
        5 -> "F"
        6 -> "F#"
        7 -> "G"
        8 -> "G#"
        9 -> "A"
        10 -> "A#"
        11 -> "B"
        else -> ""
    }

    return "$noteName$octave"
}