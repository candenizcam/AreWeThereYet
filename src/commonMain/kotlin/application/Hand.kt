package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.View
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle


@DelicateCoroutinesApi
@KorgeInternal
class Hand(id: String? = null, relativeRectangle: Rectangle) : Puntainer(id, relativeRectangle) {
    var fingerCut: Puntainer
    private val greenBlock: View
    init {
        //this.position(x,y)
        greenBlock = solidRect("sr", oneRectangle(), Colour.GREEN).also {
            it.visible = blockMode
            this.addChild(it)
        }
        fingerCut = Puntainer()
        this.addChild(fingerCut)
    }




    suspend fun suspendInitAlternative(fingerNo: String){

        ActiveAnimationType.values().forEach { it ->
            val punt= Puntainer("${fingerNo}_${it.animationID()}")
            this.addChild(punt)
            if(fingerNo=="two"){
                it.sourceList()
            }else{
                it.sourceListOne()
            }.forEach {s->
                Image(resourcesVfs[s].readBitmap()).also { it2->
                    it2.visible = false
                    punt.addChild(it2)
                }
            }


        }
    }




    private val animationSpeed = 15 //12 frames per second


}