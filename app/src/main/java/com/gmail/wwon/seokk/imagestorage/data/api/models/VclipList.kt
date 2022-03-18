package com.gmail.wwon.seokk.imagestorage.data.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class VclipList(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("documents") val vclips: List<Vclip>
): Serializable