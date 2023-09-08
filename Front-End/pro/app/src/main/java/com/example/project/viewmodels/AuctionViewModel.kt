package com.example.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.api.AuctionDetailResponseDTO
import com.example.project.api.AuctionFilterDTO
import com.example.project.api.AuctionService
import com.example.project.api.RegisterAuctionDTO
import com.example.project.sharedpreferences.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuctionViewModel @Inject constructor(
    private val service: AuctionService,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : ViewModel() {

    // 글 불러오기?
    private val _auctionItems = MutableStateFlow<List<AuctionItemData>>(emptyList())
    val auctionItems: StateFlow<List<AuctionItemData>> = _auctionItems

    private val _totalItems = MutableStateFlow<Int>(0)
    val totalItems: StateFlow<Int> = _totalItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // 글 상세보기
    private val _auctionDetail = MutableStateFlow<AuctionDetailResponseDTO?>(null)
    val auctionDetail: StateFlow<AuctionDetailResponseDTO?> = _auctionDetail

    private var currentPage = 1
    private var currentFilter = AuctionFilterDTO("", "", "", null, currentPage)

    init {
        fetchAuctionItems()
    }

    fun changePage(page: Int) {
        currentPage = page
        currentFilter = currentFilter.copy(page = currentPage)
        fetchAuctionItems()
    }

    fun applyFilter(filter: AuctionFilterDTO) {
        currentFilter = filter.copy(page = 1)
        currentPage = 1
        fetchAuctionItems()
    }

    fun searchItems(query: String) {
        currentFilter = currentFilter.copy(searchQuery = query, page = 1)
        currentPage = 1
        fetchAuctionItems()
    }

    // 경매글 리스트 불러오기
    private fun fetchAuctionItems() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.getAllAuctionItems(currentFilter)

                if (response.isSuccessful && response.body() != null) {
                    _auctionItems.value = response.body()!!.items
                    _totalItems.value = response.body()!!.total
                } else {
                    _error.value = "Failed to load data: ${response.message()}"
                    _auctionItems.value = getSampleData()
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
                _auctionItems.value = getSampleData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 경매글 등록
    fun registerAuctionItem(upperLimit: Int, lowerLimit: Int, postContent: String, gifticonIdx: Long) {
        val userIdx = sharedPreferencesUtil.getUserId()

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.registerAuctionItem(RegisterAuctionDTO(upperLimit, lowerLimit, postContent, gifticonIdx, userIdx))

                if (response.isSuccessful && response.body() != null) {
                    // TODO: Handle successful registration logic if needed.
                } else {
                    _error.value = "Failed to register item: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 경매글 상세보기
    fun fetchAuctionDetail(auctionIdx: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.getAuctionDetail(auctionIdx)

                if (response.isSuccessful && response.body() != null) {
                    _auctionDetail.value = response.body()
                } else {
                    _error.value = "Failed to load detail: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
}


// API response를 위한 데이터 클래스
data class AuctionItemData(
    val index: Long,
    val image: String,
    val name: String,
    val gifticonTime: String,
    val auctionTime: String,
    val popular: String,
    val upperprice: Int,
    val nowprice: Int
)

// 상세보기용 데이터 클래스
data class ActuonVidData(
    val auctionBidIdx: Long,
    val auctionBidPrice: Int,
    val auctionRegistedDate: String,
    val auctionBidStatus: String,
    val userIdx: Long,
    val auctionIdx: Long,
)

// 인터넷 미연결 샘플데이터
private fun getSampleData(): List<AuctionItemData> {
    return listOf(
        AuctionItemData(1,"image1", "Item1", "2일", "5시간", "서버미연결", 1000, 800),
        AuctionItemData(2,"image2", "Item2", "3일", "4시간", "중간", 2000, 1500),
        AuctionItemData(3,"image3", "Item3", "1일", "2시간", "낮음", 3000, 2500),
        AuctionItemData(4,"image4", "Item4", "4일", "6시간", "높음", 40000, 3500),
        AuctionItemData(5,"image5", "Item5", "5일", "3시간", "중간", 5000, 4500)
    )
}