package com.pmj.cleanarchitecture.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import com.pmj.domain.usecase.DiningUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiningViewModel @Inject constructor(private val useCase: DiningUseCase) : ViewModel() {

    private val _diningList = MutableLiveData<Output<List<Dining>>>()
    val diningList: LiveData<Output<List<Dining>>> = _diningList

    init {
        fetchDining()
    }

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
}