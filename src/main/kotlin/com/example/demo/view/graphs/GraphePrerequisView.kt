package com.example.demo.view.graphs

import com.example.demo.controller.EtudiantController
import com.example.demo.controller.MainController
import com.example.demo.model.UE
import com.example.demo.model.UESuivi
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import org.graphstream.ui.fx_viewer.FxDefaultView
import org.graphstream.ui.fx_viewer.FxViewer
import org.graphstream.ui.layout.springbox.BarnesHutLayout
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
    var graph: Graph = SingleGraph("graph")
    private var algo: BarnesHutLayout = SpringBox()
    private var viewer: FxViewer = FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD)
    private var ueFormation: List<UE> = listOf()
    private lateinit var selectedUes: FilteredList<UESuivi>
    val ctrl: MainController by inject()
    val etuCtrl: EtudiantController by inject()

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
        createGraph()
    }

    private fun createGraph() {
        print("Graphe")
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer")
        graph = SingleGraph("graph")
        loadCSS()
        loadGraph()
        viewer = FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD)
        algo.stabilizationLimit = 1E-30
        algo.quality = 4.0
        algo.force = 1.7
        viewer.enableAutoLayout(algo)
        val view = viewer.addDefaultView(true) as FxDefaultView
        this.add(view)
    }

    private fun loadGraph() {
        selectedUes = ctrl.ueSuivis.filtered { ue -> ue.etu.equals(etuCtrl.selectedEtu) }
        ueFormation = ctrl.ues.filter { ue -> ue.formation == etuCtrl.selectedEtu.formation }
        for (ue in ueFormation) {
            val node = graph.addNode(ue.code)
            node.setAttribute("ui.label", ue.code)
            node.setAttribute("layout.weight", 100)
            node.setAttribute("ui.class", "impossible")
            drawPrerequis(ue, ue.listeUePrereq)
        }
        for (ue in selectedUes.filter { ue -> ue.ue.formation == etuCtrl.selectedEtu.formation }) {
            val node = graph.getNode(ue.ue.code)
            node.setAttribute("ui.class", "suivie")
            if (ue.valide) {
                node.setAttribute("ui.class", "valide")
            }
        }
        //setPossible()
        setDepart()
    }

    private fun setDepart() {
        for (node in graph.nodes()) {
            if (node.enteringEdges().count() == 0L) {
                node.setAttribute("ui.class", "depart")
            }
        }
    }

    private fun setPossible() {
//
//            if (node.hasAttribute("ui.class")) {
//                //println(node.getAttribute("ui.class"))
//
//                var o = true
//                for (enteringNode in node.enteringEdges()) {
//                    val valid =
//                        enteringNode.hasAttribute("ui.class") && ((enteringNode.getAttribute("ui.class") as String) == "valide" || (enteringNode.getAttribute(
//                            "ui.class"
//                        ) as String) == "suivie")
//                    if (!valid) {
//                        o = false;
//                    }
//                }
//                if (o){
//                    node.setAttribute("ui.class", "impossible")
//                }
//
//                println(o)
//            }
//        }
        for (ue in ctrl.ues.filter { ue -> ue.formation == etuCtrl.selectedEtu.formation }) {
            println(ue.listeUePrereq.size)
            println(ue.code)
            val ueSuiviOuValide = selectedUes.find { ueSuivie -> ueSuivie.ue.code == ue.code }
            var possible = true
            ue.listeUePrereq.forEach { uePrerequie ->
                val ueSuivie = selectedUes.find { ueSuivi -> uePrerequie.equals(ueSuivi) }
                if ((ueSuivie != null && (ueSuivie.valide || ueSuivie.enCour))) {
                    possible = false
                }
            }
            //val a = graph.getNode(ue.code)?.enteringEdges()?.anyMatch { t -> t.getAttribute("ui.class") == "possible" }
            possible = possible && ueSuiviOuValide == null //&& !a!!

            if (possible) {
                graph.getNode(ue.code)?.setAttribute("ui.class", "possible")
            }
        }
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
