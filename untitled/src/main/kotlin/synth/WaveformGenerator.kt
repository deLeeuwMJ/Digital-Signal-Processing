package synth

abstract class WaveformGenerator {
    abstract fun generateSample(frequency: Double, t: Double): Short
}

class SineWaveformGenerator : WaveformGenerator() {
    override fun generateSample(frequency: Double, t: Double): Short {
        return (Short.MAX_VALUE * kotlin.math.sin(2.0 * Math.PI * frequency * t)).toInt().toShort()
    }
}

class SawtoothWaveformGenerator : WaveformGenerator() {
    override fun generateSample(frequency: Double, t: Double): Short {
        val period = 1.0 / frequency
        val normalizedT = t / period
        val value = 2.0 * (normalizedT - normalizedT.toInt()) - 1.0
        return (Short.MAX_VALUE * value).toInt().toShort()
    }
}

class SquareWaveformGenerator : WaveformGenerator() {
    override fun generateSample(frequency: Double, t: Double): Short {
        val period = 1.0 / frequency
        val normalizedT = t / period
        return if (normalizedT - normalizedT.toInt() >= 0.5) Short.MAX_VALUE else Short.MIN_VALUE
    }
}