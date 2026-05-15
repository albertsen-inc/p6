package com.albertsen.core.authentication;

import static com.albertsen.core.utilFunctions.State.ACCEPT;
import static com.albertsen.core.utilFunctions.State.Pending;

import com.albertsen.core.utilFunctions.ConnectionStateHandler;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Authenticate {
    private KeyPair KeyPair;

    public byte[] generateKey() throws Exception {

        KeyPairGenerator Kpg = KeyPairGenerator.getInstance("EC");

        // Recommended secure curve
        ECGenParameterSpec ECSpec = new ECGenParameterSpec("secp256r1");

        Kpg.initialize(ECSpec);

        KeyPair = Kpg.generateKeyPair();


        byte[] PublicKeyEncoded =
                KeyPair.getPublic().getEncoded();

        MessageDigest sha =
                MessageDigest.getInstance("SHA-256");

        byte[] fingerprint =
                sha.digest(PublicKeyEncoded);

        StringBuilder sb = new StringBuilder();

        for (byte b : fingerprint) {
            sb.append(String.format("%02X", b));
        }

        System.out.println(
                "Alice fingerprint: " + sb
        );

        return PublicKeyEncoded;
    }


    public PublicKey reciveKey(byte[] PublicKeyEncoded) throws Exception{

        KeyFactory aliceKeyFactory = KeyFactory.getInstance("EC");

        X509EncodedKeySpec KeySpec =
                new X509EncodedKeySpec(PublicKeyEncoded);

        PublicKey publicKey =
                aliceKeyFactory.generatePublic(KeySpec);

        return publicKey;
    }



    public boolean checkFingerprint(byte[] PublicKeyEncoded) throws Exception{
        MessageDigest sha =
                MessageDigest.getInstance("SHA-256");

        byte[] receivedFingerprint =
                sha.digest(PublicKeyEncoded);

        ConnectionStateHandler.setFingerprint(Arrays.toString(receivedFingerprint));

        synchronized (ConnectionStateHandler.popupLock) {
            while (ConnectionStateHandler.getPopupState() == Pending) {
                try {
                    ConnectionStateHandler.popupLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            if (ConnectionStateHandler.getPopupState() == ACCEPT) {
                System.out.println("User accepted");
                return true;
                // continue connection
            } else {
                System.out.println("User denied");
                return false;
                // cancel connection
            }
        }
    }


    public byte[] generateSharedSecret(PublicKey publicKey)throws Exception{
        KeyAgreement KeyAgree =
                KeyAgreement.getInstance("ECDH");

        KeyAgree.init(KeyPair.getPrivate());

        KeyAgree.doPhase(publicKey, true);

        byte[] SharedSecret =
                KeyAgree.generateSecret();

        return SharedSecret;
    }


    public SecretKey makeAESKey(byte[] Secret) throws Exception{
        MessageDigest sha256 =
                MessageDigest.getInstance("SHA-256");

        byte[] hashedSecret =
                sha256.digest(Secret);

        // AES-256
        byte[] aesKeyBytes =
                Arrays.copyOf(hashedSecret, 32);

        SecretKey aesKey =
                new SecretKeySpec(aesKeyBytes, "AES");


        System.out.println("AES key generated successfully!");
        System.out.println(
                "AES key length: " +
                        (aesKey.getEncoded().length * 8) +
                        " bits"
        );

        return aesKey;

    }

    public void fullExtangeExample() throws Exception{
        // =========================
        // ALICE GENERATES EC KEY PAIR
        // =========================
        KeyPairGenerator aliceKpg = KeyPairGenerator.getInstance("EC");

        // Recommended secure curve
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");

        aliceKpg.initialize(ecSpec);

        KeyPair aliceKeyPair = aliceKpg.generateKeyPair();


        byte[] alicePublicKeyEncoded =
                aliceKeyPair.getPublic().getEncoded();

        MessageDigest sha =
                MessageDigest.getInstance("SHA-256");

        byte[] fingerprintAlice =
                sha.digest(alicePublicKeyEncoded);

        StringBuilder sb = new StringBuilder();

        for (byte b : fingerprintAlice) {
            sb.append(String.format("%02X", b));
        }

        System.out.println(
                "Alice fingerprint: " + sb
        );



        // =========================
        // BOB RECEIVES ALICE PUBLIC KEY
        // =========================
        KeyFactory bobKeyFactory = KeyFactory.getInstance("EC");

        X509EncodedKeySpec aliceKeySpec =
                new X509EncodedKeySpec(alicePublicKeyEncoded);

        PublicKey alicePublicKey =
                bobKeyFactory.generatePublic(aliceKeySpec);

        byte[] receivedFingerprintFromAlice =
                sha.digest(alicePublicKeyEncoded);

        if (!Arrays.equals(fingerprintAlice, receivedFingerprintFromAlice)){
            //CANSEL ALL MAN IN THE MIDDLE
        }


        // =========================
        // BOB GENERATES EC KEY PAIR
        // =========================
        KeyPairGenerator bobKpg = KeyPairGenerator.getInstance("EC");

        bobKpg.initialize(ecSpec);

        KeyPair bobKeyPair = bobKpg.generateKeyPair();

        byte[] bobPublicKeyEncoded =
                bobKeyPair.getPublic().getEncoded();


        byte[] fingerprintBob =
                sha.digest(bobPublicKeyEncoded);

        StringBuilder sbs = new StringBuilder();

        for (byte b : fingerprintBob) {
            sbs.append(String.format("%02X", b));
        }

        System.out.println(
                "Bob fingerprint: " + sbs
        );


        // =========================
        // ALICE RECEIVES BOB PUBLIC KEY
        // =========================
        KeyFactory aliceKeyFactory = KeyFactory.getInstance("EC");

        X509EncodedKeySpec bobKeySpec =
                new X509EncodedKeySpec(bobPublicKeyEncoded);

        PublicKey bobPublicKey =
                aliceKeyFactory.generatePublic(bobKeySpec);

        byte[] receivedFingerprintFromBob =
                sha.digest(bobPublicKeyEncoded);

        if (!Arrays.equals(fingerprintBob, receivedFingerprintFromBob)){
            //CANSEL ALL MAN IN THE MIDDLE
        }


        // =========================
        // ALICE COMPUTES SHARED SECRET
        // =========================
        KeyAgreement aliceKeyAgree =
                KeyAgreement.getInstance("ECDH");

        aliceKeyAgree.init(aliceKeyPair.getPrivate());

        aliceKeyAgree.doPhase(bobPublicKey, true);

        byte[] aliceSharedSecret =
                aliceKeyAgree.generateSecret();


        // =========================
        // BOB COMPUTES SHARED SECRET
        // =========================
        KeyAgreement bobKeyAgree =
                KeyAgreement.getInstance("ECDH");

        bobKeyAgree.init(bobKeyPair.getPrivate());

        bobKeyAgree.doPhase(alicePublicKey, true);

        byte[] bobSharedSecret =
                bobKeyAgree.generateSecret();


        // =========================
        // VERIFY BOTH SIDES MATCH
        // =========================
        System.out.println(
                "Shared secrets equal: " +
                        Arrays.equals(
                                aliceSharedSecret,
                                bobSharedSecret
                        )
        );


        // =========================
        // DERIVE AES KEY
        // =========================
        MessageDigest sha256 =
                MessageDigest.getInstance("SHA-256");

        byte[] hashedSecret =
                sha256.digest(aliceSharedSecret);

        // AES-256
        byte[] aesKeyBytes =
                Arrays.copyOf(hashedSecret, 32);

        SecretKey aesKey =
                new SecretKeySpec(aesKeyBytes, "AES");


        System.out.println("AES key generated successfully!");
        System.out.println(
                "AES key length: " +
                        (aesKey.getEncoded().length * 8) +
                        " bits"
        );
    }



}