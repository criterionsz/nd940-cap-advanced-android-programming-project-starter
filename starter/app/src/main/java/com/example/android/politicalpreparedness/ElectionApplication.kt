package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.repository.ElectionRepository

/*Application class is a base class of Android
app containing components like Activities and Services. Application or
its sub classes are instantiated before all the activities or any other application objects
have been created in Android app.
contains global appication state for your entire app it's also the main object that the operating system uses
to interact with your app
*/
class ElectionApplication : Application() {
    val electionRepository: ElectionRepository
        get() = ServiceLocator.provideElectionRepository(this)
}