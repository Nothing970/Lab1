package com.pair;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

public class RandomWalkCtrl {
  private static boolean isWalking = false;// 随机游走停止控制
  public static String StartWord = null;// 随机游走起始单词
  private static Hashtable<String, Graph_vertex> path = null;// 随机游走所需的图结构，不能与原图公用

  public static int walkOneStep(String OriginPath) {
    if (isWalking == false) {// 初始化
      GraphViz gra = new GraphViz();
      gra.clearTmpDotFile("RandomWalk.dot");
      path = DirectedGraph.hashtableMyclone(OriginPath);
      Enumeration<String> words = DirectedGraph.getG().keys();
      // 随机生成起始单词
      long tmplong = System.currentTimeMillis() % path.size();
      int rand = (int) tmplong;
      for (; rand > -1; rand--) {
        StartWord = words.nextElement();
      }
      System.out.println("newPath:");
      System.out.println(StartWord);

    }
    isWalking = true;
    Stack<String> path_ver = new Stack<String>();// 存放路径经过的点
    path_ver.push(StartWord);
    DirectedGraph.setStartWord(StartWord);
    String word2 = DirectedGraph.randomWalk(path);

    // 有下一个单词并向下走一步
    if (!word2.equals("No next")) {
      System.out.println(word2);
      path_ver.push(word2);
      GraphViz gra = new GraphViz();
      gra.setColorForPath(path_ver, "red", "RandomWalk.dot");
      gra.runAfterSetColor("RandomWalk", "RandomWalk.dot");
      // 更新起始单词
      StartWord = word2;
      return 1;
    }
    // 没有路可走，显示原始图片
    else {
      isWalking = false;
      return 0;
    }
  }

  public static void StopWark() {
    isWalking = false;
  }
}
