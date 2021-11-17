package application

import com.soywiz.korau.sound.*
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.baseName
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect

@DelicateCoroutinesApi
object MusicPlayer {
    /*
    val tracks = mutableListOf<Pair<String, Sound>>()
    val playing = mutableListOf<Pair<String, SoundChannel?>>()

    suspend fun loadBGM() {
        GlobalScope.launch {
            var t = "music/musicbox.mp3"
            tracks.add(Pair(t, resourcesVfs[t].readMusic()))
           // resourcesVfs["music"].list().collect { file -> tracks.add(Pair(file.baseName, file.readSound())) }
        }.join()
    }

    suspend fun play(name: String) {
        GlobalScope.launch {
            resourcesVfs["music/musicbox.mp3"].readMusic().play(PlaybackTimes.INFINITE)

            /*playing.add(
                Pair(
                    name,
                    tracks.find { pair -> pair.first == name }?.second?.play(PlaybackParameters(PlaybackTimes.INFINITE))
                )
            )

             */
        }
    }

    suspend fun playALT(name: String) {
        playing.add(
            Pair(
                name,
                tracks.find { pair -> pair.first == name }?.second?.play(PlaybackParameters(PlaybackTimes.INFINITE))
            )
        )
    }
        fun stop(name: String) {
        playing.find { pair -> pair.first == name }?.second?.stop()
    }

*/
    suspend fun play(name: String) {
        val test = resourcesVfs["music/$name"].readMusic()
        test.play(PlaybackTimes.INFINITE)
    }
}