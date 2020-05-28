package eu.ezytaget.processing.kapr

import processing.core.PVector
import kotlin.random.Random

data class Particle(
        val id: Int,
        val position: PVector,
        val velocity: PVector = PVector(0f, 0f, 0f),
        val maxJitter: Float,
        var next: Particle? = null
) {

    private val maxJitterHalf = maxJitter / 2f

    fun update(random: Random) {
        position.add(velocity)
        position.add(jitter(random))
    }

    private fun jitter(random: Random) = PVector(
            (random.nextFloat() * maxJitter) - maxJitterHalf,
            (random.nextFloat() * maxJitter) - maxJitterHalf,
            (random.nextFloat() * maxJitter) - maxJitterHalf
    )
}