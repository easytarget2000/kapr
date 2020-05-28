package eu.ezytaget.processing.kapr

import processing.core.PVector

data class Particle(
        val id: Int,
        val position: PVector,
        val velocity: PVector = PVector(0f, 0f, 0f),
        var next: Particle? = null
)