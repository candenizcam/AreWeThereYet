package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.View
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle

@DelicateCoroutinesApi
@KorgeInternal
class Hand(id: String? = null, relativeRectangle: Rectangle) : Puntainer(id, relativeRectangle) {

}