import kotlin.math.PI
import kotlin.math.sin

abstract class Oscillator(val frequency: Double) {
    abstract fun generateSample(time: Double, amplitude: Double): Double
}

class SineOscillator(frequency: Double) : Oscillator(frequency) {
    override fun generateSample(time: Double, amplitude: Double): Double {
        val angle = 2.0 * PI * frequency * time
        return amplitude * sin(angle)
    }
}

class SquareOscillator(frequency: Double) : Oscillator(frequency) {
    override fun generateSample(time: Double, amplitude: Double): Double {
        val period = 1.0 / frequency
        val phase = time % period
        return if (phase < period / 2.0) amplitude else -amplitude
    }
}

class SawtoothOscillator(frequency: Double) : Oscillator(frequency) {
    override fun generateSample(time: Double, amplitude: Double): Double {
        val period = 1.0 / frequency
        val phase = time % period
        return 2.0 * amplitude * (phase / period) - amplitude
    }
}