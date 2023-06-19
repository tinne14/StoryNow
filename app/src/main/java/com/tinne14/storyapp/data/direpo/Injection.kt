package com.tinne14.storyapp.data.direpo


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tinne14.storyapp.data.remote.api.ApiConfig
import com.tinne14.storyapp.data.database.StoryDatabase
import com.tinne14.storyapp.ui.pref.LoginPreference

object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): Repository {
        val preference = LoginPreference.getInstance(dataStore)
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return Repository(preference, database, apiService)
    }
}