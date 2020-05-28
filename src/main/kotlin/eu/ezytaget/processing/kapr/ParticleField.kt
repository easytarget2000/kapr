package eu.ezytaget.processing.kapr

import processing.core.PVector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleField(private val firstParticle: Particle) {

    class Builder {
        var worldWidth = 100f
        var worldHeight = 100f
        var numberOfParticles = 10
        var z = 0f

        fun build(): ParticleField {
            var firstParticle: Particle? = null

            val originX = worldWidth / 2f
            val originY = worldHeight / 2f
            val offsetLength = worldHeight / 32f
            val twoPi = PI.toFloat() * 2f

            var lastParticle: Particle? = null
            for (i in 0 until numberOfParticles) {
                val progress = i.toFloat() / numberOfParticles.toFloat()
                val offsetX = offsetLength * cos(x = progress * twoPi)
                val offsetY = offsetLength * sin(x = progress * twoPi)

                val position = PVector(originX + offsetX, originY + offsetY, z)
                val particle = Particle(id = i, position = position)

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

    fun updateAndDraw(pApplet: PApplet) {
        var currentParticle = firstParticle
        do {
            update(currentParticle)
            draw(currentParticle, pApplet)
            currentParticle = currentParticle.next!!
        } while (currentParticle != firstParticle)
    }

    private fun update(particle: Particle) {

    }

    private fun draw(particle: Particle, pApplet: PApplet) {
        pApplet.point(particle.position.x, particle.position.y)
    }

}