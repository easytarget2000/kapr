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
            val offsetLength = 16f
            val twoPi = PI.toFloat() * 2f
            val maxParticleJitter = smallestWorldLength / 256f
            val particleRadius = smallestWorldLength / 256f
            val particlePushForce = smallestWorldLength * 0.01f
            val particleGravityToNext = -particleRadius * 0.5f
            val maxParticleInteractionDistance = smallestWorldLength / 4f

            var lastParticle: Particle? = null
            for (particleIndex in 0 until numberOfParticles) {
                val progress = particleIndex.toFloat() / numberOfParticles.toFloat()
                if (VERBOSE) {
                    println("ParticleField.Builder: build(): progress: $progress")
                }
                val offsetX = offsetLength * cos(x = progress * twoPi)
                val offsetY = offsetLength * sin(x = progress * twoPi)

                val particlePosition = PVector(originX + offsetX, originY + offsetY, z)
                val particle = Particle(
                        id = particleIndex,
                        position = particlePosition,
                        radius = particleRadius,
                        pushForce = particlePushForce,
                        gravityToNext = particleGravityToNext,
                        preferredDistanceToNext = 0f,   // Will be set later.
                        maxInteractionDistance = maxParticleInteractionDistance,
                        maxJitter = maxParticleJitter
                )

                if (firstParticle == null) {
                    firstParticle = particle
                    if (VERBOSE) {
                        println("ParticleField.Builder: build(): firstParticle: $firstParticle")
                    }
                } else {
                    lastParticle!!.next = particle
                    particle.preferredDistanceToNext =
                            particle.position.dist(lastParticle.position) * PREFERRED_DISTANCE_PUSH_FACTOR

                    if (VERBOSE) {
                        println("ParticleField.Builder: build(): particle: $particle")
                    }
                }

                lastParticle = particle
            }

            lastParticle!!.next = firstParticle!!
            firstParticle.preferredDistanceToNext =
                    firstParticle.position.dist(lastParticle.position) * PREFERRED_DISTANCE_PUSH_FACTOR
            println("ParticleField.Builder: build(): firstParticle: $firstParticle")

            return ParticleField(firstParticle)
        }
    }

    fun updateAndDraw(pApplet: PApplet, maxColorValue: Float, random: Random, rounds: Int) {
        pApplet.stroke(maxColorValue)
        pApplet.strokeWeight(2f)
        for (i in 0 until rounds) {
            updateAndDrawOnce(pApplet, maxColorValue, random)
        }
    }

    fun updateAndDrawOnce(pApplet: PApplet, maxColorValue: Float, random: Random) {
        var currentParticle = firstParticle
        do {
            update(currentParticle, random)
            draw(currentParticle, pApplet)
            currentParticle = currentParticle.next
        } while (currentParticle != firstParticle)
    }

    private fun update(particle: Particle, random: Random) {
        particle.update(random = random)
    }

    private fun draw(particle: Particle, pApplet: PApplet) {
        if (debugDrawIDs.isNotEmpty() && !debugDrawIDs.contains(particle.id)) {
            return
        }
        pApplet.point(particle.position.x, particle.position.y)
    }

    companion object {
        private const val VERBOSE = true
        private const val PREFERRED_DISTANCE_PUSH_FACTOR = 1.01f
        private var debugDrawIDs = listOf<Int>()
    }
}