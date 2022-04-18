package ru.harlion.pomodorolist

import android.app.Application
import com.google.gson.Gson
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.data.billing.BillingClientWrapper

class AppApplication: Application() {

    val clientWrapper: BillingClientWrapper by lazy {
        BillingClientWrapper(this)
    }

    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }

    companion object {
        val gson = Gson()
    }
}