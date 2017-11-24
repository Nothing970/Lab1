package com.pair;

public class QueryBridgeWordCtrl {
  public static String query(String word1, String word2) {
    // 保证单词纯英文
    word1.matches("[a-zA-Z]+");
    if (word1.equals("") || word2.equals("")) {
      return "请输入单词!";
    } else if (word1.matches("[a-zA-Z]+") && word2.matches("[a-zA-Z]+")) {
      String tmpString = DirectedGraph.queryBridgeWords(DirectedGraph.getG(), word1, word2);
      return tmpString;
    } else {
      return "仅输入英文!";
    }
  }
}
