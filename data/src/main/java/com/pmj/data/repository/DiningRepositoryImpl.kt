package com.pmj.data.repository


import com.pmj.data.remote.DiningRemoteDataSource
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import com.pmj.domain.repository.DiningRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Implementation of DiningRepository
 * @param remoteDataSource the object of remote data source
 */
internal class DiningRepositoryImpl @Inject constructor(
    private val remoteDataSource: DiningRemoteDataSource
) : DiningRepository {
    override fun fetchDining(): Flow<Output<List<Dining>>> {
        return flow {
            emit(Output.loading())
            val result = remoteDataSource.fetchDining()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}