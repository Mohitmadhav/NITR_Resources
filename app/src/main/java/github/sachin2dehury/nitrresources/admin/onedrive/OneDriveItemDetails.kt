package github.sachin2dehury.nitrresources.admin.onedrive

import com.squareup.moshi.Json

data class OneDriveItemDetails(
    @field:Json(name = "@content.downloadUrl") val url: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "parentReference") val parent: ItemParent,
    @field:Json(name = "file") val type: ItemMime,
    @field:Json(name = "size") val size: Long
)