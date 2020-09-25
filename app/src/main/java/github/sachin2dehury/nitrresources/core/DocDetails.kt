package github.sachin2dehury.nitrresources.core

import github.sachin2dehury.nitrresources.component.AppCore
import github.sachin2dehury.nitrresources.component.AppCore.PDF

data class DocDetails(
    var name: String = "temp",
    var subCode: Int = 0,
    var subName: String = "temp",
    var contributor: String = AppCore.firebaseAuth.currentUser!!.email!!,
    var time: Long = 0,
    var size: Double = 0.0,
    var url: String = "url",
    var type: String = PDF
)