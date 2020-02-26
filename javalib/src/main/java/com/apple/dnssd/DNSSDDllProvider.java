package com.apple.dnssd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DNSSDDllProvider {
  private static Path dataDir = null;

  public static void setDataDir(Path dd) {
    dataDir = dd;
  }

  public static String getDLL(String library) throws IOException {
    if (dataDir == null) {
      throw new IllegalArgumentException("Please set dataDir via setDataDir() first so the library knows where to extract its .dll files to!");
    }
    String fileName = System.mapLibraryName(library);
    Path dstFile = dataDir.resolve("dnssd-native").resolve(fileName);
    try (InputStream is = DNSSD.class.getResourceAsStream("/" + fileName)) {
      Files.createDirectories(dstFile.getParent());
      Files.copy(is, dstFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (AccessDeniedException exc) {
      // if library is used in two different locations with same dataDir dll could already be in use
      return dstFile.toFile().getAbsolutePath();
    }

    return dstFile.toFile().getAbsolutePath();
  }

}
