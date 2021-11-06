import application.TestModule
import com.soywiz.korge.*
import com.soywiz.korge.internal.KorgeInternal

@KorgeInternal
suspend fun main() = Korge(Korge.Config(module = TestModule))