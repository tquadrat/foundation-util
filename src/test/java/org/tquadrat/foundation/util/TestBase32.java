/*
 * ============================================================================
 *  Copyright © 2002-2022 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.Base32.Decoder;
import static org.tquadrat.foundation.util.Base32.EMPTY_byte_ARRAY;
import static org.tquadrat.foundation.util.Base32.Encoder;
import static org.tquadrat.foundation.util.Base32.getDecoder;
import static org.tquadrat.foundation.util.Base32.getEncoder;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for
 *  {@link Base32}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestBase32.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestBase32" )
public class TestBase32 extends TestBaseClass
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some arbitrary tests.
     *
     *  @throws Exception   Something unexpected gone wrong.
     */
    @Test
    final void someTestsWithByteArrays() throws Exception
    {
        skipThreadTest();

        final var encoder = getEncoder();
        assertNotNull( encoder );
        final var decoder = getDecoder();
        assertNotNull( decoder );

        byte [] expected, input, result;

        input = EMPTY_byte_ARRAY;
        expected = EMPTY_byte_ARRAY;
        result = encoder.encode( input );
        assertNotNull( result );
        assertArrayEquals( expected, result );
        result = decoder.decode( input );
        assertNotNull( result );
        assertArrayEquals( expected, result );

        input = new byte [] {0};
        expected = new byte [] {48};
        result = encoder.encode( input );
        assertNotNull( result );
        assertArrayEquals( expected, result );

        input = new byte [] {48};
        expected = new byte [] {0};
        result = decoder.decode( input );
        assertNotNull( result );
        assertArrayEquals( expected, result );
    }   //  someTestsWithByteArrays()

    /**
     *  Some arbitrary tests.
     *
     *  @throws Exception   Something unexpected gone wrong.
     */
    @Test
    final void someTestsWithNumbers() throws Exception
    {
        skipThreadTest();

        final var encoder = getEncoder();
        assertNotNull( encoder );
        final var decoder = getDecoder();
        assertNotNull( decoder );

        {
            final var input = 0L;
            final var expected = new byte [] {48};
            var result = encoder.encode( input );
            assertNotNull( result );
            assertArrayEquals( expected, result );
        }

        {
            final var input = 123456789L;
            final var expected = new byte [] {51,78,81,75,56,78};
            var result = encoder.encode( input );
            assertNotNull( result );
            assertArrayEquals( expected, result );
        }

        {
            final var input = new byte [] {51,78,81,75,56,78};
            final var expected = 123456789L;
            var result = decoder.decodeToNumber( input );
            assertNotNull( result );
            assertEquals( expected, result.longValue() );
        }
    }   //  someTestsWithByteNumbers()

    /**
     *  Some arbitrary tests.
     *
     *  @throws Exception   Something unexpected gone wrong.
     */
    @Test
    final void someTestsWithStrings() throws Exception
    {
        skipThreadTest();

        final var encoder = getEncoder();
        assertNotNull( encoder );
        final var decoder = getDecoder();
        assertNotNull( decoder );

        String expected, input, result;

        input = EMPTY_STRING;
        expected = EMPTY_STRING;
        result = encoder.encodeToString( input );
        assertNotNull( result );
        assertEquals( expected, result );
        result = decoder.decodeToString( input );
        assertNotNull( result );
        assertEquals( expected, result );

        input = "123";
        expected = "32CHK";
        result = encoder.encodeToString( input );
        assertNotNull( result );
        assertEquals( expected, result );

        input = "123";
        expected = "3V";
        result = encoder.encodeToString( new BigInteger( input ) );
        assertNotNull( result );
        assertEquals( expected, result );

        input = "3V";
        expected = "123";
        result = decoder.decodeToNumber( input ).toString();
        assertNotNull( result );
        assertEquals( expected, result );

        input = "32CHK";
        expected = "123";
        result = decoder.decodeToString( input );
        assertNotNull( result );
        assertEquals( expected, result );
    }   //  someTestsWithStrings()

    /**
     *  The validation tests for
     *  {@link Decoder#decode(byte[])},
     *  {@link Decoder#decode(String)},
     *  {@link Decoder#decodeToNumber(byte[])}.
     *  {@link Decoder#decodeToNumber(String)}.
     *  {@link Decoder#decodeToString(byte[])},
     *  and
     *  {@link Decoder#decodeToString(String)}.
     *
     *  @throws Exception   Something unexpected gone wrong.
     */
    @Test
    final void testDecoderDecodeValidation() throws Exception
    {
        skipThreadTest();

        final var candidate = getDecoder();
        assertNotNull( candidate );

        assertThrows( NullArgumentException.class, () -> candidate.decode( (byte []) null ) );
        assertThrows( ValidationException.class, () -> candidate.decode( new byte [] {(byte) ' '} ) );
        assertThrows( ValidationException.class, () -> candidate.decode( new byte [] {(byte) '-'} ) );
        assertThrows( ValidationException.class, () -> candidate.decode( new byte [] {(byte) 'u'} ) );
        assertThrows( NullArgumentException.class, () -> candidate.decode( (String) null ) );
        assertThrows( ValidationException.class, () -> candidate.decode( " " ) );
        assertThrows( ValidationException.class, () -> candidate.decode( "-" ) );
        assertThrows( ValidationException.class, () -> candidate.decode( "u" ) );

        assertThrows( NullArgumentException.class, () -> candidate.decodeToNumber( (byte []) null ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToNumber( new byte [] {(byte) ' '} ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToNumber( new byte [] {(byte) '-'} ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToNumber( new byte [] {(byte) 'u'} ) );
        assertThrows( NullArgumentException.class, () -> candidate.decodeToNumber( (String) null ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToNumber( " " ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToNumber( "-" ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToNumber( "u" ) );

        assertThrows( NullArgumentException.class, () -> candidate.decodeToString( (byte []) null ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToString( new byte [] {(byte) ' '} ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToString( new byte [] {(byte) '-'} ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToString( new byte [] {(byte) 'u'} ) );
        assertThrows( NullArgumentException.class, () -> candidate.decodeToString( (String) null ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToString( " " ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToString( "-" ) );
        assertThrows( ValidationException.class, () -> candidate.decodeToString( "u" ) );
    }   //  testDecoderDecodeValidation()

    /**
     *  The validation tests for
     *  {@link Encoder#encode(byte[])},
     *  {@link Encoder#encode(long)},
     *  {@link Encoder#encode(BigInteger)},
     *  {@link Encoder#encodeToString(byte[])},
     *  {@link Encoder#encodeToString(long)},
     *  and
     *  {@link Encoder#encodeToString(BigInteger)}.
     *
     *  @throws Exception   Something unexpected gone wrong.
     */
    @Test
    final void testEncoderEncodeValidation() throws Exception
    {
        skipThreadTest();

        final var candidate = getEncoder();
        assertNotNull( candidate );

        assertThrows( NullArgumentException.class, () -> candidate.encode( (BigInteger) null ) );
        assertThrows( NullArgumentException.class, () -> candidate.encode( (byte []) null ) );
        assertThrows( NullArgumentException.class, () -> candidate.encodeToString( (BigInteger) null ) );
        assertThrows( NullArgumentException.class, () -> candidate.encodeToString( (byte []) null ) );

        final var value = BigInteger.valueOf( -1234L );
        assertThrows( ValidationException.class, () -> candidate.encode( value ) );
        assertThrows( ValidationException.class, () -> candidate.encodeToString( value ) );
    }   //  testEncoderEncodeValidation()

    /**
     *  Validates whether the class is static.
     */
    @Test
    final void validateClass()
    {
        assertTrue( validateAsStaticClass( Base32.class ) );
    }   //  validateClass()
}
//  class TestBase32

/*
 *  End of File
 */