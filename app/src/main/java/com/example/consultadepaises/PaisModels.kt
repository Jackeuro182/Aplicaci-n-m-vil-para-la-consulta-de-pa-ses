package com.example.consultadepaises



import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaisesResponse(
    val data: DataObjects = DataObjects()
)

data class DataObjects(
    val objects: List<PaisItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PaisItem(
    val names: NombrePais,
    val population: Long = 0L,
    val codes: CodigosPais? = null
) {
    val nombre: String
        get() = names.common

    val banderaUrl: String
        get() {
            val iso2 = codes?.alpha_2?.lowercase()
            return if (!iso2.isNullOrBlank()) {
                "https://flags.restcountries.com/v5/w320/$iso2.png"
            } else {
                ""
            }
        }
}

@JsonClass(generateAdapter = true)
data class NombrePais(
    val common: String
)

@JsonClass(generateAdapter = true)
data class CodigosPais(
    val alpha_2: String? = null
)