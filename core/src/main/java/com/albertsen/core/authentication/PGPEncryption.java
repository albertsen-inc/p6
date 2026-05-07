package com.albertsen.core.authentication;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.*;

import java.io.*;
import java.security.SecureRandom;

public class PGPEncryption {

    public static byte[] encrypt(byte[] data, PGPPublicKey publicKey) throws Exception {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ArmoredOutputStream armoredOut = new ArmoredOutputStream(byteOut);

        // 1. Setup encryption generator
        PGPEncryptedDataGenerator encGen =
                new PGPEncryptedDataGenerator(
                        new JcePGPDataEncryptorBuilder(PGPEncryptedData.AES_256)
                                .setWithIntegrityPacket(true)
                                .setSecureRandom(new SecureRandom())
                                .setProvider("BC")
                );

        encGen.addMethod(
                new JcePublicKeyKeyEncryptionMethodGenerator(publicKey)
                        .setProvider("BC")
        );

        OutputStream encOut = encGen.open(armoredOut, new byte[4096]);

        // 2. Write data
        encOut.write(data);
        encOut.close();
        armoredOut.close();

        return byteOut.toByteArray();
    }
}