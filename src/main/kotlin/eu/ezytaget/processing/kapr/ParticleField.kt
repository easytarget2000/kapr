package eu.ezytaget.processing.kapr

import processing.core.PConstants
import processing.core.PConstants.LINES
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
        var originX = worldWidth / 2f
        var originY = worldHeight / 2f
        var numberOfParticles = 10
        var z = 0f

        fun build(): ParticleField {
            var firstParticle: Particle? = null

            val smallestWorldLength = min(worldWidth, worldHeight)

            val offsetLength = smallestWorldLength / 8f
            val twoPi = PI.toFloat() * 2f
            val maxParticleJitter = smallestWorldLength / 512f
            val particleRadius = smallestWorldLength / 512f
            val particlePushForce = particleRadius * 4f
            val particleGravityToNext = -particleRadius * 0.5f
            val maxParticleInteractionDistance = smallestWorldLength / 6f

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

            if (VERBOSE) {
                println("ParticleField.Builder: build(): firstParticle: $firstParticle")
            }           

            return ParticleField(firstParticle)
        }
    }

    var particleAlpha = 0.1f

    var addParticleProbability = 1f / 1000f

    var maxNumOfParticles: Int = 256

    fun update(random: Random) {
        var counter = 0

        var currentParticle = firstParticle
        do {
            if (++counter < maxNumOfParticles && addParticle(random)) {
                val newParticle = currentParticle.copy(id = random.nextInt())
                newParticle.next = currentParticle.next
                currentParticle.next = newParticle
            }

            update(currentParticle, random)
            currentParticle = currentParticle.next
        } while (currentParticle != firstParticle)
    }

    fun drawConfiguredVolumetric(
            pApplet: PApplet,
            drawLine: Boolean,
            numberOfSlices: Int = 16,
            yRotationOffset: Float = 0f
    ) {
        pApplet.pushMatrix()

        for (sliceIndex in 0 until numberOfSlices) {
            val yRotation = (sliceIndex.toFloat() / numberOfSlices.toFloat() * PConstants.TWO_PI) + yRotationOffset
            pApplet.translate(pApplet.width / 2f, pApplet.height / 2f, 0f)
            pApplet.rotateY(yRotation)
            pApplet.translate(-pApplet.width / 2f, -pApplet.height / 2f, 0f)

            drawConfigured(pApplet, drawLine)
        }

        pApplet.popMatrix()
    }

    fun drawConfigured(pApplet: PApplet, drawLine: Boolean) {
        if (drawLine) {
            pApplet.beginShape(LINES)
        }
        var currentParticle = firstParticle
        do {
            if (drawLine) {
                pApplet.vertex(currentParticle.position.x, currentParticle.position.y)
            } else {
                drawPoint(currentParticle, pApplet)
            }
            currentParticle = currentParticle.next
        } while (currentParticle != firstParticle)

        if (drawLine) {
            pApplet.endShape()
        }
    }

    private fun update(particle: Particle, random: Random) {
        particle.update(random = random)
    }

    private fun drawPoint(particle: Particle, pApplet: PApplet) {
        if (debugDrawIDs.isNotEmpty() && !debugDrawIDs.contains(particle.id)) {
            return
        }
    }

    private fun addParticle(random: Random) = random.nextFloat() < addParticleProbability

    companion object {
        private const val VERBOSE = false
        private const val PREFERRED_DISTANCE_PUSH_FACTOR = 1.01f
        private var debugDrawIDs = listOf<Int>()
    }
}