package org.shibaty.japanesecharsetconvert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


public class Main {

  /**
   * UTF-8を示すCharset名.<br>
   */
  private static final String CHARSET_NAME_UTF8 = "UTF-8";

  /**
   * Shift-JISを示すCharset名.<br>
   */
  private static final String CHARSET_NAME_SJIS = "Shift_JIS";

  /**
   * JISを示すCharset名.<br>
   */
  private static final String CHARSET_NAME_JIS = "ISO-2022-JP";

  /**
   * 改行.<br>
   */
  private static final String NEW_LINE = "\r\n";

  private static final String SUFFIX_TO_UTF8 = "_utf8.txt";
  private static final String SUFFIX_TO_SJIS = "_sjis.txt";
  private static final String SUFFIX_TO_JIS = "_jis.txt";
  private static final String SUFFIX_TO_JIS2 = "_jis2.txt";

  public static void main(String[] args) {
    // 文字コードのエイリアスを変更
    // Shift_JIS -> windows-31j
    // ISO-2022-JP -> x-windows-iso2022jp
    // 設定されないときは、Javaの起動オプション-Dにて設定する
    // (Windows Oracle Java8だと-Dでないと設定できなかった)
    System.setProperty("sun.nio.cs.map", "windows-31j/Shift_JIS,x-windows-iso2022jp/ISO-2022-JP");

    System.out.println(System.getProperty("sun.nio.cs.map"));

    if (args.length < 1) {
      System.out.println("parameter error.");
      System.exit(-1);
    }

    // サポートしているCharsetの一覧を出力
    Map<String, Charset> charsetMap = Charset.availableCharsets();
    for (Map.Entry<String, Charset> entry: charsetMap.entrySet()) {
      System.out.println(entry.getKey());
    }

    // 入力パラメータからPathを生成
    Path pathSjis = Paths.get("./", args[0]);
    Path pathUtf8 = pathSjis.resolveSibling(pathSjis.getFileName() + SUFFIX_TO_UTF8);
    Path pathReSjis = pathSjis.resolveSibling(pathSjis.getFileName() + SUFFIX_TO_SJIS);
    Path pathJis = pathSjis.resolveSibling(pathSjis.getFileName() + SUFFIX_TO_JIS);
    Path pathJis2 = pathSjis.resolveSibling(pathSjis.getFileName() + SUFFIX_TO_JIS2);

    // from SJIS to UTF-8
    convert(pathSjis, CHARSET_NAME_SJIS, pathUtf8, CHARSET_NAME_UTF8);

    // from UTF-8 to SJIS
    convert(pathUtf8, CHARSET_NAME_UTF8, pathReSjis, CHARSET_NAME_SJIS);

    // from UTF-8 to JIS
    convert(pathUtf8, CHARSET_NAME_UTF8, pathJis, CHARSET_NAME_JIS);

    // from SJIS to JIS
    convert(pathSjis, CHARSET_NAME_SJIS, pathJis2, CHARSET_NAME_JIS);
  }

  /**
   * 入力ファイルの文字コードを別の文字コードに変換して出力します.<br>
   * JISに変換する場合は①等の丸文字は置換できません.
   *
   * @param srcPath 入力ファイルパス
   * @param srcCharsetName 入力ファイルの文字コード名
   * @param destPath 出力ファイルパス
   * @param destCharsetName 出力ファイルの文字コード名
   */
  private static void convert(Path srcPath, String srcCharsetName, Path destPath, String destCharsetName) {

    try (
        BufferedReader br = Files.newBufferedReader(srcPath, Charset.forName(srcCharsetName));
      ) {
      try (
        BufferedWriter bw = Files.newBufferedWriter(destPath, Charset.forName(destCharsetName));
      ) {
        String line;
        while ((line = br.readLine()) != null) {
          bw.write(convertCharset(line, destCharsetName));
          bw.write(NEW_LINE); // bw.newLine()相当
        }
      }
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * 文字列を出力文字コードに変換します.<br>
   *
   * @param srcLine 入力文字列
   * @param destCharset 出力文字コード
   * @return 文字コード変換後の文字列
   * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない
   */
  private static String convertCharset(String srcLine, String destCharset) throws UnsupportedEncodingException {
    return new String(srcLine.getBytes(destCharset), destCharset);
  }

}

