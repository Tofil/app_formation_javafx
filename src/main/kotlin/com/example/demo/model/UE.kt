package com.example.demo.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.getValue
import tornadofx.setValue

class UE(code: String, nbCredit: Int=0,formation: Formation? = null, listeUePrereq: ObservableList<UE>? = null) {
    val codeProperty = SimpleStringProperty(code)
    var code by codeProperty

    val nbCreditsProperty = SimpleIntegerProperty(nbCredit)
    var nbCredits by nbCreditsProperty

    var formationProperty = SimpleObjectProperty<Formation>(formation)
    var formation by formationProperty

    val listeUePrereqProperty = SimpleListProperty<UE>(listeUePrereq)
    var listeUePrereq by listeUePrereqProperty


}
