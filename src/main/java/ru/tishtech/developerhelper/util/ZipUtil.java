package ru.tishtech.developerhelper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

  public static void zip(String fromFile, String toFile) {
    try {
      ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(toFile));
      File fileToZip = new File(fromFile);
      zipFile(fileToZip, fileToZip.getName(), zipOutputStream);
      zipOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOutputStream) {
    try {
      if (fileToZip.isDirectory()) {
        if (fileName.endsWith("/")) {
          zipOutputStream.putNextEntry(new ZipEntry(fileName));
          zipOutputStream.closeEntry();
        } else {
          zipOutputStream.putNextEntry(new ZipEntry(fileName + "/"));
          zipOutputStream.closeEntry();
        }
        File[] children = fileToZip.listFiles();
        for (File childFile : children) {
          zipFile(childFile, fileName + "/" + childFile.getName(), zipOutputStream);
        }
        return;
      }
      FileInputStream fileInputStream = new FileInputStream(fileToZip);
      ZipEntry zipEntry = new ZipEntry(fileName);
      zipOutputStream.putNextEntry(zipEntry);
      byte[] bytes = new byte[1024];
      int length;
      while ((length = fileInputStream.read(bytes)) >= 0) {
        zipOutputStream.write(bytes, 0, length);
      }
      fileInputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
