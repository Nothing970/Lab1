package com.pair;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class whiteTest3 {
  private Hashtable<String, Graph_vertex> g = MainClass.createDirectedGraph("whiteTest.txt");

  @Before
  public void setUp() throws Exception {
    /* 设置起始单词 */
    MainClass.StartWord = "a";
  }

  @Test
  public void test() {
    // 集合匹配符
    List<String> results = new ArrayList<String>();
    results.add("b");
    results.add("c");
    results.add("d");
    String testResult = MainClass.randomWalk(g);
    // hasItem：results数组中含有testResult时，测试通过
    assertThat(results, hasItem(testResult));
    // assertEquals("e", testResult);// 不影响运行是否通过，只用来观察允许的结果
  }

}
