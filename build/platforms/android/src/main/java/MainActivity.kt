package com.pungame.pungame
import com.soywiz.korio.android.withAndroidContext
import com.soywiz.korgw.KorgwActivity
import main
class MainActivity : KorgwActivity() {
	override suspend fun activityMain() {
		main()
	}
}
