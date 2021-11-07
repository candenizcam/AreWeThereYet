package application

import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector


data class Obstacle(
    var type: ObstacleTypes,
    var obstacleRarity: ObstacleRarity,
    var centerX: Double,
    var centerY: Double,
    var height: Double,
    var width: Double
) {
    val rectangle: Rectangle
        get() {
            return Rectangle(Vector(centerX, centerY), width, height)
        }
}

enum class ObstacleTypes {
    LOWJUMP {
        override fun relevantID(rarity: Int): String {
            return "low-jump-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                20.0 / 164.0,
                144.0 / 164.0,
                0.0 / 1080.0 + 240.0 / 1080.0,
                200.0 / 1080.0 + 240.0 / 1080.0
            )
        }
    },
    HIGHJUMP {
        override fun relevantID(rarity: Int): String {
            return "high-jump-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                117.0 /384.0,
                267.0/ 384.0,
                0.0 / 1080.0+ 240.0 / 1080.0,
                250.0 / 1080.0 + 240.0 / 1080.0
            )
        } //125.0 / 840, 250.0 / 840, 150.0 / 1920)) //1.05 170 340 248
    },
    DUCK {
        override fun relevantID(rarity: Int): String {
            return "duck-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                32.0 / 188.0,
                156.0 / 188.0,
                140.0 / 1080.0 + 240.0 / 1080.0,
                840.0 / 1080.0 + 240.0 / 1080.0
            )
        }
    },
    LONGJUMP {
        override fun relevantID(rarity: Int): String {
            return "long-jump-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                284.0 / 636.0,
                568.0 / 636.0,
                0.0 / 1080.0 + 240.0 / 1080.0,
                200.0 / 1080.0 + 240.0 / 1080.0
            )
        }
    },
    DONTJUMP {
        override fun relevantID(rarity: Int): String {
            return "dont-jump-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                238.0 / 426.0,
                362.0 / 426.0,
                500.0 / 1080.0 + 240.0 / 1080.0,
                840.0 / 1080.0 + 240.0 / 1080.0
            )
        }
    },
    LOWBIRD {
        override fun relevantID(rarity: Int): String {
            return "bird-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                50.0 / 318.0,
                174.0 / 318.0,
                500.0 / 1080.0 + 240.0 / 1080.0,
                640.0 / 1080.0 + 240.0 / 1080.0
            )
        }
    },
    HIGHBIRD {
        override fun relevantID(rarity: Int): String {
            return "bird-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                50.0 / 318.0,
                174.0 / 318.0,
                500.0 / 1080.0 + 240.0 / 1080.0,
                640.0 / 1080.0 + 240.0 / 1080.0
            )
        }
    },
    JUMPDUCK {
        override fun relevantID(rarity: Int): String {
            return "jump-duck-$rarity"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(
                52.0 / 238.0,
                176.0 / 238.0,
                140.0 / 1080.0 + 240.0 / 1080.0,
                340.0 / 1080.0 + 240.0 / 1080.0
            )
        }
    };

    abstract fun relevantID(rarity: Int = 1): String
    abstract fun ratedRect(): Rectangle
}

enum class ObstacleRarity {
    RARE,
    RARER,
    RAREST
}