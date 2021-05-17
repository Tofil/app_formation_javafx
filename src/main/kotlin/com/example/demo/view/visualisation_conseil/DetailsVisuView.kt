package com.example.demo.view.visualisation_conseil

import com.example.demo.controller.CSVController
import com.example.demo.controller.EtudiantController
import com.example.demo.controller.MainController
import com.example.demo.model.UE
import com.example.demo.model.UESuivi
import javafx.beans.property.SimpleStringProperty
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import tornadofx.*

class DetailsVisuView : View("Visualisation et conseil") {
    private val ctrl: MainController by inject()
    private val etuCtrl: EtudiantController by inject()
    private val  csvController: CSVController by inject()

    private val ueSuivisValide = SortedFilteredList(ctrl.ueSuivis)
    private val ueSuivisEnCour = SortedFilteredList(ctrl.ueSuivis)
    private val ueSuivisPrereq = SortedFilteredList(ctrl.ues)

    private var filterMention = SimpleStringProperty("")
    private var filterParcours = SimpleStringProperty("")


    override val root = borderpane {
        left {
            vbox {
                label("Nom :")
                label(etuCtrl.selectedEtu.nom)
                label("Prenom :")
                label(etuCtrl.selectedEtu.prenom)
                label("Mention :")
                label(etuCtrl.selectedEtu.formation.mention)
                label("Parcours :")
                label(etuCtrl.selectedEtu.formation.parcours)
                button("Save") {
                    setOnAction {
                        csvController.csvWriter()
                    }
                }
                minWidth = 100.0
                paddingLeft = 20
            }
        }
        center {
            tabpane {
                tab("UE Validées") {
                    tableview(ueSuivisValide) {
                        column<UESuivi, String>("Code UE") { it.value.ueProperty.value.codeProperty }
                        column("UE Validée", UESuivi::valide)
                        column("Année", UESuivi::annee)
                        column("Semestre Pair", UESuivi::semestrePair)
                    }
                }
                tab("UE Suivies") {
                    tableview(ueSuivisEnCour) {
                        column<UESuivi, String>("Code UE") { it.value.ueProperty.value.codeProperty }
                        column("En Cour", UESuivi::enCour)
                        column("Année", UESuivi::annee)
                        column("Semestre Pair", UESuivi::semestrePair)
                    }
                }
                tab("UE avec Prérequis") {
                    vbox {
                        form {
                            fieldset {
                                field("Filtrer par mention") {
                                    textfield(filterMention).setOnAction { ueSuivisPrereq.refilter() }
                                }
                                field("Filtrer par parcours") {
                                    textfield(filterParcours).setOnAction { ueSuivisPrereq.refilter() }
                                }
                            }
                        }
                        tableview(ueSuivisPrereq) {
                            column("Code", UE::code)
                            column("Crédits", UE::nbCredits)
                            column<UE, String>("Mention") { if (it.value.formation != null) it.value.formation.mentionProperty else SimpleStringProperty() }
                            column<UE, String>("Parcours") { if (it.value.formation != null) it.value.formation.parcoursProperty else SimpleStringProperty() }

                        }
                    }
                }
            }
        }
        minWidth = 700.0
        minHeight = 500.0

    }

    init {
        ueSuivisValide.predicate = {
            etuCtrl.selectedEtu == it.etu && it.valide
        }
        ueSuivisEnCour.predicate = {
            etuCtrl.selectedEtu == it.etu && it.enCour
        }
        ueSuivisPrereq.predicate = { ue ->
            val uesEtu = ctrl.ueSuivis.filter { ueSuivi ->
                etuCtrl.selectedEtu == ueSuivi.etu
            }.map{ueSuivi->ueSuivi.ue}

            val uesEtuPreq = ctrl.ues.filter { ue-> ue.listeUePrereq!=null && ue.listeUePrereq.any(uesEtu::contains)}
            uesEtuPreq.contains(ue) &&
            (
                    ue.formation == null || (
                            ue.formation != null &&
                                    (ue.formation.mention.contains(filterMention.value)) &&
                                    (ue.formation.parcours.contains(filterParcours.value))
                            )
                    )

        }
    }
    override fun onBeforeShow() {
        super.onBeforeShow()
        val jMetro = JMetro(Style.LIGHT)
        if (currentStage != null) jMetro.scene = currentStage?.scene
    }
}
