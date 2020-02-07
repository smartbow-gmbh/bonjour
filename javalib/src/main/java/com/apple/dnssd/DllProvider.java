package com.apple.dnssd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DllProvider {
  private static Path dataDir = null;

  public static void setDataDir(Path dd) {
    dataDir = dd;
  }

  public static String getDLL(String library) throws IOException {
    if (dataDir == null) {
      throw new IllegalArgumentException("Please set dataDir via DllProvider.setDataDir() first!");
    }
    String fileName = System.mapLibraryName(library);
    Path dstFile = dataDir.resolve("dnssd-native").resolve(fileName);
    try (InputStream is = DNSSD.class.getResourceAsStream("/" + fileName)) {
      Files.createDirectories(dstFile.getParent());
      Files.copy(is, dstFile, StandardCopyOption.REPLACE_EXISTING);
    }

    return dstFile.toFile().getAbsolutePath();
  }

}
