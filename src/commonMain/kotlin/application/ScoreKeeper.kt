package application

import com.soywiz.korio.file.std.resourcesVfs

class ScoreKeeper {
    var scores = mutableListOf<Int>()

    fun addScore(newScore: Int) {
        scores.add(newScore)
        scores.sortDescending()
    }

    fun reset() {
        scores.removeAll { true }
    }

    suspend fun save() {
        if (!resourcesVfs["scores"].exists()) {
            resourcesVfs["scores"].mkdir()
        }
        resourcesVfs["scores/scores"].writeString(scores.toString())

    }

    suspend fun load() {
        if (resourcesVfs["scores"].exists()) {
            reset()
            resourcesVfs["scores/scores"].readString().drop(1).dropLast(1).split(", ").forEach { scores.add(it.toInt()) }
        }
    }
}