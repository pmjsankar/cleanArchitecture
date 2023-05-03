package com.pmj.cleanarchitecture.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pmj.domain.model.Dining
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseDbViewModel @Inject constructor() : ViewModel() {

    private val _diningViewState = MutableLiveData<DiningViewState>()
    val diningViewState: LiveData<DiningViewState> = _diningViewState

    /**
     * Method to fetch the dining data.
     */
    fun fetchDining() {
        _diningViewState.postValue(DiningViewState.Loading)
        viewModelScope.launch {
            val database = Firebase.database.reference
            val diningDbList = arrayListOf<Dining>()
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        postSnapshot.getValue(Dining::class.java)?.let { dining ->
                            diningDbList.add(dining)
                        }
                    }
                    _diningViewState.postValue(DiningViewState.Success(diningDbList))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.message)
                    _diningViewState.postValue(DiningViewState.Error(databaseError.message))
                }
            })
        }
    }
}

sealed class DiningViewState {
    object Loading : DiningViewState()
    data class Error(val message: String) : DiningViewState()
    data class Success(val data: List<Dining>) : DiningViewState()
}