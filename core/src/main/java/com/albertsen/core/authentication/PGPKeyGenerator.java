package com.albertsen.core.authentication;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.util.Date;

public class PGPKeyGenerator {



    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        PGPKeyPair pgpKeyPair = new JcaPGPKeyPair(
                4,
                PGPPublicKey.RSA_GENERAL,
                kp,
                new Date()
        );

        byte[] originalFp = pgpKeyPair.getPublicKey().getFingerprint();
        StringBuilder sb = new StringBuilder();
        System.out.println("this one you say");
        for (byte b : originalFp) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println("Fingerprint:");
        System.out.println(sb.toString().trim());

        String sendData = dataSend(pgpKeyPair);

        PGPPublicKey receivedKey = dataRecive(sendData);

        byte[] receivedFp = receivedKey.getFingerprint();

        boolean matches = MessageDigest.isEqual(originalFp, receivedFp);

        System.out.println("Verified: " + matches);


    }


    public static String dataSend(PGPKeyPair pgpKeyPair) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArmoredOutputStream armor = new ArmoredOutputStream(out);

        pgpKeyPair.getPublicKey().encode(armor);
        armor.close();

        String publicKeyString = out.toString();

        System.out.println(publicKeyString);

        return publicKeyString;
    }

    public static PGPPublicKey dataRecive(String sendData) throws IOException, PGPException {
        InputStream in = PGPUtil.getDecoderStream(
                new ByteArrayInputStream(sendData.getBytes())
        );

        PGPPublicKeyRingCollection pgpPub =
                new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());

        PGPPublicKey receivedKey = null;

        for (PGPPublicKeyRing ring : pgpPub) {
            for (PGPPublicKey key : ring) {
                if (key.isEncryptionKey()) {
                    receivedKey = key;
                    break;
                }
            }
        }
        return receivedKey;
    }


}