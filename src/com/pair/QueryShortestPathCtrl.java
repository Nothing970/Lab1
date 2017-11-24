package com.pair;

import java.util.Set;

public class QueryShortestPathCtrl {
  private static Short_path spath = null;// 存储最短路径的多条路径
  private static Set<String> multiShortPath = null;// 最短路径只输一个单词，存储未到达过的点

  public static void setupPaths(String OriginPath) {
    multiShortPath = DirectedGraph.hashtableMyclone(OriginPath).keySet();
  }

  public static boolean isNoPath() {
    return multiShortPath.isEmpty();
  }

  /*
   * 单源最短路径，每次选取一个目标路径，再去计算最短路径
   */
  public static String getWord2() {
    String word2 = "";
    for (String tmp_word : multiShortPath) {
      word2 = tmp_word;
      multiShortPath.remove(word2);
      break;
    }
    return word2;
  }

  public static String queryMultiPath(String word1, String word2) {
    // // 取出下一个单词
    // for (String tmp_word : multiShortPath) {
    // word2 = tmp_word;
    // multiShortPath.remove(word2);
    // break;
    // }
    // 计算最短路径
    String shortPath = DirectedGraph.calcShortestPath(DirectedGraph.getG(), word1, word2);
    spath = DirectedGraph.getSpath();
    // if (shortPath.indexOf("->") >= 0) {
    // CreatePicture("ShortPath");
    // }
    return shortPath;
  }

  public static String queryOnePath(String word1, String word2) {
    String shortPath = DirectedGraph.calcShortestPath(DirectedGraph.getG(), word1, word2);
    spath = DirectedGraph.getSpath();
    return shortPath;
  }

  public static void CreatePicture(String pictureName) {
    // 突出路径颜色
    GraphViz pr = new GraphViz();
    Short_path tmpPath = spath.clone();
    int i = 0;
    pr.clearTmpDotFile("ShortPath.dot");// 删除之前脚本文件
    while (tmpPath != null) {
      pr.setColorForPath(tmpPath.path, GraphViz.color[i % GraphViz.color.length], "ShortPath.dot");
      i++;
      tmpPath = tmpPath.next;
    }
    pr.runAfterSetColor(pictureName, "ShortPath.dot");
  }

}
