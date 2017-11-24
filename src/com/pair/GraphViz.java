package com.pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class GraphViz {
  private String runPath = "";// 图片+脚本存放路径
  private String dotPath = "dot.exe";// dot.exe可执行路径
  private String runOrder = "";// 运行dot.exe的参数
  private String dotCodeFile = "graph.dot";// 脚本文件名
  private String resultGra = "graph";// 生成图片文件名
  private StringBuilder graph = new StringBuilder();// 脚本文件缓冲区
  public static final String[] color = { "red", "chartreuse", "blue", "gold", "darkgoldenrod",
      "fuchsia", "hotpink", "cadetblue", "green", "cyan", "yellow" };// 可选线条颜色

  Runtime runtime = Runtime.getRuntime();

  public void run() {
    // 脚本缓冲区内容写到脚本文件中
    writeGraphToFile(graph.toString(), runPath);
    // 生成dot.exe运行所需参数
    creatOrder();
    Process tmpProcess = null;
    try {
      tmpProcess = runtime.exec(runOrder);// 调用dot.exe生成图片
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      tmpProcess.waitFor();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void creatOrder() {
    // 生成运行时参数
    runOrder += dotPath + " ";
    runOrder += runPath;
    runOrder += "\\" + dotCodeFile + " ";
    runOrder += "-T jpg ";
    runOrder += "-o ";
    runOrder += runPath;
    runOrder += "\\" + resultGra + ".jpg";
    // System.out.println(runOrder);
  }

  public void writeGraphToFile(String dotcode, String filename) {
    // 脚本缓冲区内容写到脚本文件中
    try {
      File file = new File(filename + "\\" + dotCodeFile);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(dotcode.getBytes());
      fos.close();
    } catch (java.io.IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public GraphViz()
  {
    // 构造函数
    File f = new File("");
    try {
      this.runPath = f.getCanonicalPath();// 获取当前项目的根目录
    } catch (IOException e) {
      // TODO 自动生成的 catch 块
      e.printStackTrace();
    }
  }

  public void add(String line) {
    graph.append("\t" + line);
  }

  public void addln(String line) {
    graph.append("\t" + line + "\n");
  }

  public void addln(String line, int label) {
    graph.append("\t" + line + "[label=" + String.valueOf(label) + "]\n");
  }

  public void addln() {
    graph.append('\n');
  }

  public void start_graph() {
    graph.append("digraph G {\n");
  }

  public void setColorForPath(Stack<String> path_Stack, String color, String dotName) {

    // 将待标记的每条边加入Set中
    Set<String> path_Set = new HashSet<String>();
    Stack<String> tmpStack = (Stack<String>) path_Stack.clone();// 待标记的路径（点）
    String str1 = "";
    String str2 = tmpStack.peek();
    tmpStack.pop();
    while (!tmpStack.isEmpty()) {
      str1 = tmpStack.peek();
      tmpStack.pop();
      path_Set.add("\"" + str1 + "\"->\"" + str2 + "\"");
      str2 = str1;
    }

    // 如果目标脚本文件不存在，从原始脚本文件拷贝
    String targetFile = runPath + "\\" + dotName;
    File outpuFile = new File(targetFile);
    if (!outpuFile.exists()) {
      try {
        Files.copy(new File(runPath + "\\graph.dot").toPath(), outpuFile.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    int pos = 0;
    String sourceFileName = runPath + "\\" + dotName;
    String tmp_str = "";
    File sourceFile = new File(sourceFileName);
    FileReader souceFileReader = null;

    // prepare readStream
    // 打开目标脚本文件（dotName）
    try {
      souceFileReader = new FileReader(sourceFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    BufferedReader sourceBufferedReader = new BufferedReader(souceFileReader);

    // prepare writeStream
    // 新建临时脚本文件，保存修改结果
    outpuFile = new File(runPath + "\\tmp.dot");
    try {
      outpuFile.createNewFile();
    } catch (IOException e2) {
      // TODO 自动生成的 catch 块
      e2.printStackTrace();
    }
    FileOutputStream fout = null;
    OutputStreamWriter outWriter = null;
    try {
      fout = new FileOutputStream(outpuFile);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    try {
      outWriter = new OutputStreamWriter(fout, "UTF-8");
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }
    BufferedWriter bufWrite = new BufferedWriter(outWriter);

    // add Head
    try {
      bufWrite.write("digraph G {\n");
    } catch (IOException e1) {
      // TODO 自动生成的 catch 块
      e1.printStackTrace();
    }

    // 逐行读取源脚本文件，将在指定路径上的边进行标记
    try {
      while ((tmp_str = sourceBufferedReader.readLine()) != null) {
        if ((pos = tmp_str.indexOf("[")) < 0) {// 排除脚本文件的头尾+空行
          continue;
        }
        String edge = tmp_str.substring(tmp_str.indexOf("\t") + 1, pos);
        if (path_Set.contains(edge)) {// 当前的边在待标记的路径上
          StringBuffer tmpBuffer = new StringBuffer(tmp_str);
          pos = tmp_str.indexOf("]");
          if (tmp_str.indexOf("color") >= 0) {// 已经包含在某条路径中，置为虚线
            tmpBuffer.insert(pos, " style=tapered penwidth=4 arrowtail=normal");
          } else {
            tmpBuffer.insert(pos, " color=" + color);
          }
          tmp_str = tmpBuffer.toString();
        }
        tmp_str += "\n";
        // 修改后写到临时脚本文件中
        bufWrite.write(tmp_str);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // add tail && close stream
    try {
      bufWrite.write("}");
      bufWrite.close();
      fout.close();
      sourceBufferedReader.close();
      souceFileReader.close();
      sourceFile.delete();// 删除源脚本文件
    } catch (IOException e1) {
      // TODO 自动生成的 catch 块
      e1.printStackTrace();
    }
    outpuFile.renameTo(sourceFile);// 将临时脚本文件重命名尾源脚本文件，供下次使用
  }

  public void end_graph() {
    graph.append("}");
  }

  public void clearTmpDotFile(String dotName) {
    // 清除临时脚本文件
    String fileNameString = runPath + "\\" + dotName;
    File tmpFile = new File(fileNameString);
    if (tmpFile.exists()) {
      tmpFile.delete();
    }
  }

  public void runAfterSetColor(String newJpgName, String dotName) {
    // create new Jpg file
    resultGra = newJpgName;
    dotCodeFile = dotName;
    Process tmpProcess = null;
    creatOrder();
    try {
      tmpProcess = runtime.exec(runOrder);

    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      tmpProcess.waitFor();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
