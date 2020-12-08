package com.example.pinatafarm.data.api

import com.example.pinatafarm.data.PersonDTO
import kotlinx.coroutines.flow.Flow

interface ApiPerson {
    fun getPeople(): Flow<List<PersonDTO>>
}