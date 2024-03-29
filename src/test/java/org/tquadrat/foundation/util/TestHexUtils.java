/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.util;

import static java.lang.System.arraycopy;
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.util.HexUtils.convertFromHexString;

import java.util.HexFormat;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  This class provides the JUnit tests for the methods in class
 *  {@link HexUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "removal" )
@ClassVersion( sourceVersion = "$Id: TestHexUtils.java 1087 2024-01-06 09:51:27Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestHexUtils" )
public class TestHexUtils extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test if
     *  {@link HexUtils#convertFromHexString(CharSequence) convertFromHexString()}
     *  and the conversion through
     *  <pre><code>String s = HexFormat.of().withUpperCase().formatHex( bytes );</code></pre>
     *  are invers to each other.
     */
    @Test
    public final void testInversConversion()
    {
        skipThreadTest();

        final Function<byte[],String> convertToHexString = bytes -> HexFormat.of().withUpperCase().formatHex( bytes );

        byte [] inputBytes;
        byte [] actuals;
        byte [] expecteds;

        String inputString;
        String actual;
        String expected;

        //---* Only zeroes *---------------------------------------------------
        inputBytes = new byte [] {0, 0, 0, 0};
        expecteds = new byte [inputBytes.length];
        arraycopy( inputBytes, 0, expecteds, 0, inputBytes.length );
        actuals = convertFromHexString( convertToHexString.apply( inputBytes ) );
        assertArrayEquals( expecteds, actuals );

        inputString = "00000000";
        expected = inputString;
        actual = convertToHexString.apply( convertFromHexString( inputString ) );
        assertEquals( expected, actual );

        //---* Only FFs *------------------------------------------------------
        //noinspection NumericCastThatLosesPrecision
        inputBytes = new byte [] {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        expecteds = new byte [inputBytes.length];
        arraycopy( inputBytes, 0, expecteds, 0, inputBytes.length );
        actuals = convertFromHexString( convertToHexString.apply( inputBytes ) );
        assertArrayEquals( expecteds, actuals );

        inputString = "FFFFFFFF";
        expected = inputString;
        actual = convertToHexString.apply( convertFromHexString( inputString ) );
        assertEquals( expected, actual );

        inputString = "ffffffff";
        expected = inputString;
        actual = convertToHexString.apply( convertFromHexString( inputString ) );
        assertNotEquals( actual, expected );
        assertEquals( expected, actual.toLowerCase( ROOT ) );

        //---* Any values *----------------------------------------------------
        //noinspection NumericCastThatLosesPrecision
        inputBytes = new byte [] {(byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0 };
        expecteds = new byte [inputBytes.length];
        arraycopy( inputBytes, 0, expecteds, 0, inputBytes.length );
        actuals = convertFromHexString( convertToHexString.apply( inputBytes ) );
        assertArrayEquals( expecteds, actuals );

        //noinspection NumericCastThatLosesPrecision
        inputBytes = new byte [] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, (byte)128, (byte)129, (byte)130, (byte)255};
        expecteds = new byte [inputBytes.length];
        arraycopy( inputBytes, 0, expecteds, 0, inputBytes.length );
        actuals = convertFromHexString( convertToHexString.apply( inputBytes ) );
        assertArrayEquals( expecteds, actuals );

        inputString = "123456789ABCDEF0";
        expected = inputString;
        actual = convertToHexString.apply( convertFromHexString( inputString ) );
        assertEquals( expected, actual );

        inputString = "123456789abcdef0";
        expected = inputString;
        actual = convertToHexString.apply( convertFromHexString( inputString ) );
        assertNotEquals( actual, expected );
        assertEquals( expected, actual.toLowerCase( ROOT ) );
    }   //  testInversConversion()

    /**
     *  Validates whether the class is static.
     */
    @Test
    final void validateClass()
    {
        assertTrue( validateAsStaticClass( HexUtils.class ) );
    }   //  validateClass()
}
//  class TestHexUtils

/*
 *  End of File
 */