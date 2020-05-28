package eu.ezytaget.processing.kapr

import processing.core.PVector
import processing.core.PVector.sub
import kotlin.random.Random

data class Particle(
        val id: Int,
        val position: PVector,
        val pushForce: Float,
        val radius: Float,
        val gravityToNext: Float,
        val preferredDistanceToNext: Float,
        val maxInteractionDistance: Float,
        val maxJitter: Float
) {
    lateinit var next: Particle

    private val maxJitterHalf = maxJitter / 2f

    fun update(firstParticle: Particle, random: Random) {
//        position.add(jitter(random))

        val velocity = PVector(0f, 0f, 0f)
        var otherParticle = firstParticle
        do {
            if (otherParticle == this) {
                otherParticle = next
                continue
            }

            val vectorToOther = sub(otherParticle.position, position)
            val distanceToOther = vectorToOther.mag()
            if (distanceToOther > maxInteractionDistance) {
                otherParticle = otherParticle.next
                continue
            }

            val force = if (otherParticle == next) {
                if (distanceToOther > preferredDistanceToNext) {
                    distanceToOther / pushForce
                } else {
                    gravityToNext
                }
            } else {
                if (distanceToOther < radius) {
                    -radius;
                } else {
                    -(pushForce / distanceToOther);
                }
            }

            vectorToOther.setMag(force)
            velocity.add(vectorToOther)

            otherParticle = otherParticle.next
        } while (otherParticle != this)

        position.add(velocity)
    }

    private fun force(distanceToOther: Float) = if (distanceToOther < preferredDistanceToNext) {
        preferredDistanceToNext / distanceToOther
    } else {
        -(distanceToOther / preferredDistanceToNext)
    } * 0.01f

    private fun jitter(random: Random) = PVector(
            (random.nextFloat() * maxJitter) - maxJitterHalf,
            (random.nextFloat() * maxJitter) - maxJitterHalf,
            0f
    )
}