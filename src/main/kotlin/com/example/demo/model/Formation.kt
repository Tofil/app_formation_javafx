package com.example.demo.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class Formation(mention: String, parcours: String) {
    val mentionProperty =  SimpleStringProperty(mention)
    var mention by mentionProperty

    val parcoursProperty = SimpleStringProperty(parcours)
    var parcours by parcoursProperty

}