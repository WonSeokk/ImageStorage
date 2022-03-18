package com.gmail.wwon.seokk.imagestorage.data.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageList(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("documents") val images: List<Image>
): Serializable