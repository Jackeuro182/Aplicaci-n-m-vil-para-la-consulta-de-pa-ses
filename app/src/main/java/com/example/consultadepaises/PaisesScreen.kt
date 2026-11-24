package com.example.consultadepaises

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun PaisesAppScreen(viewModel: PaisesViewModel = viewModel()) {
    val paises by viewModel.paises.observeAsState(emptyList())
    val paisSeleccionado by viewModel.paisSeleccionado.observeAsState(null)
    val error by viewModel.mensajeError.observeAsState(null)

    LaunchedEffect(Unit) {
        viewModel.cargarPaises()
    }

    if (paisSeleccionado != null) {
        BackHandler {
            viewModel.limpiarPaisSeleccionado()
        }
        BanderaScreen(
            pais = paisSeleccionado!!,
            onVolver = { viewModel.limpiarPaisSeleccionado() }
        )
    } else {
        ListaPaisesScreen(
            paises = paises,
            error = error,
            onPaisClick = { viewModel.seleccionarPais(it) }
        )
    }
}

@Composable
private fun ListaPaisesScreen(
    paises: List<PaisItem>,
    error: String?,
    onPaisClick: (PaisItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(
            text = "Lista de países. Retrofit-Coil",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 12.dp)
        )

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(paises, key = { it.nombre }) { pais ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPaisClick(pais) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        Text(
                            text = pais.nombre,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = pais.population.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    AsyncImage(
                        model = pais.banderaUrl,
                        contentDescription = "Bandera de ${pais.nombre}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(72.dp)
                            .height(46.dp)
                    )
                }
                HorizontalDivider(thickness = 0.5.dp)


            }
        }
    }
}

@Composable
private fun BanderaScreen(
    pais: PaisItem,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = pais.banderaUrl,
            contentDescription = "Bandera de ${pais.nombre}",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = pais.nombre, style = MaterialTheme.typography.headlineSmall)
        Text(text = "Población: ${pais.population}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onVolver) {
            Text("Volver a la lista")
        }
    }
}