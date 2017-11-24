package com.pair;

public class CreateTextCtrl {
  public static String ctrl(String originText) {
    if (originText.equals("")) {
      return "请输入文本!";
    } else {
      return DirectedGraph.generateNewText(DirectedGraph.getG(), originText);
    }
  }
}
