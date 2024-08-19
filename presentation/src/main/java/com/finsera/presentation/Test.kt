package com.finsera.presentation

import com.phanna.emv_qr_code.MerchantPresentedDecoder

fun main() {
    val merchantPresentedMode = MerchantPresentedDecoder.decode(
        "00020101021126670016COM.NOBUBANK.WWW01189360050300000507850214123100000493370303UME51440014ID.CO.QRIS.WWW0215ID20200176137420303UME5204866153033605802ID5919DOMPET DHUAFA ZAKAT6015Jakarta Selatan61051211062070703A016304AF21", false
    )

    println("${merchantPresentedMode.merchantAccountInformation.replace("+", " ")}")
}