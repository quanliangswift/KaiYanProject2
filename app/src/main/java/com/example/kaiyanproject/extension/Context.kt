package com.example.kaiyanproject.extension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.kaiyanproject.KaiYanApplication

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = KaiYanApplication.context.packageName + "_preferences",
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context,
                KaiYanApplication.context.packageName + "_preferences"
            )
        )
    })