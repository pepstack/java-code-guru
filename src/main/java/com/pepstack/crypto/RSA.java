/***********************************************************************
* Copyright (c) 2018 pepstack, pepstack.com
*
* This software is provided 'as-is', without any express or implied
* warranty.  In no event will the authors be held liable for any damages
* arising from the use of this software.
* Permission is granted to anyone to use this software for any purpose,
* including commercial applications, and to alter it and redistribute it
* freely, subject to the following restrictions:
*
* 1. The origin of this software must not be misrepresented; you must not
*   claim that you wrote the original software. If you use this software
*   in a product, an acknowledgment in the product documentation would be
*   appreciated but is not required.
*
* 2. Altered source versions must be plainly marked as such, and must not be
*   misrepresented as being the original software.
*
* 3. This notice may not be removed or altered from any source distribution.
***********************************************************************/
/**
 * @file: RSA.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2014-12-29
 *
 * @update: 2018-06-06 01:42:37
 */
package com.pepstack.crypto;


import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Date;
import java.util.Locale;
import java.util.Base64;

import java.math.BigInteger;

import java.security.KeyPair;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;

import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateEncodingException;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

// http://www.bouncycastle.org/wiki/display/JA1/BC+Version+2+APIs
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;

import org.bouncycastle.operator.OperatorCreationException;

import org.apache.commons.lang.StringUtils;


public final class RSA {
    // key size with 1024. DO NOT change it.
    public static final int KEY_SIZE = 1024;

    // encryption algorithm
    public static final String KEY_ALGORITHM = "RSA";

    // signature algorithm: SHA1withRSA, MD5withRSA
    public static final String SGN_ALGORITHM = "SHA1withRSA";


    // RSA max plain text block size (bytes) for encrypt
    //	 1024 / 8 - 11 = 117
    private static final int ENCRYPT_BLOCK = 117;


    // RSA max cipher text block size (bytes) for decrypt
    private static final int DECRYPT_BLOCK = 128;


    // RSA singleton instance
    private static final KeyStore keyStore = KeyStore.getInstance();


    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");


    public static KeyStore getKeyStoreSingleton() {
        return keyStore;
    }


    private RSA() {
        // DO NOT CALL IT!
    }


    // 安全地关闭流
    public static void finallyCloseStreamNoThrow(Closeable is) {
        try {
            if ( is != null ) {
                is.close();
            }
        } catch(IOException e) {
        }
    }


    public static final KeyFactory getKeyFactory(Provider provider) {
        try {
            final KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM, provider);
            return factory;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }


    // 生成公钥
    public static RSAPublicKey genPublicKey(KeyFactory factory, String hexModulus, String hexPublicExponent) {
        try {
            byte[] modulus = Hex.decodeHex(hexModulus.toCharArray());
            byte[] publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));

            return (RSAPublicKey) factory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        } catch (DecoderException ex) {
            throw new RuntimeException(ex);
        }
    }


    // 生成私钥
    public static RSAPrivateKey genPrivateKey(KeyFactory factory, String hexModulus, String hexPrivateExponent) {
        try {
            byte[] modulus = Hex.decodeHex(hexModulus.toCharArray());
            byte[] privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());

            RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));

            return (RSAPrivateKey) factory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        } catch (DecoderException ex) {
            throw new RuntimeException(ex);
        }
    }


    // 私钥签名
    public static byte[] sign(Provider provider, PrivateKey privateKey, byte[] data) throws Exception {
        Signature signature = Signature.getInstance(SGN_ALGORITHM, provider);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signBytes = signature.sign();
        return signBytes;
    }

    
    // 公钥验证签名
    public static boolean verify(Provider provider, PublicKey publicKey, byte[] data, byte[] signBytes) throws Exception {
        boolean ok = false;
        Signature signature = Signature.getInstance(SGN_ALGORITHM, provider);
        signature.initVerify(publicKey);
        signature.update(data);
        ok = signature.verify(signBytes);
        return ok;
    }


    // 私钥签名字符串
    public static String signHex(Provider provider, PrivateKey privateKey, String dataStr) {
        try {
            byte[] hash = sign(provider, privateKey, dataStr.getBytes());
            String signHexStr = new String(Hex.encodeHex(hash));
            return signHexStr;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    // 公钥验证签名字符串
    public static boolean verifyHex(Provider provider, PublicKey publicKey, String dataStr, String signHexStr) {
        try {
            byte[] signBytes = Hex.decodeHex(signHexStr.toCharArray());

            boolean ok = verify(provider, publicKey, dataStr.getBytes(), signBytes);

            return ok;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    // 公钥加密
    public static byte[] encrypt(Provider provider, PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(KEY_ALGORITHM, provider);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);

        if (data.length <= ENCRYPT_BLOCK) {
            return ci.doFinal(data);
        } else {
            ByteArrayOutputStream output = null;
            try {
                output = new ByteArrayOutputStream();

                int offset = 0;
                int len = data.length - offset;

                while (len > 0) {
                    if (len > ENCRYPT_BLOCK) {
                        len = ENCRYPT_BLOCK;
                    }

                    byte[] block = ci.doFinal(data, offset, len);
                    output.write(block, 0, block.length);

                    offset += len;
                    len = data.length - offset;
                }

                return output.toByteArray();
            } catch (Exception e) {
                return null;
            } finally {
                finallyCloseStreamNoThrow(output);
            }
        }
    }


    // 私钥解密
    public static byte[] decrypt(Provider provider, PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(KEY_ALGORITHM, provider);
        ci.init(Cipher.DECRYPT_MODE, privateKey);

        if (data.length <= DECRYPT_BLOCK) {
            return ci.doFinal(data);
        } else {
            ByteArrayOutputStream output = null;
            try {
                output = new ByteArrayOutputStream();

                int offset = 0;
                int len = data.length - offset;

                while (len > 0) {
                    if (len > DECRYPT_BLOCK) {
                        len = DECRYPT_BLOCK;
                    }

                    byte[] block = ci.doFinal(data, offset, len);
                    output.write(block, 0, block.length);

                    offset += len;
                    len = data.length - offset;
                }

                return output.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                finallyCloseStreamNoThrow(output);
            }
        }
    }


    // 使用公钥加密字符串, 加密后得到 16 进制字符串
    //
    public static String encryptHex(Provider provider, RSAPublicKey publicKey, String plainText) {
        try {
            byte[] cipher = encrypt(provider, publicKey, plainText.getBytes());

            return  new String(Hex.encodeHex(cipher));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    
    // 使用私钥解密 16 进制加密字符串, 解密后得到原字符串
    //
    public static String decryptHex(Provider provider, RSAPrivateKey privateKey, String cipherText) {
        try {
            if (StringUtils.isBlank(cipherText)) {
                return null;
            }

            byte[] plain = decrypt(provider, privateKey, Hex.decodeHex(cipherText.toCharArray()));

            return new String(plain);
        } catch (Exception ex) {
        }

        return null;
    }


    // 使用证书公钥加密字符串, 加密后得到 16 进制字符串
    //
    public static String encryptHex(SecretKeyPair secretCert, String plainText) {
        return encryptHex(secretCert.getProvider(), secretCert.getPublicKey(), plainText);
    }


    // 使用证书私钥解密 16 进制加密字符串, 解密后得到原字符串
    //
    public static String decryptHex(SecretKeyPair secretCert, String cipherText) {
        return decryptHex(secretCert.getProvider(), secretCert.getPrivateKey(), cipherText);
    }


    // javascript RSA 加密后的字符串解密后需要反转
    public static String reverseString(String str) {
        if (str != null) {
            return StringUtils.reverse(str);
        }
        return null;
    }


    public static String getX509CertHexModulus(X509Certificate x509cert) {
        final RSAPublicKey pubKey = (RSAPublicKey) x509cert.getPublicKey();
        final String hexModulus = new String(Hex.encodeHex(pubKey.getModulus().toByteArray()));
        return hexModulus;
    }


    public static String getX509CertHexPublicExponent(X509Certificate x509cert) {
        final RSAPublicKey pubKey = (RSAPublicKey) x509cert.getPublicKey();
        final String hexPublicExponent = new String(Hex.encodeHex(pubKey.getPublicExponent().toByteArray()));
        return hexPublicExponent;
    }


    // X509 证书输出到字符串
    //   https://stackoverflow.com/questions/3313020/write-x509-certificate-into-pem-formatted-string-in-java
    public static String formatPemCertificate(final Certificate certificate) throws CertificateEncodingException {
        final Base64.Encoder encoder = Base64.getMimeEncoder(64, LINE_SEPARATOR.getBytes());
        final byte[] rawCrtText = certificate.getEncoded();
        final String encodedCertText = new String(encoder.encode(rawCrtText));
        final String pem_cert = BEGIN_CERT + LINE_SEPARATOR + encodedCertText + LINE_SEPARATOR + END_CERT;
        return pem_cert;
    }


    public static class SecretKeyPair {
        private final Provider provider;
        private final KeyPair keyPair;


        public SecretKeyPair(Provider bcProvider, KeyPairGenerator pairGenerator) {
            provider = bcProvider;
            keyPair = pairGenerator.generateKeyPair();
        }


        public SecretKeyPair(Provider bcProvider, String hexModulus, String hexPublicExponent, String hexPrivateExponent) {
            provider = bcProvider;
           
            final KeyFactory factory = RSA.getKeyFactory(provider);

            final RSAPublicKey publicKey = RSA.genPublicKey(factory, hexModulus, hexPublicExponent);
            final RSAPrivateKey privateKey = RSA.genPrivateKey(factory, hexModulus, hexPrivateExponent);

            keyPair = new KeyPair(publicKey, privateKey);
        }


        public Provider getProvider() {
            return provider;
        }


        public KeyPair getKeyPair() {
            return keyPair;
        }


        public RSAPublicKey getPublicKey() {
            return (RSAPublicKey) keyPair.getPublic();
        }


        public RSAPrivateKey getPrivateKey() {
            return (RSAPrivateKey) keyPair.getPrivate();
        }


        public String getHexModulus() {
            final String hexModulus = new String(Hex.encodeHex(getPublicKey().getModulus().toByteArray()));
            return hexModulus;
        }


        public String getHexPublicExponent() {
            final String hexPublicExponent = new String(Hex.encodeHex(getPublicKey().getPublicExponent().toByteArray()));
            return hexPublicExponent;
        }


        public String getHexPrivateExponent() {
            final String hexPrivateExponent = new String(Hex.encodeHex(getPrivateKey().getPrivateExponent().toByteArray()));
            return hexPrivateExponent;
        }
        

        // 生成 X509 证书 ( 证书只包含公钥, 对应的私钥要服务器自己保存. )
        // https://www.programcreek.com/java-api-examples/index.php?api=org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
        //
        public X509Certificate generateX509Certificate(String issuerName, String subjectName, Date startDate, Date endDate) throws CertificateException {
            try {
                java.security.Security.addProvider(new BouncyCastleProvider());

                X500Name issuer = new X500Name(issuerName);

                X500Name subject = new X500Name(subjectName);

                SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(getPublicKey().getEncoded());

                BigInteger serialNo = new BigInteger(64, new SecureRandom());

                X509v3CertificateBuilder v3certBuilder =
                    new X509v3CertificateBuilder(issuer, serialNo, startDate, endDate, subject, subPubKeyInfo);

                ContentSigner signer = new JcaContentSignerBuilder(RSA.SGN_ALGORITHM).setProvider(provider.getName()).build(getPrivateKey());

                X509CertificateHolder certHolder = v3certBuilder.build(signer);

                return new JcaX509CertificateConverter().setProvider(provider.getName()).getCertificate(certHolder);
            } catch (OperatorCreationException ex) {
                throw new CertificateException(ex);
            } catch (CertificateException ex) {
                throw new CertificateException(ex);
            }
        }

    }


    // 单例内部静态类, 只能访问外部类的静态方法
    //
    public static class KeyStore {
        private final Provider provider;

        private final KeyPairGenerator pairGenerator;

        private final SecretKeyPair secretCert;


        private static enum Singleton {
            INSTANCE;

            private static final KeyStore singleton = new KeyStore();

            public KeyStore getSingleton() {
                return singleton;
            }
        }


        private KeyStore() {
            try {
                provider = new BouncyCastleProvider();

                SecureRandom random = new SecureRandom();
                random.setSeed(System.currentTimeMillis());

                pairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
                pairGenerator.initialize(KEY_SIZE, random);

                secretCert = createSecretKeyPair();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }


        // 创建新的证书密钥对
        public SecretKeyPair createSecretKeyPair() {
            SecretKeyPair secret = new SecretKeyPair(provider, pairGenerator);
            return secret;
        }


        public static KeyStore getInstance() {
            return KeyStore.Singleton.INSTANCE.getSingleton();
        }


        public Provider getProvider() {
            return provider;
        }


        public final KeyFactory getKeyFactory() {
            try {
                final KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM, provider);
                return factory;
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }


        public KeyPairGenerator getKeyPairGenerator() {
            return pairGenerator;
        }


		public SecretKeyPair getSecretKeyPair() {
			return secretCert;
		}


        /*
        // save key store to file
        private void saveKeyStore(File keyStoreFile) {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = FileUtils.openOutputStream(keyStoreFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(pair);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } finally {
                finallyCloseStreamNoThrow(oos);
                finallyCloseStreamNoThrow(fos);
            }
        }


        // load key pair from key store file
        private void loadKeyStore(File keyStoreFile) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = FileUtils.openInputStream(keyStoreFile);
                ois = new ObjectInputStream(fis);

                pair = (KeyPair) ois.readObject();

                publicKey = (RSAPublicKey) pair.getPublic();
                privateKey = (RSAPrivateKey) pair.getPrivate();

                hexModulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
                hexPublicExponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
                hexPrivateExponent = new String(Hex.encodeHex(privateKey.getPrivateExponent().toByteArray()));
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } finally {
                finallyCloseStreamNoThrow(ois);
                finallyCloseStreamNoThrow(fis);
            }
        }
        */
    }
}
