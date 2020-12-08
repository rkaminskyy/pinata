package com.example.pinatafarm.domain

import com.example.pinatafarm.data.api.ApiPerson
import com.example.pinatafarm.domain.provider.SimpleFaceProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataRepository(
    private val api: ApiPerson,
    private val faceProvider: SimpleFaceProvider
) {
    fun getPeople(): Flow<List<Person>> {

        return api.getPeople()
            .map {
                val results = mutableListOf<Person>()

                it.forEach { person ->
                    val face = faceProvider.provideForImage(person.img)
                    val faceBounds = face.map { it.boundingBox }

                    results.add(
                        Person(
                            person.name,
                            person.img,
                            faceBounds
                        )
                    )
                }

                results.also { it.sortWith(compareByDescending<Person> { it.largestArea() }.thenBy { it.name }) }
            }
    }
}