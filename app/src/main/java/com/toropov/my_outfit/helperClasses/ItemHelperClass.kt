package com.toropov.my_outfit.helperClasses

import android.graphics.drawable.GradientDrawable
import android.net.Uri
import com.google.firebase.database.DatabaseReference

class ItemHelperClass(_postKey: String, _check: Boolean, _image: String, _itemShop: String, _itemName: String, _itemPrice: Double, _itemHref: String) {
    var postKey: String = _postKey
    var check: Boolean = _check
    val image: String = _image
    val itemShop: String = _itemShop
    val itemName: String = _itemName
    val itemPrice: Double = _itemPrice
    val itemHref: String = _itemHref


}