package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiFriendsHelper
import pl.skomunikacja.synclyapp.model.FriendRequestData
import pl.skomunikacja.synclyapp.model.FriendUserData

class FriendsViewModel : ViewModel() {
    private val apiFriendsHelper = ApiFriendsHelper(RetrofitClient.apiFriendsService)

    private val _friends = MutableStateFlow<List<FriendUserData>>(listOf())
    val friends = _friends.asStateFlow()

    private val _userSentFriendRequests = MutableStateFlow<List<FriendRequestData>>(listOf())
    val userSentFriendRequests = _userSentFriendRequests.asStateFlow()

    private val _userPendingFriendRequests = MutableStateFlow<List<FriendRequestData>>(listOf())
    val userPendingFriendRequests = _userPendingFriendRequests.asStateFlow()


    fun fetchFriendsData(userId: Long) {
        viewModelScope.launch {
            val userFriendsList = apiFriendsHelper.getUserFriendsList(userId);
            _friends.value = userFriendsList;
        }
    }

    fun fetchUserSentFriendRequests(userId: Long) {
        viewModelScope.launch {
            val sentFriendRequests = apiFriendsHelper.getUserSentFriendRequests(userId)
            _userSentFriendRequests.value = sentFriendRequests
        }
    }

    fun fetchUserPendingFriendRequests(userId: Long) {
        viewModelScope.launch {
            val userPendingFriendRequests = apiFriendsHelper.getUserPendingFriendRequests(userId)
            _userPendingFriendRequests.value = userPendingFriendRequests
        }
    }


    fun removeSentFriendRequest(receiverId: Long, userId: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val result = apiFriendsHelper.deleteSentFriendRequest(receiverId, userId)
            if (result) {
                val foundRequest = _userSentFriendRequests.value.find { requestData ->
                    requestData.requester.userId == userId && requestData.receiver.userId == receiverId
                }

                if (foundRequest != null) {
                    _userSentFriendRequests.value -= foundRequest
                    onSuccess()
                }
            } else {
                onError()
            }
        }
    }

    fun acceptPendingFriendRequest(requestId: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val result = apiFriendsHelper.acceptFriendRequest(requestId)
            if (result) {
                val foundRequest = _userPendingFriendRequests.value.find { requestData ->
                    requestData.id == requestId
                }

                if (foundRequest != null) {
                    _userPendingFriendRequests.value -= foundRequest
                    onSuccess()
                }
            } else {
                onError()
            }
        }
    }

    fun declinePendingFriendRequest(requestId: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val result = apiFriendsHelper.declineFriendRequest(requestId)
            if (result) {
                val foundRequest = _userPendingFriendRequests.value.find { requestData ->
                    requestData.id == requestId
                }

                if (foundRequest != null) {
                    _userPendingFriendRequests.value -= foundRequest
                    onSuccess()
                }
            } else {
                onError()
            }
        }
    }
}