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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiningViewModel @Inject constructor(
    private val useCase: DiningUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _diningList = MutableLiveData<Output<List<Dining>>>()
    val diningList: LiveData<Output<List<Dining>>> = _diningList
    private val _selectedTheme = MutableLiveData<Int>()
    val selectedTheme: LiveData<Int> = _selectedTheme

    /**
     * Method to fetch the dining data.
     */
    fun fetchDining() {
        viewModelScope.launch {
            useCase.execute().collectLatest { diningList ->
                _diningList.postValue(diningList)
            }
        }
    }

    /**
     * Method to get current mode of theme from preferences data store
     */
    fun getCurrentMode() {
        viewModelScope.launch {
            userPreferences.selectedTheme.collect { themeIndex ->
                _selectedTheme.postValue(themeIndex)
            }
        }
    }

    /**
     * Method to store user selected mode of theme into preferences data store
     */
    fun toggleNightMode(selectedTheme: Int) {
        viewModelScope.launch {
            userPreferences.saveTheme(selectedTheme = selectedTheme)
        }
    }
}