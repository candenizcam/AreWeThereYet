package application

import com.soywiz.klock.TimeSpan

class LevelGenerator {

    var obstacles = mutableListOf<Obstacle>()
    var dtAcc = 0.0

    fun generate(){
        obstacles.add(Obstacle(ObstacleTypes.NORMAL, 1.05, 0.05, 0.1, 0.1))
    }

    fun update(dt: TimeSpan){
        obstacles.forEach {
            it.centerX = it.centerX - dt.seconds*0.2
        }

        obstacles.removeAll { obstacle -> obstacle.centerX<0 }

        dtAcc+=dt.seconds

        if (dtAcc >= 2){
            dtAcc %= 2
            generate()
        }
    }
}