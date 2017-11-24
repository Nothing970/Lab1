package com.pair;

public class CreateDirectedGraphCtrl {
  public static void ctrl(String OriginPath) {
    DirectedGraph.setG(DirectedGraph.createDirectedGraph(OriginPath));
    DirectedGraph.showDirectedGraph(DirectedGraph.getG());

  }
}
