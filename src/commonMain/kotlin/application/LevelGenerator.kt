package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korau.sound.readSound
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.math.roundDecimalPlaces
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

class LevelGenerator() {

    var obstacles = mutableListOf<Obstacle>()
    var obstacleDistance = 0.0
    private var toNext = 1.0
    var nextFloor = 0.3
    private var nextCeil = 1.0
    var speed = 00.3
    val maxSpeed = 1.0
    var acceleration = 1.0001
    var lastGenerated = 0
    var nowGenerated = 0
    var rarity = ObstacleRarity.RARE

    fun generateType(): Int{
        nowGenerated = if (lastGenerated == 1) {
            (1..7).random()
        } else {
            (0..7).random()
        }
        if(obstacles.any { it.type.ordinal == nowGenerated }) {
            return generateType()
        }
        return nowGenerated
    }

    @DelicateCoroutinesApi
    fun generate() {
        nowGenerated = generateType()
        rarity = when ((0..5).random()) {
            5 -> {
                ObstacleRarity.RAREST
            }
            4, 3 -> {
                ObstacleRarity.RARER
            }
            else -> {
                ObstacleRarity.RARE
            }
        }

        when (nowGenerated) {
            ObstacleTypes.DUCK.ordinal -> obstacles.add(Obstacle(ObstacleTypes.DUCK, rarity, 1.05, 1.0 - 350.0 / 840, 700.0 / 840, 124.0 / 1920))
            ObstacleTypes.HIGHJUMP.ordinal -> obstacles.add(Obstacle(ObstacleTypes.HIGHJUMP, rarity, 1.05, 170.0 / 840, 340.0 / 840, 248.0 / 1920))
            ObstacleTypes.LOWJUMP.ordinal -> obstacles.add(Obstacle(ObstacleTypes.LOWJUMP, rarity, 1.05, 100.0 / 840, 200.0 / 840, 124.0 / 1920))
            ObstacleTypes.LONGJUMP.ordinal -> obstacles.add(Obstacle(ObstacleTypes.LONGJUMP, rarity, 1.05, 100.0 / 840, 200.0 / 840, 248.0 / 1920))
            ObstacleTypes.DONTJUMP.ordinal -> obstacles.add(
                Obstacle(
                    ObstacleTypes.DONTJUMP,
                    rarity,
                    1.05,
                    1.0 - 170.0 / 840,
                    340.0 / 840,
                    124.0 / 1920
                )
            )
            ObstacleTypes.JUMPDUCK.ordinal -> obstacles.add(Obstacle(ObstacleTypes.JUMPDUCK, rarity, 1.05, 240.0 / 840, 200.0 / 840, 124.0 / 1920))
            ObstacleTypes.HIGHBIRD.ordinal -> obstacles.add(
                Obstacle(
                    ObstacleTypes.HIGHBIRD,
                    rarity,
                    1.05,
                    1 - 240.0 / 840,
                    100.0 / 840,
                    124.0 / 1920
                )
            )
            ObstacleTypes.LOWBIRD.ordinal -> obstacles.add(
                Obstacle(
                    ObstacleTypes.LOWBIRD,
                    rarity,
                    1.05,
                    1 - 490.0 / 840,
                    100.0 / 840,
                    124.0 / 1920
                )
            )
        }
        lastGenerated = nowGenerated

        SfxPlayer.playSfx("cut.mp3")
    }


    fun update(dt: TimeSpan) {
        if (speed < maxSpeed) {
            speed *= (acceleration)
        }

        obstacles.forEach {
            it.centerX -= dt.seconds * speed
        }

        obstacles.removeAll { obstacle -> obstacle.centerX < -0.05 }

        obstacleDistance += dt.seconds * speed

        if (obstacleDistance >= toNext && obstacles.size < 5) {
            obstacleDistance = 0.0
            generate()

            val seed = Random.nextDouble()
            seed.roundDecimalPlaces(2)

            toNext = (nextCeil - nextFloor) * seed + nextFloor
        }
    }
}