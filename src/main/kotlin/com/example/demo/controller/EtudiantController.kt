package com.example.demo.controller

import com.example.demo.app.Styles
import com.example.demo.model.*
import javafx.collections.FXCollections
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.importStylesheet

class EtudiantController : Controller() {
    val mainCtrl: MainController by inject()
    var selectedEtu: Etudiant = Etudiant("", "", "", Formation("", ""))







}