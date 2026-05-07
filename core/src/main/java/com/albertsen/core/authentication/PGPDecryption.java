package com.albertsen.core.authentication;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;

import java.io.*;
import java.util.Iterator;

public class PGPDecryption {

    public static byte[] decrypt(byte[] encryptedData, PGPPrivateKey privateKey) throws Exception {

        InputStream in = PGPUtil.getDecoderStream(new ByteArrayInputStream(encryptedData));

        PGPObjectFactory factory =
                new PGPObjectFactory(in, new JcaKeyFingerprintCalculator());

        Object obj = factory.nextObject();

        PGPEncryptedDataList encList;

        if (obj instanceof PGPEncryptedDataList) {
            encList = (PGPEncryptedDataList) obj;
        } else {
            encList = (PGPEncryptedDataList) factory.nextObject();
        }

        @SuppressWarnings("unchecked")
        Iterator<PGPEncryptedData> it = encList.getEncryptedDataObjects();

        PGPPrivateKey sKey = privateKey;

        PGPPublicKeyEncryptedData encData = null;

        while (it.hasNext()) {
            PGPEncryptedData data = it.next();

            if (data instanceof PGPPublicKeyEncryptedData) {
                encData = (PGPPublicKeyEncryptedData) data;
                break;
            }
        }

        if (encData == null) {
            throw new IllegalArgumentException("No encrypted data found");
        }

        InputStream clear = encData.getDataStream(
                new JcePublicKeyDataDecryptorFactoryBuilder()
                        .setProvider("BC")
                        .build(sKey)
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int ch;
        while ((ch = clear.read()) >= 0) {
            out.write(ch);
        }

        return out.toByteArray();
    }
}