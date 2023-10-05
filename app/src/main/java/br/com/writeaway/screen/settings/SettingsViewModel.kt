package br.com.writeaway.screen.settings

import androidx.lifecycle.MutableLiveData
import br.com.writeaway.base.BaseViewModel
import br.com.writeaway.domain.repositories.settings.SettingsRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepositoryContract) :
    BaseViewModel() {

    val textSize = MutableLiveData<Float>()
    val orderType = MutableLiveData<Int>()
    val layoutManager = MutableLiveData<String>()

    fun fetchTextSize() {
        defaultLaunch {
            settingsRepository.fetchTextSize().collect { textSize ->
                this.textSize.postValue(textSize)
            }
        }
    }

    fun setTextSize(textSize: Float) {
        defaultLaunch {
            settingsRepository.setTextSize(textSize)
        }
        fetchTextSize()
    }


    fun fetchOrderType() {
        defaultLaunch {
            settingsRepository.fetchOrder().collect { orderType ->
                this.orderType.postValue(orderType)
            }
        }
    }

    fun setOrderType(orderType: Int) {
        defaultLaunch { settingsRepository.setOrder(orderType) }
        fetchOrderType()
    }

    fun fetchLayoutManager() {
        defaultLaunch {
            settingsRepository.fetchLayoutManager().collect { layoutManager ->
                this.layoutManager.postValue(layoutManager)
            }
        }
    }

    fun setLayoutManager(layoutManager: String) {
        defaultLaunch { settingsRepository.setLayoutManager(layoutManager) }
        fetchLayoutManager()
    }
}