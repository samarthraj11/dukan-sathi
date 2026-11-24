package com.dukaansaathi.feature.account.presentation

import androidx.lifecycle.ViewModel
import com.dukaansaathi.core.model.DistributorStatus
import com.dukaansaathi.domain.repository.DukaanRepository
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class AccountViewModel(private val repository: DukaanRepository) : ViewModel(), OrbitContainerHost<AccountState, AccountState, AccountSideEffect> {
    override val container = orbitContainer<AccountState, AccountSideEffect>(
        AccountState(repository.phone(), repository.distributors().filter { it.status == DistributorStatus.Active }),
    )

    fun logOut() = intent {
        repository.signOut()
        postSideEffect(AccountSideEffect.LoggedOut)
    }
}
