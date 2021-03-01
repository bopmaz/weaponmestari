package com.mint.weaponmestari.networking.mock

import android.os.SystemClock
import com.mint.weaponmestari.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()

            val responseString = when {
                uri.endsWith("warriors") -> WARRIOR_LIST
                uri.endsWith("weapons") -> WEAPON_LIST
                else -> ""
            }

            //Simulate loading from backend
            SystemClock.sleep(1000)

            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(responseString.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            throw IllegalAccessError("Run In Debug for mock response")
        }
    }
}

const val WARRIOR_LIST = """
[
{"id": 1, "name":"G4Lordi", "type":"assault", "damage":9000, "armor": 1001, "weapons": [0,1]},
{"id": 2, "name":"Juggernault", "type":"assault", "damage":9000, "armor": 1001, "weapons": [0]},
{"id": 3, "name":"Axe", "type":"defense", "damage":1000, "armor": 10001, "weapons": [0]},
{"id": 4, "name":"DK", "type":"defense", "damage":1000, "armor": 10001, "weapons": [0, 1]},
{"id": 5, "name":"Rylai", "type":"support", "damage":100, "armor": 100, "weapons": [0, 1]}
]
"""
const val WEAPON_LIST = """
[
{"weapon_id": 0, "weapon_type":"sword", "weapon_damage": 100, "range": 1},
{"weapon_id": 1, "weapon_type":"spear", "weapon_damage": 20, "range": 5}
]
"""