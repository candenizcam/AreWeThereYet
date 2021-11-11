package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import pungine.PunImage
import pungine.PunScene
import pungine.Puntainer
import pungine.geometry2D.Rectangle

class Outside {
    suspend fun deploy(addFunction: (List<Puntainer>)->Unit){
        val bmp1 = resourcesVfs["environment/Bg_Small-3.png"].readBitmap()
        val bmp2 = resourcesVfs["environment/Bg_Small-2.png"].readBitmap()
        val bmp3 = resourcesVfs["environment/Bg_Small-1.png"].readBitmap()

        //PunImage("o1",bmp.clone(), Rectangle(0.0, 960.0, 0.0, 1080.0))

        closeList = List(3){it->
            PunImage(bitmap=bmp1.clone(), relativeRectangle = Rectangle(960.0*(it), 960.0*(it+1), 0.0, 1080.0))
        }
        mediumList = List(3){it->
            PunImage(bitmap=bmp2.clone(), relativeRectangle = Rectangle(960.0*(it), 960.0*(it+1), 0.0, 1080.0))
        }
        farList = List(3){it->
            PunImage(bitmap=bmp3.clone(), relativeRectangle = Rectangle(960.0*(it), 960.0*(it+1), 0.0, 1080.0))
        }
        addFunction(farList)
        addFunction(mediumList)
        addFunction(closeList)


        for (i in 0..2){
            closeList[i].x = 960.0*i
            mediumList[i].x = 960.0*i
            farList[i].x = 960.0*i
        }



        println("worked")
    }

    fun update(inc: Double){
        closeList.forEach {
            it.x -= inc
            if(it.x + it.width< -20.0){
                it.x += it.width * 3
            }
        }

        mediumList.forEach {
            it.x -= inc*0.25
            if(it.x + it.width< -20.0){
                it.x += it.width * 3
            }
        }

        farList.forEach {
            it.x -= inc*0.1
            if(it.x + it.width< -20.0){
                it.x += it.width * 3
            }
        }
    }

    var closeList: List<Puntainer> = listOf()
    var mediumList: List<Puntainer> = listOf()
    var farList: List<Puntainer> = listOf()
}