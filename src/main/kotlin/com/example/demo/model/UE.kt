package com.example.demo.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class UE(code: String, nbCredit: Int=0, listeUePrereq: List<UE>? = null) {
    val codeProperty = SimpleStringProperty(code)
    var code by codeProperty

    val nbCreditsProperty = SimpleIntegerProperty(nbCredit)
    var nbCredits by nbCreditsProperty

    var listeUePrereq: List<UE>? = listeUePrereq




}
