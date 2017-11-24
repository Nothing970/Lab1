package com.test;

import static org.junit.Assert.assertEquals;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.pair.DirectedGraph;
import com.pair.Graph_vertex;

public class whiteTest1 {
  private Hashtable<String, Graph_vertex> g = DirectedGraph.createDirectedGraph("whiteTest.txt");

  @Before
  public void setUp() throws Exception {
    /* 设置起始单词 */
    DirectedGraph.setStartWord("e");
  }

  @Test
  public void test() {
    assertEquals("No next", DirectedGraph.randomWalk(g));
  }

}
