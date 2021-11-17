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

@DelicateCoroutinesApi
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
    suspend fun generate() {
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
            ObstacleTypes.HIGHJUMP.ordinal -> obstacles.add(Obstacle(ObstacleTypes.HIGHJUMP, rarity, 1.05, 125.0 / 840, 250.0 / 840, 150.0 / 1920)) //1.05 170 340 248
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
        if(GlobalAccess.fingers==2){
            when((0..19).random()){
                13 -> SfxPlayer.playSfx("radiotune.mp3")
                17 -> SfxPlayer.playSfx("turnSignal.mp3")
            }
        } else {
            when((0..29).random()){
                13 -> SfxPlayer.playSfx("radiotune.mp3")
                17 -> SfxPlayer.playSfx("turnSignal.mp3")
                5 -> SfxPlayer.playSfx("daddy.mp3")
                8 -> SfxPlayer.playSfx("mommy.mp3")
            }
        }

            if(nowGenerated==5 || nowGenerated==6) {
                if ((0..2).random()==1) {
                    SfxPlayer.playSfx("crows-or-rooks-10.mp3")
                }
            }
    }


    suspend fun update(dt: TimeSpan) {
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