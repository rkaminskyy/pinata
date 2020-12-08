package com.example.pinatafarm.data.api

import android.content.res.Resources
import com.example.pinatafarm.data.PersonDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeApiPerson(
    private val resources: Resources,
    private val gson: Gson
) : ApiPerson {

    override fun getPeople(): Flow<List<PersonDTO>> {
        val raw: String = resources.assets.open("android_guys.json").bufferedReader().use { it.readText() }
        val list = gson.fromJson<List<PersonDTO>>(raw, object : TypeToken<List<PersonDTO>>() {}.type)
        return flowOf(list)
    }

}