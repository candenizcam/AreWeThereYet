package application

import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector


data class Obstacle(var type: ObstacleTypes,var obstacleRarity: ObstacleRarity, var centerX: Double, var centerY: Double, var height: Double, var width: Double){
    val rectangle: Rectangle
    get() {
        return Rectangle(Vector(centerX,centerY),width,height)
    }
}
enum class ObstacleTypes{
    LOWJUMP {
        override fun relevantID(rarity: Int): String {
            return "low-jump-1"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(476.0/1980.0,600.0,200.0,0.0)
        }
    },
    HIGHJUMP {
        override fun relevantID(rarity: Int): String {
            return "high-jump-1"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(88.0/1920.0,336.0/1920.0,0.0/1080.0+240.0/1080.0,340.0/1080.0+240.0/1080.0)
        }
    },
    DUCK {
        override fun relevantID(rarity: Int): String {
            return "duck-1"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(778.0/1920.0,902.0/1920.0,140.0/1080.0+240.0/1080.0,840.0/1080.0+240.0/1080.0)
        }
    },
    LONGJUMP {
        override fun relevantID(rarity: Int): String {
            return "long-jump-1"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(1042.0/1920.0,1290.0/1920.0,0.0/1080.0+240.0/1080.0,200.0/1080.0+240.0/1080.0)
        }
    },
    DONTJUMP {
        override fun relevantID(rarity: Int): String {
            return "dont-jump-1"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(1373.0/1920.0,1713.0/1920.0,500.0/1080.0+240.0/1080.0,840.0/1080.0+240.0/1080.0)
        }
    },
    JUMPDUCK {
        override fun relevantID(rarity: Int): String {
            return "jump-duck-1"
        }

        override fun ratedRect(): Rectangle {
            return Rectangle(1621.0/1920.0,1761.0/1920.0,140.0/1080.0+240.0/1080.0,340.0/1080.0+240.0/1080.0)
        }
    };

    abstract fun relevantID(rarity:Int = 0): String
    abstract fun ratedRect(): Rectangle
}

enum class ObstacleRarity{
    RARE,
    RARER,
    RAREST
}