package com.pmj.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pmj.data.ApiService
import com.pmj.domain.model.Dining
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DiningRemoteDataSourceTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var retrofit: Retrofit

    @Mock
    lateinit var apiService: ApiService

    private lateinit var remoteDataSource: DiningRemoteDataSource

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
        remoteDataSource = DiningRemoteDataSource(apiService, retrofit)
    }

    @Test
    fun `should return success response when fetchDining is called`() = runBlocking {
        //GIVEN
        val givenDiningList = testDataList
        Mockito.`when`(apiService.getDining()).thenReturn(Response.success(givenDiningList))
        //WHEN
        val fetchedDiningList = remoteDataSource.fetchDining()
        //THEN
        assert(fetchedDiningList.data?.size == givenDiningList.size)
    }

    @Test
    fun `should return Unknown Error response when fetchDining is called`() = runBlocking {
        //GIVEN
        val mockitoException = MockitoException("Unknown Error")
        Mockito.`when`(apiService.getDining()).thenThrow(mockitoException)
        //WHEN
        val fetchedDiningList = remoteDataSource.fetchDining()
        //THEN
        assert(fetchedDiningList.message == "Unknown Error")
    }

    @Test
    fun `should return Server Error response when fetchDining is called`() = runBlocking {
        //GIVEN
        Mockito.`when`(apiService.getDining())
            .thenReturn(Response.error(500, "".toResponseBody()))
        //WHEN
        val fetchedDiningList = remoteDataSource.fetchDining()
        //THEN
        assert(fetchedDiningList.message == "Unknown Error")
    }
}