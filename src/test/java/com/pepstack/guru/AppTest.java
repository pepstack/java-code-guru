/**
 * AppTest.java
 */
package com.pepstack.guru;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.pepstack.guru.AccountValidator;
import com.pepstack.guru.PhoneValidator;
import com.pepstack.guru.JsonResult;
import com.pepstack.guru.RC4;


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
}
