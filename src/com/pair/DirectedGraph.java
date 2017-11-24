package com.pair;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;

public class DirectedGraph {
  private static Hashtable<String, Graph_vertex> g = null;// 图结构
  private static String StartWord = null;// 随机游走起始单词
  private static Short_path spath = null;// 存储最短路径的多条路径

  public static Hashtable<String, Graph_vertex> createDirectedGraph(String filename) {
    Hashtable<String, Graph_vertex> g = new Hashtable<String, Graph_vertex>();
    Graph_vertex pre_vertex = null;
    Graph_vertex cur_vertex = null;
    String cur_str = "";
    String pre_str = "";
    FileReader f = null;
    try {
      f = new FileReader(filename);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    int c = 0;
    int flag = 0;// 读取单词开始标记，读到字母置为1，操作单词之后置为0
    while (c != -1) {
      try {
        c = f.read();
      } catch (IOException e) {
        e.printStackTrace();
      }
      if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
        cur_str += String.valueOf((char) c);
        flag = 1;
      } else if (flag == 1) {
        cur_str = cur_str.toLowerCase();
        // 判断单词是否重复
        if (g.get(cur_str) == null) {
          cur_vertex = new Graph_vertex(cur_str);
          g.put(cur_str, cur_vertex);
        }
        cur_vertex = g.get(cur_str);
        // 第一个单词不做操作
        if (!pre_str.equals("")) {
          Node tmp_Node = pre_vertex.links;
          while (tmp_Node != null) {
            // 边已经存在，权重加1
            if (tmp_Node.link_vertex.word.equals(cur_str)) {
              tmp_Node.weight++;
              break;
            }
            tmp_Node = tmp_Node.next;
          }
          // 边不存在，添加边
          if (tmp_Node == null) {
            Node new_node = new Node(pre_vertex.links, cur_vertex);
            pre_vertex.links = new_node;
            pre_vertex.children++;
          }
        }
        pre_vertex = cur_vertex;
        pre_str = cur_str;
        cur_str = "";
        flag = 0;
      }

    }
    return g;
  }

  public static void showDirectedGraph(Hashtable<String, Graph_vertex> G) {
    String cur_word = null;
    Node tmp_node = null;
    GraphViz gViz = new GraphViz();
    gViz.start_graph();
    // 遍历每个单词，把每个的所有边写入脚本文件
    for (String word : G.keySet()) {
      tmp_node = G.get(word).links;
      while (tmp_node != null) {
        cur_word = tmp_node.link_vertex.word;
        gViz.addln("\"" + word + "\"->\"" + cur_word + "\"", tmp_node.weight);
        tmp_node = tmp_node.next;
      }
    }
    gViz.end_graph();
    try {
      gViz.run();// 由脚本文件生成图片
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String queryBridgeWords(Hashtable<String, Graph_vertex> G, String word1,
      String word2) {
    int bridgeNum = 0;// 标记桥接词数量
    StringBuffer ret = new StringBuffer(
        "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ");
    String cur_str = "";
    Graph_vertex mid_ver = null;
    Node tmp_node = null;
    // 判断word1、word2是否存在
    try {
      tmp_node = G.get(word1).links;
    } catch (Exception e) {
      return "No \"" + word1 + "\" or \"" + word2 + "\"in the graph!";
    }
    if (G.get(word2) == null) {
      return "No \"" + word1 + "\" or \"" + word2 + "\"in the graph!";
    }
    // word1->mid_ver->word2
    Node mid_node = null;// mid_ver出边指向的点
    while (tmp_node != null) {
      mid_ver = tmp_node.link_vertex;// 可能的桥接词
      mid_node = mid_ver.links;
      cur_str = mid_ver.word;
      // 判断mid_ver指出的点是否含有word2
      while (mid_node != null) {
        if (mid_node.link_vertex.word.equals(word2)) {
          bridgeNum++;
          ret.append(cur_str + ", ");
          break;
        }
        mid_node = mid_node.next;
      }
      tmp_node = tmp_node.next;
    }
    // 修改结果字符串
    ret.deleteCharAt(ret.length() - 1);
    int len = ret.length();
    if (bridgeNum == 0) {
      ret = new StringBuffer("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
    } else if (bridgeNum == 1) {
      ret.replace(len - 1, len, ".");
    } else {
      ret.replace(len - 1, len, ".");
      int lastpos = ret.lastIndexOf(", ");
      ret.insert(lastpos + 2, "and ");
    }
    return ret.toString();
  }

  public static String calcShortestPath(Hashtable<String, Graph_vertex> G, String word1,
      String word2) {
    // 采用深度优先遍历和分支限界法，先找到一条路径可行解，继续深度优先搜索，当前路径长度大于可行解，丢弃
    // 路径，等于可行解合并，小于更新可行解，最后的为最优解
    Stack<Node> node_sta = new Stack<Node>();// 存放下次要访问的结点
    Stack<Graph_vertex> ver_sta = new Stack<Graph_vertex>();// 路径上的点
    Stack<String> str_sta = new Stack<String>();// 路径上点的字符串
    Stack<Integer> int_sta = new Stack<Integer>();// 路径上边的长度
    Graph_vertex tmp_vertex = G.get(word2);
    spath = null;// 最短路径可行解
    // 判断word1、word2是否存在
    if (tmp_vertex == null) {
      return "\"" + word2 + "\" is not exist!";
    }
    tmp_vertex = G.get(word1);
    if (tmp_vertex == null) {
      return "\"" + word1 + "\" is not exist!";
    }
    Node tmp_node = tmp_vertex.links;
    String tmp_str = word1;
    int path_len = 0;
    int cur_shortest = 0x7f7f7f7f;
    // 判断word1是否有出边
    if (tmp_node == null) {
      return "Do not arrive!";
    }
    node_sta.push(tmp_node);
    ver_sta.push(tmp_vertex);
    str_sta.push(tmp_str);
    int_sta.push(0);
    // 深度优先搜索
    while (!ver_sta.isEmpty()) {
      tmp_node = node_sta.peek();
      // 当前不能继续向下搜索或者当前路径大于可行解路径长度
      if (tmp_node == null || path_len > cur_shortest) {
        path_len -= int_sta.peek();
        ver_sta.pop();
        str_sta.pop();
        node_sta.pop();
        int_sta.pop();
      }
      // 当前结点已经存入当前路径而且当前结点不是目标结点（自环）
      else if (str_sta.contains(tmp_node.link_vertex.word)
          && !tmp_node.link_vertex.word.equals(word2)) {
        node_sta.pop();
        node_sta.push(tmp_node.next);
      }
      // 进入下一层
      else {
        path_len += tmp_node.weight;
        tmp_vertex = tmp_node.link_vertex;
        tmp_str = tmp_vertex.word;
        node_sta.pop();
        node_sta.push(tmp_node.next);
        node_sta.push(tmp_vertex.links);
        ver_sta.push(tmp_vertex);
        str_sta.push(tmp_str);
        int_sta.push(tmp_node.weight);
        // 找到可行解
        if (tmp_str.equals(word2) && path_len <= cur_shortest) {
          if (path_len < cur_shortest) {
            cur_shortest = path_len;
            spath = null;
          }
          spath = (new Short_path((Stack<String>) str_sta.clone(), getSpath()));
          ver_sta.pop();
          str_sta.pop();
          node_sta.pop();
          int_sta.pop();
          path_len -= tmp_node.weight;
        }
      }
    }
    // 找到最优解
    if (cur_shortest < 0x7f7f7f7f) {
      String paths_String = "Shortest Lenth:" + String.valueOf(cur_shortest) + "\n";
      Short_path tmpSPath = getSpath().clone();
      int i = 1;
      // 拼接路径结果
      while (tmpSPath != null) {

        paths_String += "Path" + i + ": " + PrintPath(tmpSPath.path) + "\n";
        tmpSPath = tmpSPath.next;
        i++;
      }
      return paths_String;
    }
    // 无解
    else {
      return "Do not arrive!";
    }
  }

  public static String generateNewText(Hashtable<String, Graph_vertex> G, String inputText) {
    inputText += " ";// 保证最后一个单词有非字母字符
    byte[] chars = inputText.getBytes();
    String str1 = "";
    String str2 = "";
    String query_ret = "";
    StringBuffer new_ret = new StringBuffer();
    Hashtable<Integer, Integer> location = new Hashtable<>();// 记录每个桥接词在桥接词查询结果串中的位置
    int select_ctn = 0;
    int rand = 0;
    int flag = 0;// 为1字母读入str1，为2字母读入str2
    int len = inputText.length();
    int j = 0;
    for (int i = 0; i < len; i++) {
      if (chars[i] >= 65 && chars[i] <= 90 || chars[i] >= 97 && chars[i] <= 122) {
        if (flag <= 1) {
          str1 += (char) chars[i];
          flag = 1;
        } else {
          str2 += (char) chars[i];
        }
      }
      // 读到的第一个单词特殊处理
      else if (flag == 1) {
        new_ret.append(str1 + " ");
        flag = 2;
      }
      // str1已经读到，保证str2不为空
      else if ((!str2.equals(""))) {
        query_ret = queryBridgeWords(G, str1, str2);// 查桥接词结果
        // 找到桥接词
        if (query_ret.indexOf(':') > 0) {
          System.out.println(query_ret);
          j = query_ret.indexOf(", and ");
          if (j > 0) {// 多个桥接词
            // 返回串中最后一个桥接词特殊处理
            location.put(select_ctn++, j + 6);
            while (query_ret.lastIndexOf(",", j - 1) > 0) {
              j = query_ret.lastIndexOf(",", j - 1);
              location.put(select_ctn, j + 1);
            }
            // 返回串中第一个桥接词特殊处理
            location.put(select_ctn++, query_ret.lastIndexOf(":") + 2);
            // 随机找桥接词
            long tmplong = System.currentTimeMillis() % select_ctn;
            rand = (int) tmplong;
            try {
              Thread.sleep(1);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            rand = location.get(rand);
          } else {// 1个桥接词
            rand = query_ret.indexOf(":") + 2;
          }
          // rand save the index of a bridge word's begin_location
          if (query_ret.indexOf(",", rand) > 0) {
            j = query_ret.indexOf(",", rand);
          } else {
            j = query_ret.length() - 1;
          }
          // j save the index of a bridge word's end_location
          str1 = query_ret.substring(rand, j);
          new_ret.append(str1 + " ");
        }
        new_ret.append(str2 + " ");
        str1 = str2;
        str2 = "";
      }
    }
    new_ret.deleteCharAt(new_ret.length() - 1);
    return new_ret.toString();
  }

  public static String randomWalk(Hashtable<String, Graph_vertex> G) {
    // StartWord类的静态变量，函数外部赋值
    Node tmp_node = null;
    // 开始单词没有出边
    if (G.get(getStartWord()).children == 0) {
      return "No next";
    }
    // 随机找StartWord的一个出边
    long tmplong = System.currentTimeMillis() % G.get(getStartWord()).children;
    int rand = (int) tmplong;
    tmp_node = G.get(getStartWord()).links;
    while (true) {
      if (tmp_node.weight > 0) {
        rand--;
        if (rand == -1) {
          break;
        }
      }
      tmp_node = tmp_node.next;
    }
    // 对应单词的出边减少
    G.get(getStartWord()).children--;
    // 标记边已经走过
    tmp_node.weight = 0;
    return tmp_node.link_vertex.word;
  }

  ////////////////////////////////////////////////////////////////////

  public static String PrintPath(Stack<String> path_st) {
    // path_st存放路径，转为字符串存入toPrint
    StringBuffer toPrint = new StringBuffer();
    Stack<String> tmpStack = (Stack<String>) path_st.clone();
    String str1 = "";
    String str2 = tmpStack.peek();
    tmpStack.pop();
    while (!tmpStack.empty()) {
      str1 = tmpStack.peek();
      tmpStack.pop();
      toPrint.insert(0, str2);
      toPrint.insert(0, "->");
      str2 = str1;
    }
    toPrint.insert(0, str2);
    System.out.println(toPrint);
    return toPrint.toString();
  }

  public static Hashtable<String, Graph_vertex> hashtableMyclone(String filename) {
    Hashtable<String, Graph_vertex> ret = new Hashtable<String, Graph_vertex>();
    Graph_vertex pre_vertex = null;
    Graph_vertex cur_vertex = null;
    String cur_str = "";
    String pre_str = "";
    FileReader f = null;
    try {
      f = new FileReader(filename);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    int c = 0;
    int flag = 0;
    while (c != -1) {
      try {
        c = f.read();
      } catch (IOException e) {
        e.printStackTrace();
      }
      if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
        cur_str += String.valueOf((char) c);
        flag = 1;
      } else if (flag == 1) {
        // System.out.println(cur_str);
        cur_str = cur_str.toLowerCase();
        if (ret.get(cur_str) == null) {
          cur_vertex = new Graph_vertex(cur_str);
          ret.put(cur_str, cur_vertex);
        }
        cur_vertex = ret.get(cur_str);
        if (!pre_str.equals("")) {
          Node tmp_Node = pre_vertex.links;
          while (tmp_Node != null) {
            if (tmp_Node.link_vertex.word.equals(cur_str)) {
              tmp_Node.weight++;
              break;
            }
            tmp_Node = tmp_Node.next;
          }
          if (tmp_Node == null) {
            Node new_node = new Node(pre_vertex.links, cur_vertex);
            pre_vertex.links = new_node;
            pre_vertex.children++;
          }
        }
        pre_vertex = cur_vertex;
        pre_str = cur_str;
        cur_str = "";
        flag = 0;
      }

    }
    return ret;
  }

  public static Hashtable<String, Graph_vertex> getG() {
    return g;
  }

  public static void setG(Hashtable<String, Graph_vertex> g) {
    DirectedGraph.g = g;
  }

  public static String getStartWord() {
    return StartWord;
  }

  public static void setStartWord(String startWord) {
    StartWord = startWord;
  }

  public static Short_path getSpath() {
    return spath;
  }
}// D:\Java\project\Pair_txt\test.txt
