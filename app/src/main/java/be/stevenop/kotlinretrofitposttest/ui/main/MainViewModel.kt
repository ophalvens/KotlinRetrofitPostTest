package be.stevenop.kotlinretrofitposttest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.stevenop.kotlinretrofitposttest.network.MyApi
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the request status
    val status: LiveData<String> = _status

    init {
        setStatus()
    }

    public fun getProducten(){
        _getProducts()
    }
    /**
     * Haal de producten uit de lijst van de API
     * Opgelet : aangezien deze API verspreid is, is het mogelijk
     * dat andere studenten de producten verwijderd hebben.
     * In dat geval voeg je best nieuwe producten toe als je met
     * hetzelfde endpoint werkt.
     */
    private fun _getProducts() {
        viewModelScope.launch {
            val listResult = MyApi.retroFitService.getProducten()
            _status.value = listResult
        }
    }

    public fun login() {
        _login()
    }

    private fun _login() {
        viewModelScope.launch {
            val loginResult = MyApi.retroFitService.login("test", "test")
            _status.value = loginResult
        }
    }

    private fun setStatus() {
        _status.value = "Klik op 1 van de knoppen om te testen"
    }

}