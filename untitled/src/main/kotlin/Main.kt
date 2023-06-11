import kotlinx.coroutines.*
import java.io.FileInputStream
import java.time.LocalDateTime
import javax.sound.midi.*

fun main(args: Array<String>) {
    val waveformGenerator: WaveformGenerator = SineWaveformGenerator()
    val source: InputSource = InputSource.FILE
    when (source) {
        InputSource.FILE -> {
            val fileName = "animals_melody.mid"
            val sequence: Sequence = MidiSystem.getSequence(FileInputStream(fileName))

            runBlocking {
                val player =
                    MidiPlayer(waveformGenerator, this@runBlocking, sequence.toNotes())
                player.playNotes(LocalDateTime.now())
            }
        }

        InputSource.DEVICE -> {
            val deviceName = "Impact GX49"
            val deviceInfo = MidiSystem.getMidiDeviceInfo().filter { info -> info.name == deviceName }.toList()
            println("Connected to ${deviceInfo[0].name}")

            runBlocking {
                val deviceOut = MidiSystem.getMidiDevice(deviceInfo[0]).apply { open() }
                val receiver = deviceOut.receiver

                val deviceIn = MidiSystem.getMidiDevice(deviceInfo[1]).apply { open() }
                val transmitter = deviceIn.transmitter

                transmitter.receiver = receiver

                val listener = object : Receiver {
                    override fun send(message: MidiMessage, timeStamp: Long) {
                        when (message) {
                            is ShortMessage -> {
                                val command = message.command
                                val note = message.data1
                                val amplitude = message.data2
                                val tick = timeStamp * 128 / (1000 * 60).toDouble()

                                if (command == ShortMessage.NOTE_ON) {
                                    val player =
                                        MidiPlayer(waveformGenerator, this@runBlocking)
                                    player.playNote(Note(tick, note, 0.25, amplitude / 127.0f), LocalDateTime.now())
                                }
                            }
                        }
                    }

                    override fun close() {
                        receiver.close()
                    }
                }

                transmitter.receiver = listener
                readlnOrNull()
                deviceOut.close()
                deviceIn.close()
            }
        }
    }
}







