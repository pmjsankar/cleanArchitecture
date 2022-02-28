package com.pmj.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pmj.data.remote.DiningRemoteDataSource
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import com.pmj.domain.repository.DiningRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DiningRepositoryImplTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var diningRepository: DiningRepository

    @Mock
    lateinit var diningRemoteDataSource: DiningRemoteDataSource

    private val testDataList = listOf(
        Dining(
            offer = "30",
            address = "Test address",
            price = "100",
            imageUrl = "testUrl",
            rating = 4.5f,
            title = "Test title",
            desc = "Test description",
            timing = "10am to 10pm",
        )
    )

    @Before
    fun setUp() {
        diningRepository = DiningRepositoryImpl(diningRemoteDataSource)
    }

    @Test
    fun `should return success response when fetchDining is called from repository`() =
        runBlocking {
            //GIVEN
            val givenDiningList = testDataList
            val fetchedDiningList = Output.success(givenDiningList)
            val inputFlow = listOf(Output.loading(), Output.success(fetchedDiningList))
            `when`(diningRemoteDataSource.fetchDining()).thenReturn(fetchedDiningList)

            //WHEN
            val outputFlow = diningRepository.fetchDining().toList()

            //THEN
            assert(outputFlow.size == 2)
            assert(inputFlow[0] == outputFlow[0])
            assert(inputFlow[1].data?.data == outputFlow[1].data)
        }
}