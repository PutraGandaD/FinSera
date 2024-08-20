package com.finsera.presentation

import com.phanna.emv_qr_code.MerchantPresentedDecoder
import org.json.JSONObject

fun main() {
//    val merchantPresentedMode = MerchantPresentedDecoder.decode(
//        "00020101021126670016COM.NOBUBANK.WWW01189360050300000507850214123100000493370303UME51440014ID.CO.QRIS.WWW0215ID20200176137420303UME5204866153033605802ID5919DOMPET DHUAFA ZAKAT6015Jakarta Selatan61051211062070703A016304AF21", false
//    )
//
//    println("${merchantPresentedMode.merchantAccountInformation.replace("+", " ")}")

    val merchant = MerchantPresentedDecoder.decode(
        "hQVDUFYwMWGBk08HoAAABgIgIFAHUVJJU0NQTVoKk2AACRIAJjmZD18gCkhVU0FJTiBXSURfLQRpZGVuX1AgbWFpbHRvOmh1c2FpbndpZGF5YXQxMkBnbWFpbC5jb22fJQKZkGM3n3Q0NjQ2MzJfampEUV43MTUyMi0yMTAyMDIySUI3Nzg4MThoaGhWaC03NDk2MTQ3NTA4NDAtTg==", false
    )
    if(merchant.merchantAccountInformation.isNullOrEmpty()) {
        //println("${merchant.merchantAccountInformation.replace("+", " ")}")
        println("Lol")
    } else {
        println("Nuh")
    }


}