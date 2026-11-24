package com.example.consultadepaises



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consultadepaises.PaisItem
import com.example.consultadepaises.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaisesViewModel : ViewModel() {

    private val _paises = MutableLiveData<List<PaisItem>>(emptyList())
    val paises: LiveData<List<PaisItem>> = _paises

    private val _paisSeleccionado = MutableLiveData<PaisItem?>(null)
    val paisSeleccionado: LiveData<PaisItem?> = _paisSeleccionado

    private val _mensajeError = MutableLiveData<String?>(null)
    val mensajeError: LiveData<String?> = _mensajeError

    fun cargarPaises() {
        viewModelScope.launch {
            try {


                val lista = withContext(Dispatchers.IO) {
                    RetrofitClient.api.obtenerPaises().data.objects
                        .sortedBy { it.nombre }
                }

                _mensajeError.value = null
                _paises.value = lista
            } catch (e: Exception) {
                _mensajeError.value = "Error al obtener países: ${e.localizedMessage ?: "sin conexión"}"
            }
        }
    }

    fun seleccionarPais(pais: PaisItem) {
        _paisSeleccionado.value = pais
    }

    fun limpiarPaisSeleccionado() {
        _paisSeleccionado.value = null
    }
}