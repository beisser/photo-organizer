package de.beisser.po.digest;

import java.io.File;

public interface FileHashProvider {
    public String getHash(File file);
}
