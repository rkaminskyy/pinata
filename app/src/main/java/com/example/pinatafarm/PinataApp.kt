package com.example.pinatafarm

import android.app.Application
import com.example.pinatafarm.data.api.ApiPerson
import com.example.pinatafarm.data.api.FakeApiPerson
import com.example.pinatafarm.domain.DataRepository
import com.example.pinatafarm.domain.provider.SimpleFaceProvider
import com.example.pinatafarm.presentation.SharedViewModel
//import com.example.pinatafarm.presentation.SharedViewModelFactory
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PinataApp : Application() {

    private val diModules = module {
        single { Gson() }
        single<ApiPerson> { FakeApiPerson(get(), get()) }
        factory { resources }
        factory { SimpleFaceProvider(get()) }
        factory { DataRepository(get(), get()) }
        viewModel { SharedViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PinataApp)
            modules(diModules)
        }
    }

}