package com.example.pinatafarm.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinatafarm.domain.DataRepository
import com.example.pinatafarm.domain.Person
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SharedViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val listState = MutableLiveData<UiState>()
    private val selected = MutableLiveData<Person>()

    init {
        viewModelScope.launch {
            dataRepository.getPeople()
                .onStart {
                    listState.value = UiState.Loading
                }
                .catch {
                    listState.postValue(UiState.Error(it))
                }
                .collect {
                    listState.postValue(UiState.Result(it))
                }
        }
    }

    fun getPeopleUiState(): LiveData<UiState> = listState

    fun getSelectedPerson(): LiveData<Person> = selected

    fun setSelected(person: Person) {
        selected.value = person
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Result(val persons: List<Person>) : UiState()
    data class Error(val e: Throwable) : UiState()
}