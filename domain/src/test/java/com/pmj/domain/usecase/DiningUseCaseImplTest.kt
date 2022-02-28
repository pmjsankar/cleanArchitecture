package com.pmj.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import com.pmj.domain.repository.DiningRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DiningUseCaseImplTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var diningRepository: DiningRepository
    private lateinit var diningUseCase: DiningUseCaseImpl

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
        diningUseCase = DiningUseCaseImpl(diningRepository)
    }

    @Test
    fun `should return success when diningUseCase is executed`() = runBlocking {
        //GIVEN
        val inputFlow = flowOf(Output.success(testDataList))
        Mockito.`when`(diningRepository.fetchDining()).thenReturn(inputFlow)
        //WHEN
        val output = diningUseCase.execute().toList()
        //THEN
        assert(output[0].data?.size == 1)
    }

}