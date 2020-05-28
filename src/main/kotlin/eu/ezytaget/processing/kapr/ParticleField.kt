package eu.ezytaget.processing.kapr

import processing.core.PVector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class ParticleField(private val firstParticle: Particle) {

    class Builder {
        var worldWidth = 100f
        var worldHeight = 100f
        var numberOfParticles = 10
        var z = 0f

        fun build(): ParticleField {
            var firstParticle: Particle? = null

            val smallestWorldLength = min(worldWidth, worldHeight)

            val originX = worldWidth / 2f
            val originY = worldHeight / 2f
            val offsetLength = smallestWorldLength / 32f
            val twoPi = PI.toFloat() * 2f
            val maxParticleJitter = smallestWorldLength / 128f
            val particleRadius = smallestWorldLength / 256f
            val particlePushForce = smallestWorldLength * 0.0047f
            val particleGravityToNext = -particleRadius * 0.5f
            val preferredParticleDistanceToNext = smallestWorldLength / 128f
            val maxParticleInteractionDistance = smallestWorldLength / 8f

            var lastParticle: Particle? = null
            for (particleIndex in 0 until numberOfParticles) {
                val progress = particleIndex.toFloat() / (numberOfParticles.toFloat() - 1f)
                val offsetX = offsetLength * cos(x = progress * twoPi)
                val offsetY = offsetLength * sin(x = progress * twoPi)

                val particlePosition = PVector(originX + offsetX, originY + offsetY, z)
                val particle = Particle(
                        id = particleIndex,
                        position = particlePosition,
                        radius = particleRadius,
                        pushForce = particlePushForce,
                        gravityToNext = particleGravityToNext,
                        preferredDistanceToNext = preferredParticleDistanceToNext,
                        maxInteractionDistance = maxParticleInteractionDistance,
                        maxJitter = maxParticleJitter
                )

                if (firstParticle == null) {
                    firstParticle = particle
                } else {
                    lastParticle!!.next = particle
                }

                lastParticle = particle
            }

            lastParticle!!.next = firstParticle!!

            return ParticleField(firstParticle)
        }
    }

    fun updateAndDraw(pApplet: PApplet, random: Random) {
        var currentParticle = firstParticle
        do {
            draw(currentParticle, pApplet)
            update(currentParticle, random)
            currentParticle = currentParticle.next
        } while (currentParticle != firstParticle)
    }

    private fun update(particle: Particle, random: Random) {
        particle.update(firstParticle = firstParticle, random = random)
    }

    private fun draw(particle: Particle, pApplet: PApplet) {
        if (debugDrawIDs.isNotEmpty() && !debugDrawIDs.contains(particle.id)) {
            return
        }
        pApplet.point(particle.position.x, particle.position.y)
    }

    companion object {
        private val debugDrawIDs = listOf<Int>()
    }
}