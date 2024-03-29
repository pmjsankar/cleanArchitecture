package com.pmj.cleanarchitecture.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmj.cleanarchitecture.datastore.UserPreferences
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import com.pmj.domain.usecase.DiningUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiningViewModel @Inject constructor(
    private val useCase: DiningUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _diningList = MutableLiveData<Output<List<Dining>>>()
    val diningList: LiveData<Output<List<Dining>>> = _diningList
    private val _isNightMode = MutableLiveData<Boolean>()
    val isNightMode: LiveData<Boolean> = _isNightMode

    /**
     * Method to fetch the dining data.
     */
    fun fetchDining() {
        viewModelScope.launch {
            useCase.execute().collect { diningList ->
                _diningList.postValue(diningList)
            }
        }
    }

    /**
     * Method to get current mode of theme from preferences data store
     */
    fun getCurrentMode() {
        viewModelScope.launch {
            userPreferences.isNightMode.collect { nightMode ->
                _isNightMode.postValue(nightMode ?: false)
            }
        }
    }

    /**
     * Method to store user selected mode of theme into preferences data store
     */
    fun toggleNightMode() {
        viewModelScope.launch {
            _isNightMode.value?.let { userPreferences.toggleNightMode(!it) }
        }
    }
}