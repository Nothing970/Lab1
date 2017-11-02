package com.pair;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class whiteTest2 {
  private Hashtable<String, Graph_vertex> g = MainClass.createDirectedGraph("whiteTest.txt");

  @Before
  public void setUp() throws Exception {
    /* 设置起始单词 */
    MainClass.StartWord = "a";

    /* 删除a的第一个后继节点 */
    Graph_vertex tmpVertex = g.get("a");
    tmpVertex.links.weight = 0;
    tmpVertex.children--;
  }

  @Test
  public void test() {
    // 集合匹配符
    List<String> results = new ArrayList<String>();
    results.add("b");
    results.add("c");
    results.add("d");
    String testResult = MainClass.randomWalk(g);
    // hasItem：Iterable变量中含有指定元素时，测试通过
    assertThat(results, hasItem(testResult));
    // assertEquals("b", testResult);// 这行的作用只是某一次的运行结果到底是什么
  }

}
