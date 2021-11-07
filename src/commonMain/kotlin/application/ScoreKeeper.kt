package application

import com.soywiz.korio.async.launch
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.GlobalScope

class ScoreKeeper {
    var scores = mutableListOf<Double>()

    fun addScore(newScore: Double) {
        scores.add(newScore)
        scores.sortDescending()
    }

    fun reset() {
        scores.removeAll { true }
    }

    fun save() {
        GlobalScope.launch {
            if(!resourcesVfs["scores/scores"].exists()) {
                resourcesVfs["scores"].mkdir()
            }
            resourcesVfs["scores/scores"].writeString(scores.toString())
        }
    }

    fun load() {
        GlobalScope.launch {
            if(resourcesVfs["scores/scores"].exists()) {
                reset()
                resourcesVfs["scores/scores"].readString().drop(1).dropLast(1).split(",").forEach { scores.add(it.toDouble()) }
            }
        }
    }
}