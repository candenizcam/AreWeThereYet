package application

class ScoreKeeper {
    var scores = mutableListOf<Int>()

    fun addScore(newScore: Int) {
        scores.add(newScore)
        scores.sortDescending()
    }

    fun reset() {
        scores.removeAll { true }
    }
}