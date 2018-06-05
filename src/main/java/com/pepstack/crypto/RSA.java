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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Date;

import java.math.BigInteger;

import java.security.KeyPair;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidParameterException;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

// http://www.bouncycastle.org/wiki/display/JA1/BC+Version+2+APIs
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;


public final class RSA {
    /**
     * encryption algorithm
     */
    private static final String KEY_ALGORITHM = "RSA";


    /**
     * key size with 1024. not change it.
     */
    private static final int KEY_SIZE = 1024;


    /**
     * signature algorithm
     */
    private static final String SGN_ALGORITHM = "MD5withRSA";


    /**
     * RSA max plain text block size (bytes) for encrypt
     * 	 1024 / 8 - 11 = 117
     */
    private static final int ENCRYPT_BLOCK = 117;


    /**
     * RSA max cipher text block size (bytes) for decrypt
     */
    private static final int DECRYPT_BLOCK = 128;


    /**
     * RSA singleton instance
     */
    private static final KeyStore keyStore = KeyStore.getInstance();


    private RSA() {
        // DO NOT CALL ME
    }


    /**
     * encrypt data with public key
     */
    private static byte[] encrypt(Provider provider, PublicKey publicKey, byte[] data) throws Exception {
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
                e.printStackTrace();
                return null;
            } finally {
                if (output != null) {
                    output.close();
                }
            }
        }
    }


    /**
     * decrypt data with private key
     */
    private static byte[] decrypt(Provider provider, PrivateKey privateKey, byte[] data) throws Exception {
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
                if (output != null) {
                    output.close();
                }
            }
        }
    }


    /**
     * get rsa private key
     */
    public static RSAPrivateKey getRSAPrivateKey() {
        return keyStore.getPrivateKey();
    }


    /**
     * get rsa public key
     */
    public static RSAPublicKey getRSAPublicKey() {
        return keyStore.getPublicKey();
    }


    /**
     * get provider
     */
    public static Provider getProvider() {
        return keyStore.getProvider();
    }


    /**
     * get key factory
     */
    public static KeyFactory getKeyFactory() {
        return keyStore.getFactory();
    }


    /**
     * get modulus of public key (hex encoded)
     */
    public static String getModulus() {
        return keyStore.getHexModulus();
    }


    public static String getModulus(RSAPublicKey publicKey) {
        return new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
    }


    /**
     * get public exponent of public key (hex encoded)
     */
    public static String getPublicExponent() {
        return keyStore.getHexPublicExponent();
    }


    public static String getPublicExponent(RSAPublicKey publicKey) {
        return new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
    }


    /**
     * get private exponent of private key (hex encoded)
     */
    public static String getPrivateExponent() {
        return keyStore.getHexPrivateExponent();
    }


    /**
     * generate rsa private key
     */
    public static RSAPrivateKey genRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        return keyStore.genPrivateKey(hexModulus, hexPrivateExponent);
    }


    /**
     * generate rsa public key
     */
    public static RSAPublicKey genRSAPublidKey(String hexModulus, String hexPublicExponent) {
        return keyStore.genPublicKey(hexModulus, hexPublicExponent);
    }


    /**
     * encrypt string to hex string with public key
     */
    public static String encryptHex(String plainText) {
        try {
            return new String(Hex.encodeHex(encrypt(
                keyStore.getProvider(),
                keyStore.getPublicKey(),
                plainText.getBytes())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * decrypt hex string using default keystore
     */
    public static String decryptHex(String cipherText) {
        if (StringUtils.isBlank(cipherText)) {
            return null;
        }
        try {
            byte[] plain = decrypt(keyStore.getProvider(),
                keyStore.getPrivateKey(),
                Hex.decodeHex(cipherText.toCharArray()));
            return new String(plain);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * decrypt hex string encryptd by javascript
     */
    public static String decryptHexByJs(String cipherText) {
        String plainText = decryptHex(cipherText);
        if (plainText != null) {
            return StringUtils.reverse(plainText);
        }
        return null;
    }


    public static String decryptHex(Provider provider, PrivateKey privateKey, String cipherText) {
        if (StringUtils.isBlank(cipherText)) {
            return null;
        }
        try {
            byte[] plain = decrypt(provider, privateKey, Hex.decodeHex(cipherText.toCharArray()));
            return new String(plain);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * decrypt hex string using specified keystore
     */
    public static String decryptHexByJs(Provider provider, PrivateKey privateKey, String cipherText) {
        String plainText = decryptHex(provider, privateKey, cipherText);
        if (plainText != null) {
            return StringUtils.reverse(plainText);
        }
        return null;
    }


	public static void exportCertFile(SecretCert cert, String certFile) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			File keyStoreFile = new File(certFile);
			fos = FileUtils.openOutputStream(keyStoreFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(cert.getKeyPair());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(fos);
		}
	}


	public static SecretCert importCertFile(String certFile) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		SecretCert cert = null;

		try {
			File keyStoreFile = new File(certFile);

			fis = FileUtils.openInputStream(keyStoreFile);
			ois = new ObjectInputStream(fis);

			KeyPair pair = (KeyPair) ois.readObject();

			RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();

			String hexModulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
			String hexPublicExponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
			String hexPrivateExponent = new String(Hex.encodeHex(privateKey.getPrivateExponent().toByteArray()));
			
			cert = new SecretCert(hexModulus, hexPublicExponent, hexPrivateExponent);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(fis);
		}

		return cert;
	}


	public static class SecretCert {
		private String hexModulus;
        private String hexPublicExponent;
        private String hexPrivateExponent;


		public SecretCert(String hexModulus, String hexPublicExponent, String hexPrivateExponent) {
			this.hexModulus = hexModulus;
			this.hexPublicExponent = hexPublicExponent;
			this.hexPrivateExponent = hexPrivateExponent;
		}


		public String getHexModulus() {
			return hexModulus;
		}
		
		
        public String getHexPublicExponent() {
			return hexPublicExponent;
		}
		
		
        public String getHexPrivateExponent() {
			return hexPrivateExponent;
		}


		public KeyPair getKeyPair() {
			//RSAPublicKey publicKey = keyStore.genPublidKey(hexModulus, hexPublicExponent);
			//RSAPrivateKey privateKey = RSA.genPrivateKey(hexModulus, hexPrivateExponent);
			
			//return new KeyPair(publicKey, privateKey);
			return null;
		}
    }


    /**
     * inner class
     */
    private static class KeyStore {
        private Provider provider;
        private KeyFactory factory;
        private KeyPair pair;
        private RSAPublicKey publicKey;
        private RSAPrivateKey privateKey;

        private String hexModulus;
        private String hexPublicExponent;
        private String hexPrivateExponent;


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
                KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
                factory = KeyFactory.getInstance(KEY_ALGORITHM, provider);

                byte[] seeds = DateFormatUtils.format(new Date(), "yyyyMMdd").getBytes();
                kpg.initialize(KEY_SIZE, new SecureRandom(seeds));

                pair = kpg.generateKeyPair();

                publicKey = (RSAPublicKey) pair.getPublic();
                privateKey = (RSAPrivateKey) pair.getPrivate();

                hexModulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
                hexPublicExponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
                hexPrivateExponent = new String(Hex.encodeHex(privateKey.getPrivateExponent().toByteArray()));

            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }


        public static KeyStore getInstance() {
            return KeyStore.Singleton.INSTANCE.getSingleton();
        }


        public Provider getProvider() {
            return provider;
        }


        public KeyFactory getFactory() {
            return factory;
        }


        public KeyPair getPair() {
            return pair;
        }


        public RSAPublicKey getPublicKey() {
            return publicKey;
        }


        public RSAPrivateKey getPrivateKey() {
            return privateKey;
        }


        public String getHexModulus() {
            return hexModulus;
        }


        public String getHexPublicExponent() {
            return hexPublicExponent;
        }


        public String getHexPrivateExponent() {
            return hexPrivateExponent;
        }


		public SecretCert getSecretCert() {
			return new SecretCert(hexModulus, hexPublicExponent, hexPrivateExponent);
		}


        /**
         * generate public key
         */
        public RSAPublicKey genPublicKey(String hexModulus, String hexPublicExponent) {
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


        /**
         * generate private key
         */
        public RSAPrivateKey genPrivateKey(String hexModulus, String hexPrivateExponent) {
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


        /**
         * save key store to file
         */
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
                IOUtils.closeQuietly(oos);
                IOUtils.closeQuietly(fos);
            }
        }


        /**
         * load key pair from key store file
         */
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
                IOUtils.closeQuietly(ois);
                IOUtils.closeQuietly(fis);
            }
        }
    }
}
