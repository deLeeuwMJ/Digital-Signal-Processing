import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.SourceDataLine

class MidiPlayer(waveForm: WaveformGenerator, scope: CoroutineScope, notes: List<Note> = emptyList(), metronome: Metronome = Metronome()) :
    Player(waveForm, scope, notes, metronome) {

    override fun playNote(note: Note, playAt: LocalDateTime) {
        val midiNote = note.note
        println("Pressed: ${midiNote.toMusicalRepresentation()}")

        val format = AudioFormat(44100f, 16, 1, true, false)
        val line: SourceDataLine = AudioSystem.getSourceDataLine(format)
        line.open(format)
        line.start()

        val buffer = generateBuffer(calculateFrequency(midiNote))
        line.write(buffer, 0, buffer.size)

        val noteOffAt = playAt.plus((note.duration * metronome.millisPerBeat).toLong(), ChronoUnit.MILLIS)
        schedule(noteOffAt) {
            line.drain()
            line.stop()
            line.close()
        }
    }
}