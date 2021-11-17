package application

import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.readMusic
import com.soywiz.korau.sound.readSound
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.baseName
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect

@DelicateCoroutinesApi
object SfxPlayer {
    /*
    private val sfxList = mutableListOf<Pair<String, Sound>>()
    suspend fun loadSounds(){
        GlobalScope.launch {
            resourcesVfs["SFX"].list().collect { file -> sfxList.add(Pair(file.baseName, file.readSound())) }
        }
    }

    fun playSfx(name: String){
        GlobalScope.launch {
            sfxList.find { pair -> pair.first == name }?.second?.play()
        }
    }*/

    suspend fun playSfx(name: String){
        resourcesVfs["SFX/$name"].readSound().play()
    }
}