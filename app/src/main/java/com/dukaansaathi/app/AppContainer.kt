package com.dukaansaathi.app

import com.dukaansaathi.data.repository.InMemoryDukaanRepository
import com.dukaansaathi.domain.repository.DukaanRepository

/** Single place that builds the app's dependencies. Swap the repository here when a backend arrives. */
object AppContainer {
    val repository: DukaanRepository by lazy { InMemoryDukaanRepository() }

    /** True once someone has signed in during this run, so the app opens on Home instead of Login. */
    fun isSignedIn(): Boolean = repository.phone().isNotEmpty()
}
