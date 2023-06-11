import java.math.RoundingMode
import javax.sound.midi.Sequence
import javax.sound.midi.ShortMessage

fun Sequence.toNotes(): List<Note> {
    return this.tracks.flatMap { track ->
        (0 until track.size()).asSequence().map { index ->
            val event = track[index]
            when (val message = event.message) {
                is ShortMessage -> {
                    val command = message.command
                    val note = message.data1
                    val amplitude = message.data2
                    if (command == ShortMessage.NOTE_ON) {
                        val beat = (event.tick / this.resolution.toDouble())
                            .toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                            .toDouble()
                        return@map Note(beat, note, 0.25, amplitude / 127.0f)
                    }
                }
            }
            return@map null
        }.filterNotNull()
    }
}