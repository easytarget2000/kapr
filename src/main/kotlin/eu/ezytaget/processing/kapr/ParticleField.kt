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
            val maxJitter = smallestWorldLength / 128f

            var lastParticle: Particle? = null
            for (i in 0 until numberOfParticles) {
                val progress = i.toFloat() / numberOfParticles.toFloat()
                val offsetX = offsetLength * cos(x = progress * twoPi)
                val offsetY = offsetLength * sin(x = progress * twoPi)

                val position = PVector(originX + offsetX, originY + offsetY, z)
                val particle = Particle(id = i, position = position, maxJitter = maxJitter)

                if (firstParticle == null) {
                    firstParticle = particle
                } else {
                    lastParticle!!.next = particle
                }

                lastParticle = particle
            }

            lastParticle!!.next = firstParticle

            return ParticleField(firstParticle!!)
        }
    }

    fun updateAndDraw(pApplet: PApplet, random: Random) {
        var currentParticle = firstParticle
        do {
            update(currentParticle, random)
            draw(currentParticle, pApplet)
            currentParticle = currentParticle.next!!
        } while (currentParticle != firstParticle)
    }

    private fun update(particle: Particle, random: Random) {
        particle.update(firstParticle = firstParticle, random = random)
    }

    private fun draw(particle: Particle, pApplet: PApplet) {
        pApplet.point(particle.position.x, particle.position.y)
    }

}