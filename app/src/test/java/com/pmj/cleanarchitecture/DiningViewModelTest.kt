package com.pmj.cleanarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pmj.cleanarchitecture.home.DiningViewModel
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import com.pmj.domain.usecase.DiningUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
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
class DiningViewModelTest {

    @Mock
    private lateinit var diningUseCase: DiningUseCase
    private lateinit var diningViewModel: DiningViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineDispatcherRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    fun runBlockingMainTest(block: suspend TestCoroutineScope.() -> Unit): Unit =
        coroutineDispatcherRule.testCoroutineDispatcher.runBlockingTest(block)

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
        diningViewModel = DiningViewModel(diningUseCase)
    }

    @Test
    fun `should return success response when fetchDining is called from viewmodel`() =
        runBlockingMainTest {
            //GIVEN
            val flowQuestions = flowOf(Output.success(testDataList))

            //WHEN
            Mockito.doReturn(flowQuestions).`when`(diningUseCase).execute()
            diningViewModel.fetchDining()

            //THEN
            assert(1 == diningViewModel.diningList.value?.data?.size)
        }
}