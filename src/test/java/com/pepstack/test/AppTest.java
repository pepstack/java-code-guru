/**
 * AppTest.java
 */
package com.pepstack.test;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.pepstack.crypto.*;
import com.pepstack.guru.*;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.util.Date;

import java.security.KeyPair;

import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;


/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) {
        super( testName );
    }


    private void print(String text) {
        System.out.println(text);
    }


    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( AppTest.class );
    }


    public void testValidator() {
        assertFalse("Username not less than 3", AccountValidator.validateUsername("Li"));
        assertFalse("Username not less than 3", AccountValidator.validateUsername("A"));
        assertFalse("Username not great than 20", AccountValidator.validateUsername("Li1234567812345678901"));
        assertFalse("Username maximum is 20", AccountValidator.validateUsername("a234567890_234567890b"));

        assertTrue("Username not less than 3", AccountValidator.validateUsername("Tom"));
        assertTrue("Username maximum is 20", AccountValidator.validateUsername("a234567890_234567890"));

        assertTrue("Username can mixed with -_.", AccountValidator.validateUsername("Tom-Son"));
        assertTrue("Username can mixed with -_.", AccountValidator.validateUsername("Jhon-Big_Bug.A"));
        assertTrue("Username can mixed with -_.", AccountValidator.validateUsername("Jhon-Big-Bug.A"));

        assertFalse("Username not mixed with ..", AccountValidator.validateUsername("Jhon..BigBug"));
        assertFalse("Username not mixed with ..", AccountValidator.validateUsername("Jhon...BigBug"));
        assertFalse("Username not mixed with __", AccountValidator.validateUsername("Jhon__BigBug"));
        assertFalse("Username not mixed with __", AccountValidator.validateUsername("Jhon--BigBug"));
        assertFalse("Username not mixed with __", AccountValidator.validateUsername("Jhon---BigBug"));
        assertFalse("Username not mixed with __", AccountValidator.validateUsername("Jhon-_BigBug"));
        assertFalse("Username not mixed with __", AccountValidator.validateUsername("Jhon-.BigBug"));
        assertFalse("Username not mixed with __", AccountValidator.validateUsername("Jhon.-BigBug"));
        assertFalse("Username not mixed with __", AccountValidator.validateUsername("Jhon._BigBug"));
        assertFalse("Username not mixed with _.", AccountValidator.validateUsername("Jhon_.BigBug"));
        assertFalse("Username not mixed with _.", AccountValidator.validateUsername("Jhon._BigBug"));

        assertFalse("Username not start with -", AccountValidator.validateUsername("-Tom"));
        assertFalse("Username not start with _", AccountValidator.validateUsername("_Tom"));
        assertFalse("Username not start with .", AccountValidator.validateUsername(".Tom"));

        assertFalse("Username not start with number", AccountValidator.validateUsername("007Band"));
        assertFalse("Username not start with number", AccountValidator.validateUsername("007 Band"));

        assertTrue("Username start with character", AccountValidator.validateUsername("Band007"));
        assertTrue("Username start with character", AccountValidator.validateUsername("Band007x"));
        assertTrue("Username start with character", AccountValidator.validateUsername("Band-007"));

        assertFalse("Username can not have space", AccountValidator.validateUsername("Band 007"));
        assertFalse("Username can not have space", AccountValidator.validateUsername("Band 007 "));
        assertFalse("Username can not have space", AccountValidator.validateUsername("Band007 "));
        assertFalse("Username can not have space", AccountValidator.validateUsername(" Band007"));

        assertFalse("Username not end with -", AccountValidator.validateUsername("Tom-"));
        assertFalse("Username not end with _", AccountValidator.validateUsername("Tom_"));
        assertFalse("Username not end with .", AccountValidator.validateUsername("Tom."));

        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tomusa "));
        assertFalse("Username illegal characters", AccountValidator.validateUsername(" tomusa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom#usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom!usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom$usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom*usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom&usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom^usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom@usa"));
        assertFalse("Username illegal characters", AccountValidator.validateUsername("tom%usa"));

        assertFalse("Username cannot using unicode chars", AccountValidator.validateUsername("tom张"));
        assertFalse("Username cannot using unicode chars", AccountValidator.validateUsername("张3"));

        assertTrue("Username qualified", AccountValidator.validateUsername("zhang"));
        assertTrue("Username qualified", AccountValidator.validateUsername("zhang123"));
        assertTrue("Username qualified", AccountValidator.validateUsername("zhang-123"));
        assertTrue("Username qualified", AccountValidator.validateUsername("zhang_123"));
        assertTrue("Username qualified", AccountValidator.validateUsername("zhang_1.2.3"));

        assertEquals("ERROR_PASSWORD_IS_NULL", AccountValidator.validatePassword(null), AccountValidator.ERROR_PASSWORD_IS_NULL);
        assertEquals("ERROR_PASSWORD_TOO_SHORT", AccountValidator.validatePassword("A2b4#"), AccountValidator.ERROR_PASSWORD_TOO_SHORT);
        assertEquals("ERROR_PASSWORD_TOO_LONG", AccountValidator.validatePassword("A2b4#12345678900987654321abcde890"), AccountValidator.ERROR_PASSWORD_TOO_LONG);
        assertEquals("ERROR_PASSWORD_LACK_CHAR", AccountValidator.validatePassword("1234567890#"), AccountValidator.ERROR_PASSWORD_LACK_CHAR);
        assertEquals("ERROR_PASSWORD_LACK_NUMB", AccountValidator.validatePassword("AbcZdYTd#"), AccountValidator.ERROR_PASSWORD_LACK_NUMB);
        assertEquals("ERROR_PASSWORD_LACK_SPEC", AccountValidator.validatePassword("Abc123DefGG"), AccountValidator.ERROR_PASSWORD_LACK_SPEC);
        assertEquals("ERROR_PASSWORD_REPEAT_CHARS", AccountValidator.validatePassword("AAA123#des"), AccountValidator.ERROR_PASSWORD_REPEAT_CHARS);
        assertEquals("ERROR_PASSWORD_REPEAT_SETS", AccountValidator.validatePassword("Abc123Abc123#"), AccountValidator.ERROR_PASSWORD_REPEAT_SETS);
        assertEquals("ERROR_PASSWORD_HAS_WHITESPACE", AccountValidator.validatePassword("Abc 123Th#$"), AccountValidator.ERROR_PASSWORD_HAS_WHITESPACE);
        assertEquals("SUCCESS_PASSWORD_STRENGTH_OK", AccountValidator.validatePassword("Abc123Th#$"), AccountValidator.SUCCESS_PASSWORD_STRENGTH_OK);

        assertTrue(AccountValidator.validateEmail("350137278@qq.com"));
        assertTrue(AccountValidator.validateEmail("cheungmine@gmail.com"));
        assertTrue(AccountValidator.validateEmail("cheungmine@163.net"));

        assertFalse(AccountValidator.validateEmail("350137278qq.com"));
        assertFalse(AccountValidator.validateEmail("cheungmine@gmail"));
        assertFalse(AccountValidator.validateEmail("cheungmine@ 163.net"));

        assertTrue(PhoneValidator.isPhone("13800138000"));
        assertTrue(PhoneValidator.isPhone("13800138001"));

        assertFalse(PhoneValidator.isPhone("1380013800"));
        assertFalse(PhoneValidator.isPhone("138001380 0"));
        assertFalse(PhoneValidator.isPhone(" 1380013800"));

        assertFalse(PhoneValidator.isPhone("99899799789"));
    }


    public void testCrypto() {
		String inplain = "Shanghai";
		String inkey = "Pepstack.com";

		String cipher = RC4Util.toHexString(RC4.encrypt(inplain, inkey));

		String plain = RC4Util.decry_RC4(cipher, inkey);

		print(plain + " => " + cipher);
		
		assertEquals(plain, inplain);
	}
	
	
	public void testMD5() {
        print("================ testMD5 ================");

        String cipher = MD5.hexSign("abc");
        String cipher2 = RandomNumberGen.md5Message("abc");
        print(cipher + " = " + cipher2);
		assertEquals(cipher2, cipher);

        cipher = MD5.hexSign("abc", "UTF-8");
        cipher2 = RandomNumberGen.md5Message("abc");
        print(cipher + " = " + cipher2);
        assertEquals(cipher2, cipher);

        byte[] bycip = Encode.hexStringToBytes(cipher);
        String scip = Encode.toHexString(bycip);
        assertEquals(scip, cipher);

        cipher = MD5.hmacHexSign("abc", null);
        print(cipher);
        assertEquals("900150983cd24fb0d6963f7d28e17f72", cipher);

        cipher = MD5.hmacHexSign("abc", "18902130");
        print(cipher);
        assertEquals("572ed8a3166b3b0de71bd205cfa35c6a", cipher);

        cipher = MD5.hmacHexSign("abc!$df#%de", "abcdg1028");
        print(cipher);
        assertEquals("6635f2eef89f9d489554c61de9a7c834", cipher);
    }


	public void testRSA() throws Exception {
        print("==== test RSA javascript ====");

        RSA.KeyStore keyStore = RSA.getKeyStoreSingleton();

        RSA.SecretKeyPair secretCert = keyStore.getSecretKeyPair();

        String modulus = secretCert.getHexModulus();
        String publicExponent = secretCert.getHexPublicExponent();
        String privateExponent = secretCert.getHexPrivateExponent();

        print(">>>> modulus=" + modulus);
        print(">>>> publicExponent=" + publicExponent);
        print(">>>> privateExponent=" + privateExponent);

        String js_modulus = "009b4a8fcb9ccb748d13b4e05fbda511c49ba39bc3703259b793b5e1f046e7ce60e37faca9954722d961168220e2a1d77d0de35716f04eb5d667b3118ad223713b64fcb2e01ff880c958ea5908f57ce23b92625281dfc347d0516cf4f9154f31678bb70bf14659f453f7aa97dc27db215085067aed5837e91fd00078a3f70b787b";
        String js_public_exponent = "010001";
        String js_private_exponent = "243871edb5f6a68ab94bf9019bc442aeaef6ac401b8d42ebd4a219bee76aedf21f5e0a31cbc5b9d526160fe807b46404a116a39b90156bc8448bb9cdbd51d2f9006d68609959b8d61e46454bbcca249045c5858f1fc4a989663559bca89d39e3f56ee8f4ea26145f96324e1e47b49a80134cab161186d985dfda201088ae4d21";

        final java.security.KeyFactory factory = keyStore.getKeyFactory();

        RSAPrivateKey jsPrivateKey = RSA.genPrivateKey(factory, js_modulus, js_private_exponent);

        String cipher_js = "184648127daf491b30574434e10cbcfebd3695b797cce225610d8cd2becbdbdb3bac130f116932fe2d4239a4398ff0b610e1a27500d554daffc12febe9892e2c250e2b847cedaecfb3dfe8de9abede94325ccaf8d25531811f0c32422ba948d095beb6dee548a257bafb7cf718e6f4f501c128d6f65af3208f8f378015712a4f";
        String plain = RSA.reverseString( RSA.decryptHex(keyStore.getProvider(), jsPrivateKey, cipher_js) );

        print("plain=" + plain);
        assertEquals(plain, "abc123!$%@");

        js_private_exponent = "3d93837d844e7e2e5ecbd6cdea0be44c2e6fd6537c8393de90becb30d6bdfd2c97ca9e6f2a94cfad3bbc44f378f82d3b0d8054a01c8b9c4815fcac64955a735b7378827b54f11aec9efcdd4ccff8355becdc85bb2d7c049e62e0ee08ad07b081c975f6654398f58208300412eb21e65ef01ba7155a3cc3686f3b7e17cd182b11";
        js_modulus = "00911d9b224a082dda230e9b0ec86c76d3c60a97f139345b27db5fbf560b6c3179fe34f3f75b251c40a7b439b2db1c169a1bff779cf53c6c9fb875ed8b871a3bc4a3feb36db560b589fb2f6b162e7f48895187a0da079819e9c15c6c2a81f748a103faa680d1f0417c8cd34d4d9c308ba58577c1f7968fae89a9be05b7d4b195c9";
        cipher_js = "572ca1dcda60ba2e9d0467017e1be112e0eb5de8fd5ab8597ea3ba1ded1a91e32e3f4c2568f32794e87b6fc679762398fc15f35deff8342cb310da10dd351ba2b776b11518b7a6e65fd23120a4f6a0807204d0a3b0196070412c1307e27659b573344c208f6e63c4f3a6cfff4c43e0525b53757c0ce3aa1b092977603d1fecec";


        RSAPublicKey jsPublicKey = RSA.genPublicKey(factory, js_modulus, js_public_exponent);
        jsPrivateKey = RSA.genPrivateKey(factory, js_modulus, js_private_exponent);
        plain = RSA.reverseString( RSA.decryptHex(keyStore.getProvider(), jsPrivateKey, cipher_js) );
        print("plain=" + plain);
        assertEquals(plain, "12345678");

        String data = js_modulus + js_private_exponent + js_public_exponent;

        // hash is 256 chars length
        String hash = RSA.signHex(keyStore.getProvider(), jsPrivateKey, data);
        print("signHex=" + hash);

        boolean ok = RSA.verifyHex(keyStore.getProvider(), jsPublicKey, data, hash);
        print("verifyHex=" + ok);
        assertTrue(ok);
    }


	public void testRSACert() throws Exception {
        print("================ testRSACert ================");

        RSA.KeyStore keyStore = RSA.getKeyStoreSingleton();
        RSA.SecretKeyPair secretCert = keyStore.getSecretKeyPair();

        Date from = new Date();
        Date to = new Date(from.getTime() + 365 * 86400 * 1000);

        java.security.cert.X509Certificate x509cert = secretCert.generateX509Certificate("CN=test", "CN=test", from, to);

        print(RSA.formatPemCertificate(x509cert));

        print("X509CertHexModulus=" + RSA.getX509CertHexModulus(x509cert));
        print("X509CertHexPublicExponent=" + RSA.getX509CertHexPublicExponent(x509cert));
        //print("X509CertHexPrivateExponent=" + RSA.getX509CertHexPrivateExponent(x509cert));
    }
}
