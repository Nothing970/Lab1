package com.pair;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(
  value = Parameterized.class)
public class MainClassTest {
  private String word1;
  private String word2;
  private String retString;
  private Hashtable<String, Graph_vertex> g = MainClass.createDirectedGraph("test.txt");

  public MainClassTest(String word1, String word2, String retString)
  {
    this.word1 = word1;
    this.word2 = word2;
    this.retString = retString;
  };

  @Parameters
  public static Collection<String[]> getParams() {
    return Arrays.asList(new String[][] {
        { "father", "some", "No \"father\" or \"some\"in the graph!" },
        { "some", "hi", "No \"some\" or \"hi\"in the graph!" },
        { "father", "hi", "No \"father\" or \"hi\"in the graph!" },
        { "some", "kinds", "No bridge words from \"some\" to \"kinds\"!" },
        { "some", "of", "The bridge words from \"some\" to \"of\" are: kinds." },
        { "very", "but", "The bridge words from \"very\" to \"but\" are: strong, and fat." },
        { "", "", "No \"\" or \"\"in the graph!" }, { "aa", "", "No \"aa\" or \"\"in the graph!" },
        { "", "aa", "No \"\" or \"aa\"in the graph!" },
        { "123", "#$%", "No \"123\" or \"#$%\"in the graph!" } });
  }

  @Test
  public void test() {
    assertEquals(retString, MainClass.queryBridgeWords(g, word1, word2));
  }

}
