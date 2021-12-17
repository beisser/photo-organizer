package de.beisser.po.digest;

import de.beisser.po.exceptions.FileHashingException;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHashProviderImpl implements FileHashProvider{

    @Override
    public String getHash(File file) {
        byte[] fileInBytes = new byte[0];
        try {
            fileInBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new FileHashingException("unable to convert file to bytes", e);
        }

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
        final byte[] digest = digestSHA3.digest(fileInBytes);
        return Hex.toHexString(digest);
    }
}
