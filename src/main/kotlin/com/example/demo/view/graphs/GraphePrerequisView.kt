package com.example.demo.view.graphs

import com.example.demo.controller.EtudiantController
import com.example.demo.controller.MainController
import com.example.demo.model.UE
import com.example.demo.model.UESuivi
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import org.graphstream.graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.SingleGraph
import org.graphstream.ui.fx_viewer.FxDefaultView
import org.graphstream.ui.fx_viewer.FxViewer
import org.graphstream.ui.layout.springbox.implementations.SpringBox
import org.graphstream.ui.view.Viewer
import tornadofx.Fragment
import tornadofx.hbox
import tornadofx.label
import tornadofx.vbox
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths


class GraphePrerequisView : Fragment("Graphe des prérequis") {
    private lateinit var suiviesParEtu: FilteredList<UESuivi>
    val ctrl: MainController by inject()
    val etuCtrl: EtudiantController by inject()
    var graph: Graph = SingleGraph("graph")

    override val root = vbox {
        hbox {
            label("Bleu: suivie ")
            label("Vert : valide ")
            //label("Blanc : Possible")
            label("Rouge : impossible ")
            label("Orange : sans prérequis ")


        }
        System.setProperty("org.graphstream.ui", "javafx")
    }

    init {
        print("Graphe")
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer")
        graph = SingleGraph("graph")
        loadCSS()
        loadGraph()
        val viewer: Viewer = FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD)
        val algo = SpringBox()
        algo.stabilizationLimit = 1E-30
        algo.quality = 4.0
        algo.force = 1.7
        viewer.enableAutoLayout(algo)

        val view = viewer.addDefaultView(true) as FxDefaultView
        this.add(view)
    }

    private fun loadGraph() {
        suiviesParEtu = ctrl.ueSuivis.filtered { ue -> ue.etu.equals(etuCtrl.selectedEtu) }

        for (ue in ctrl.ues.filter { ue -> ue.formation == etuCtrl.selectedEtu.formation }) {
            val node = graph.addNode(ue.code)
            node.setAttribute("ui.label", ue.code)
            node.setAttribute("layout.weight", 100)
            node.setAttribute("ui.class", "impossible")
            node.setAttribute("ui.class", "impossible")
            drawPrerequis(ue, ue.listeUePrereq)
        }
        for (ue in suiviesParEtu.filter { ue -> ue.ue.formation == etuCtrl.selectedEtu.formation }) {
            val node = graph.getNode(ue.ue.code)
            node.setAttribute("ui.class", "suivie")
            if (ue.valide) {
                node.setAttribute("ui.class", "valide")
            }
        }
        setPossible()
    }

    private fun setPossible() {
        for (node: Node in graph.nodes()) {
            if (node.enteringEdges().count() == 0L) {
                node.setAttribute("ui.class", "depart")
            }
            if (node.hasAttribute("ui.class")) {
                //println(node.getAttribute("ui.class"))

                var o = true
                for (enteringNode in node.enteringEdges()) {
                    val valid =
                        enteringNode.hasAttribute("ui.class") && ((enteringNode.getAttribute("ui.class") as String) == "valide" || (enteringNode.getAttribute(
                            "ui.class"
                        ) as String) == "suivie")
                    if (!valid) {
                        o = false;
                    }
                }

                println(o)
            }
            if (node.enteringEdges().count() != 0L &&
                node.enteringEdges().allMatch { t ->
                    t.hasAttribute("ui.class") &&
                            (t.getAttribute("ui.class") as String == "valide"
                                    || t.getAttribute("ui.class") as String == "suivie")
                }
            ) {
                node.setAttribute("ui.class", "possible")
            }
        }
//        for (ue in ctrl.ues.filter { ue -> ue.formation == etuCtrl.selectedEtu.formation }) {
//            println(ue.listeUePrereq.size)
//            println(ue.code)
//            val ueSuivi = suiviesParEtu.find { ueSuivie -> ueSuivie.ue.code == ue.code }
//            var uePrerequisNonSuiviOuNonValide = false
//            ue.listeUePrereq.forEach { uePrerequie ->
//                if (suiviesParEtu.find { ueSuivi ->
//                        ((!ueSuivi.valide || !ueSuivi.enCour) || uePrerequie.equals(ueSuivi))
//                    } != null) {
//                    uePrerequisNonSuiviOuNonValide = true
//                }
//            }
//
//            if (!uePrerequisNonSuiviOuNonValide) {
//                graph.getNode(ue.code)?.setAttribute("ui.class", "possible")
//            }
//        }
    }


    private fun drawPrerequis(ueRoot: UE, listeUePrereq: ObservableList<UE>) {
        for (ue in listeUePrereq) {
            if (null == graph.getEdge(ue.code + "/" + ueRoot.code)) {
                val edge = graph.addEdge(ue.code + "/" + ueRoot.code, ue.code, ueRoot.code, true)
                edge.setAttribute("layout.weight", 10)
            }
        }
    }

    private fun loadCSS() {
        val cssURI = javaClass.classLoader.getResource("stylesheet.css")?.toURI()
        val lines = Files.readAllLines(Paths.get(cssURI), StandardCharsets.UTF_8)
        graph.setAttribute("ui.stylesheet", lines.joinToString("\n"))
    }


}
