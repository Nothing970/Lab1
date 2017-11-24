package com.pair;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class MainGUI {
  private static String OriginPath = null;// txt 的目录

  public static void main(String[] args) {
    JFrame mainFrame = new JFrame("欢迎使用");

    JButton create = new JButton("生成有向图");
    JButton show = new JButton("展示图片");
    JButton query = new JButton("查询桥接词");
    JButton generate = new JButton("生成新文本");
    JButton calc = new JButton("最短路径");
    JButton random = new JButton("随即游走");

    show.setEnabled(false);
    query.setEnabled(false);
    generate.setEnabled(false);
    calc.setEnabled(false);
    random.setEnabled(false);

    mainFrame.setLayout(null);
    create.setBounds(30, 60, 120, 40);
    create.setFont(new java.awt.Font("楷书", 1, 15));
    show.setBounds(180, 60, 120, 40);
    show.setFont(new java.awt.Font("楷书", 1, 15));
    query.setBounds(330, 60, 120, 40);
    query.setFont(new java.awt.Font("楷书", 1, 15));
    generate.setBounds(30, 160, 120, 40);
    generate.setFont(new java.awt.Font("楷书", 1, 15));
    calc.setBounds(180, 160, 120, 40);
    calc.setFont(new java.awt.Font("楷书", 1, 15));
    random.setBounds(330, 160, 120, 40);
    random.setFont(new java.awt.Font("楷书", 1, 15));

    create.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        // System.out.println(OpenFile());
        String tmpString = OpenFile();
        if (tmpString != null) {
          OriginPath = tmpString;
          CreateDirectedGraphCtrl.ctrl(OriginPath);
          show.setEnabled(true);
          query.setEnabled(true);
          generate.setEnabled(true);
          calc.setEnabled(true);
          random.setEnabled(true);
        }
      }
    });
    show.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        String runPath = null;
        File f = new File("");
        try {
          runPath = f.getCanonicalPath();
        } catch (IOException e) {
          // TODO 自动生成的 catch 块
          e.printStackTrace();
        }
        show.setEnabled(false);
        JFrame WindowPic = WindowShowPicture(runPath + "\\graph.jpg");
        WindowPic.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            show.setEnabled(true);
          }
        });
      }
    });
    query.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        query.setEnabled(false);
        JFrame windowQuery = WindowShowQueryBridge();
        windowQuery.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            query.setEnabled(true);
          }
        });
      }
    });
    generate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        generate.setEnabled(false);
        JFrame windowGenerate = WindowShowNewText();
        windowGenerate.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            generate.setEnabled(true);
          }
        });
      }
    });
    calc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        calc.setEnabled(false);
        JFrame windowCalc = WindowShowShortPath();
        windowCalc.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            calc.setEnabled(true);
          }
        });
      }
    });
    random.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        random.setEnabled(false);
        JFrame windowRandom = WindowShowRandomWalk();
        windowRandom.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            random.setEnabled(true);
          }
        });
      }
    });

    mainFrame.add(create);
    mainFrame.add(show);
    mainFrame.add(query);
    mainFrame.add(generate);
    mainFrame.add(calc);
    mainFrame.add(random);

    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setBounds(100, 100, 480, 290);
    mainFrame.setVisible(true);
  }

  public static String OpenFile() {
    // 选取文件弹窗
    JFileChooser file = new JFileChooser(".");
    FileFilter tmp = new FileFilter() {
      public String getDescription() {
        return "*.txt";
      }

      public boolean accept(File f) {
        String name = f.getName();
        return f.isDirectory() || name.toLowerCase().endsWith(".txt");
      }
    };

    file.addChoosableFileFilter(tmp);
    file.setFileFilter(tmp);
    // file.setAcceptAllFileFilterUsed(true);
    int result = file.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      String path = file.getSelectedFile().getAbsolutePath();
      return path;
    }
    return null;
  }

  public static JFrame WindowShowPicture(String picturePath) {
    JFrame secondFrame = new JFrame("展示图片");
    JLabel picture = new JLabel();
    JPanel panel = new JPanel();
    JScrollPane sp = new JScrollPane(panel);
    File jpgFile = new File(picturePath);
    Image jpgImage = null;
    try {
      jpgImage = ImageIO.read(jpgFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    panel.add(picture);
    sp.getVerticalScrollBar().setUnitIncrement(10);
    secondFrame.setContentPane(sp);
    ImageIcon myImageIcon = new ImageIcon(jpgImage);
    picture.setIcon(myImageIcon);
    BufferedImage source = null;
    try {
      source = ImageIO.read(new FileInputStream(jpgFile));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    secondFrame.setSize(source.getWidth() + 100, source.getHeight() + 100);
    secondFrame.setLocation(800, 400);
    secondFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    secondFrame.setVisible(true);
    return secondFrame;
  }

  public static JFrame WindowShowShortPath() {
    JFrame thirdFrame = new JFrame("最短路径");
    JTextField textWord1 = new JTextField("", 30);
    JTextField textWord2 = new JTextField("", 30);
    JLabel labelWord1 = new JLabel("输入单词1");
    JLabel labelWord2 = new JLabel("输入单词2");
    JLabel labelOut = new JLabel("最短路径");
    JTextArea textOut = new JTextArea("", 10, 30);
    JButton showPath = new JButton("生成路径");
    JButton showPicture = new JButton("显示图片");
    JButton stop = new JButton("停止");
    JScrollPane spOut = new JScrollPane(textOut);

    thirdFrame.setLayout(null);
    labelWord1.setBounds(25, 10, 200, 40);
    labelWord2.setBounds(250, 10, 200, 40);
    textWord1.setBounds(25, 50, 200, 40);
    textWord2.setBounds(250, 50, 200, 40);
    showPath.setBounds(75, 110, 100, 30);
    stop.setBounds(185, 110, 100, 30);
    showPicture.setBounds(300, 110, 100, 30);
    labelOut.setBounds(25, 140, 200, 40);
    spOut.setBounds(25, 180, 425, 200);

    stop.setVisible(false);
    stop.setEnabled(false);
    textWord1.setFont(new Font("楷体", 1, 20));
    textWord2.setFont(new Font("楷体", 1, 20));
    textOut.setFont(new Font("楷体", 3, 20));
    textOut.setLineWrap(true);
    showPicture.setEnabled(false);

    QueryShortestPathCtrl.setupPaths(OriginPath);

    showPath.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String word1 = textWord1.getText().toLowerCase();
        String word2 = textWord2.getText().toLowerCase();
        // 第一个单词为空
        if (word1.equals("")) {
          textOut.setText("请输入单词!");
        }
        // 输出 一个点到其它所有的点的最短路径
        else if (word2.equals("") || showPath.getText().equals("下一个单词")) {// 处理自动填充后的情况
          word2 = QueryShortestPathCtrl.getWord2();

          showPath.setText("下一个单词");
          stop.setVisible(true);
          stop.setEnabled(true);
          textWord1.setEditable(false);
          textWord2.setEditable(false);
          textWord2.setText(word2);

          String shortPath = QueryShortestPathCtrl.queryOnePath(word1, word2);

          textOut.setText(shortPath);
          // 路径存在，生成图片
          if (shortPath.indexOf("->") >= 0) {
            showPicture.setEnabled(true);
          } else {
            showPicture.setEnabled(false);
          }
        } else {// 计算指定2点之间最短路径
          String shortPath = QueryShortestPathCtrl.queryOnePath(word1, word2);
          textOut.setText(shortPath);
          if (shortPath.indexOf("->") >= 0) {
            showPicture.setEnabled(true);
          } else {
            showPicture.setEnabled(false);
          }
        }
        // 完成输出一个点到其它所有的点的最短路径
        if (QueryShortestPathCtrl.isNoPath()) {
          showPath.setText("生成路径");
          stop.setVisible(false);
          stop.setEnabled(false);
          textWord1.setEditable(true);
          textWord2.setEditable(true);
          QueryShortestPathCtrl.setupPaths(OriginPath);
        }
      }
    });
    showPicture.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        QueryShortestPathCtrl.CreatePicture("ShortPath");
        String runPath = null;
        File f = new File("");
        try {
          runPath = f.getCanonicalPath();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        WindowShowPicture(runPath + "\\ShortPath.jpg");
      }
    });
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showPath.setText("生成路径");
        stop.setVisible(false);
        stop.setEnabled(false);
        textWord1.setEditable(true);
        textWord2.setEditable(true);
        QueryShortestPathCtrl.setupPaths(OriginPath);
      }
    });

    thirdFrame.setSize(500, 430);
    thirdFrame.setLayout(null);
    thirdFrame.add(labelWord1);
    thirdFrame.add(labelWord2);
    thirdFrame.add(textWord1);
    thirdFrame.add(textWord2);
    thirdFrame.add(showPath);
    thirdFrame.add(stop);
    thirdFrame.add(showPicture);
    thirdFrame.add(labelOut);
    thirdFrame.add(spOut);
    thirdFrame.dispose();
    thirdFrame.setVisible(true);
    return thirdFrame;
  }

  public static JFrame WindowShowNewText() {
    JFrame forthFrame = new JFrame("生成新文本");
    JLabel labelIn = new JLabel("输入新文本");
    JLabel labelOut = new JLabel("生成新文本");
    JTextArea textIn = new JTextArea("", 10, 30);
    JTextArea textOut = new JTextArea("", 10, 30);
    JButton createNew = new JButton("开始生成");
    JScrollPane spIn = new JScrollPane(textIn);
    JScrollPane spOut = new JScrollPane(textOut);

    forthFrame.setLayout(null);
    labelIn.setBounds(25, 10, 200, 40);
    spIn.setBounds(25, 50, 425, 150);
    createNew.setBounds(170, 225, 100, 30);
    labelOut.setBounds(25, 255, 200, 40);
    spOut.setBounds(25, 295, 425, 150);

    textIn.setFont(new Font("楷体", 1, 20));
    textOut.setFont(new Font("楷体", 1, 20));
    textIn.setLineWrap(true);
    textOut.setLineWrap(true);
    createNew.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String originText = textIn.getText().toLowerCase();
        textOut.setText(CreateTextCtrl.ctrl(originText));
      }
    });

    forthFrame.setSize(500, 550);
    forthFrame.add(labelIn);
    forthFrame.add(spIn);
    forthFrame.add(createNew);
    forthFrame.add(labelOut);
    forthFrame.add(spOut);
    forthFrame.dispose();
    forthFrame.setVisible(true);
    return forthFrame;

  }

  public static JFrame WindowShowQueryBridge() {
    JFrame fifthFrame = new JFrame("查询桥接词");
    JTextField textWord1 = new JTextField("", 30);
    JTextField textWord2 = new JTextField("", 30);
    JLabel labelWord1 = new JLabel("输入单词1");
    JLabel labelWord2 = new JLabel("输入单词2");
    JLabel labelOut = new JLabel("桥接词查询结果");
    JTextArea textOut = new JTextArea("", 10, 30);
    JButton startQuery = new JButton("开始查询");
    JScrollPane spOut = new JScrollPane(textOut);

    fifthFrame.setLayout(null);
    labelWord1.setBounds(25, 10, 200, 40);
    labelWord2.setBounds(250, 10, 200, 40);
    textWord1.setBounds(25, 50, 200, 40);
    textWord2.setBounds(250, 50, 200, 40);
    startQuery.setBounds(175, 110, 100, 30);
    labelOut.setBounds(25, 140, 200, 40);
    spOut.setBounds(25, 180, 425, 200);

    textWord1.setFont(new Font("楷体", 1, 20));
    textWord2.setFont(new Font("楷体", 1, 20));
    textOut.setFont(new Font("楷体", 3, 20));
    textOut.setLineWrap(true);
    startQuery.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String word1 = textWord1.getText().toLowerCase();
        String word2 = textWord2.getText().toLowerCase();
        // 保证单词纯英文
        textOut.setText(QueryBridgeWordCtrl.query(word1, word2));
      }
    });

    fifthFrame.setSize(500, 430);
    fifthFrame.setLayout(null);
    fifthFrame.add(labelWord1);
    fifthFrame.add(labelWord2);
    fifthFrame.add(textWord1);
    fifthFrame.add(textWord2);
    fifthFrame.add(startQuery);
    fifthFrame.add(labelOut);
    fifthFrame.add(spOut);
    fifthFrame.dispose();
    fifthFrame.setVisible(true);
    return fifthFrame;

  }

  public static JFrame WindowShowRandomWalk() {
    int width = 800;
    int height = 900;
    JFrame RandomFrame = new JFrame("随机游走");
    JLabel picture = new JLabel();
    JPanel panel = new JPanel();
    JScrollPane sp = new JScrollPane(panel);
    JButton nextWalk = new JButton("开始");
    JButton stopWalk = new JButton("停止");
    sp.getVerticalScrollBar().setUnitIncrement(10);
    RandomFrame.setLayout(null);
    nextWalk.setBounds(width / 4, height / 20, 100, 30);
    stopWalk.setBounds(width / 2, height / 20, 100, 30);
    sp.setBounds(0, height / 10, width - 20, height / 5 * 4 + 30);
    stopWalk.setEnabled(false);

    nextWalk.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String targetPath = OriginPath.substring(0, OriginPath.lastIndexOf("\\") + 1);// 得到存放脚本和图片的路径
        int walkState = RandomWalkCtrl.walkOneStep(OriginPath);
        if (walkState == 1) {// 成功随机游走一步
          nextWalk.setText("下一步");
          stopWalk.setEnabled(true);

          File jpgFile = new File(targetPath + "RandomWalk.jpg");
          Image jpgImage = null;
          try {
            jpgImage = ImageIO.read(jpgFile);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
          panel.add(picture);
          picture.setIcon(new ImageIcon(jpgImage));

        } else {// 发现无路可走,展示图片设置为原始图片
          File jpgFile = new File(targetPath + "graph.jpg");
          Image jpgImage = null;
          try {
            jpgImage = ImageIO.read(jpgFile);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
          panel.add(picture);
          picture.setIcon(new ImageIcon(jpgImage));
          nextWalk.setText("开始");
          stopWalk.setEnabled(false);
        }
      }
    });
    stopWalk.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RandomWalkCtrl.StopWark();
        nextWalk.setText("开始");
        stopWalk.setEnabled(false);
      }
    });

    RandomFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        RandomWalkCtrl.StopWark();
        nextWalk.setText("开始");
        stopWalk.setEnabled(false);
      }
    });

    RandomFrame.setSize(width, height);
    RandomFrame.setLayout(null);
    RandomFrame.add(nextWalk);
    RandomFrame.add(stopWalk);
    RandomFrame.add(sp);
    RandomFrame.dispose();
    RandomFrame.setVisible(true);
    RandomFrame.setResizable(false);
    return RandomFrame;
  }

}