package eu.ezytaget.processing.kapr

import processing.core.PVector
import kotlin.random.Random

data class Particle(
        val id: Int,
        val position: PVector,
        val maxJitter: Float,
        var next: Particle? = null
) {
    private val maxJitterHalf = maxJitter / 2f

    fun update(firstParticle: Particle, random: Random) {
        position.add(jitter(random))

        val velocity = PVector(0f, 0f, 0f)
        var currentParticle = firstParticle
        do {
            if (currentParticle == this) {
                currentParticle = currentParticle.next!!
                continue
            }



            currentParticle = currentParticle.next!!
        } while (currentParticle != this)

        position.add(velocity)
    }

    private fun jitter(random: Random) = PVector(
            (random.nextFloat() * maxJitter) - maxJitterHalf,
            (random.nextFloat() * maxJitter) - maxJitterHalf,
            (random.nextFloat() * maxJitter) - maxJitterHalf
    )
}