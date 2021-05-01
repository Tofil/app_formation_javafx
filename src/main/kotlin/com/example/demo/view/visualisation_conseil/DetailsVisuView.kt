package com.example.demo.view.visualisation_conseil

import com.example.demo.controller.EtudiantController
import com.example.demo.controller.MainController
import com.example.demo.model.UE
import com.example.demo.model.UESuivi
import tornadofx.*

class DetailsVisuView : View("Visualisation et conseil") {
    val ctrl: MainController by inject()
    val etuCtrl: EtudiantController by inject()

    val ueSuivisValide = SortedFilteredList(ctrl.ueSuivis)
    val ueSuivisEnCour = SortedFilteredList(ctrl.ueSuivis)
    val ueSuivisPrereq = SortedFilteredList(ctrl.ues)


    override val root = borderpane {
        left{
            vbox{
                label("Nom :")
                label(etuCtrl.selectedEtu.nom)
                label("Prenom :")
                label(etuCtrl.selectedEtu.prenom)
                label("Mention :")
                label(etuCtrl.selectedEtu.formation.mention.nom)
                label("Parcours :")
                label(etuCtrl.selectedEtu.formation.nomParcour)

                minWidth = 100.0
                paddingLeft = 20
            }
        }
        center{
            tabpane{
                tab("UE Validées"){
                    tableview(ueSuivisValide){
                        column<UESuivi,String>("Code UE",  { it.value.ueProperty.value.codeProperty})
                        column("UE Validée",UESuivi::valide)
                        column("Année",UESuivi::annee)
                        column("Semestre Pair",UESuivi::semestrePair)
                    }
                }
                tab("UE Suivies"){
                    tableview(ueSuivisEnCour){
                        column<UESuivi,String>("Code UE",  { it.value.ueProperty.value.codeProperty})
                        column("En Cour",UESuivi::enCour)
                        column("Année",UESuivi::annee)
                        column("Semestre Pair",UESuivi::semestrePair)
                    }
                }
                tab("UE avec Prérequis"){
                    tableview(ueSuivisPrereq){
                        column("Code", UE::code)
                        column("Crédits", UE::nbCredits)

                    }
                }
            }
        }
        minWidth = 700.0
        minHeight = 500.0

    }
    init {
        ueSuivisValide.predicate = {
            etuCtrl.selectedEtu == it.etu && it.valide=="Oui"
        }
        ueSuivisEnCour.predicate = {
            etuCtrl.selectedEtu == it.etu && it.enCour=="Oui"
        }
//        ueSuivisPrereq.predicate = {
//            etuCtrletuCtrl.selectedEtu == it.etu &&
//        }
    }

}
