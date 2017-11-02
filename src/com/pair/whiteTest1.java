package com.pair;

import static org.junit.Assert.assertEquals;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

public class whiteTest1 {
  private Hashtable<String, Graph_vertex> g = MainClass.createDirectedGraph("whiteTest.txt");

  @Before
  public void setUp() throws Exception {
    /* 设置起始单词 */
    MainClass.StartWord = "e";
  }

  @Test
  public void test() {
    assertEquals("No next", MainClass.randomWalk(g));
  }

}
