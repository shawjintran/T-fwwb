package com.t.medicaldocument.ES;


import java.io.*;
public class createDic {

        public static void main(String[] args) {
            try {
                // 从控制台读取输入字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("请输入字符串：");
                String result="";
                while (true) {
                    String inputString = reader.readLine();

                    if(inputString.equals("stop"))
                        break;
                    // 去除英文字符
                    String outputString = inputString.replaceAll("[a-zA-Z\\s]", "");

                    result+=outputString;
                    result+="\r\n";//换行

                }
                // 将结果保存到txt文件中
                File outputFile = new File("output.dic");
                FileWriter writer = new FileWriter(outputFile);
                writer.write(result);
                writer.close();

                System.out.println("结果已保存到output.txt文件中。");

            } catch (IOException e) {
                System.out.println("发生IO异常：" + e.getMessage());
            }
        }
    }
