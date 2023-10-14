/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.charsetutils;

import static java.lang.Character.MAX_CODE_POINT;
import static java.lang.Character.MIN_CODE_POINT;
import static java.lang.Character.MIN_HIGH_SURROGATE;
import static java.lang.Character.MIN_LOW_SURROGATE;
import static java.lang.Character.MIN_SUPPLEMENTARY_CODE_POINT;
import static java.lang.Character.codePointAt;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isBmpCodePoint;
import static java.lang.Character.isDefined;
import static java.lang.Character.isDigit;
import static java.lang.Character.isISOControl;
import static java.lang.Character.isIdeographic;
import static java.lang.Character.isLetter;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isSupplementaryCodePoint;
import static java.lang.Character.isTitleCase;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.isValidCodePoint;
import static java.lang.Character.isWhitespace;
import static java.lang.Integer.max;
import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.CharSetUtils.convertEscapedStringToUnicode;
import static org.tquadrat.foundation.util.CharSetUtils.convertUnicodeToASCII;
import static org.tquadrat.foundation.util.CharSetUtils.escapeCharacter;
import static org.tquadrat.foundation.util.CharSetUtils.unescapeUnicode;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.CharSetUtils;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link org.tquadrat.foundation.util.CharSetUtils#unescapeUnicode(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestUnescapeUnicode.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( {"MisorderedAssertEqualsArguments", "OverlyComplexClass"} )
@ClassVersion( sourceVersion = "$Id: TestUnescapeUnicode.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.charsetutils.TestUnescapeUnicode" )
public class TestUnescapeUnicode extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides code points for
     *  {@link #testUnicodeEscapeUnescape(int)}.<br>
     *  <br>Currently (as 2020-12-11), Unicode has the given number of
     *  characters of the categories below:<pre>
     *  count              = 283440
     *  alphabeticCount    = 132875
     *  bmpCount           =  63951 (same as single char)
     *  digitCount         =    650
     *  ideographicCount   = 101652
     *  isoControlCount    =     65
     *  letterCount        = 131241
     *  lowerCaseCount     =   2344
     *  notBmpCount        = 219489 (same as multi char)
     *  supplementaryCount = 219489
     *  titleCaseCount     =     31
     *  upperCaseCount     =   1911
     *  whitespaceCount    =     25</pre>
     *
     *  @return The stream with the code points.
     */
    static final IntStream provideCodePoints()
    {
        var alphabeticCount = 300;
        var notAlphabeticCount = 300;

        var bmpCount = 300;
        var notBmpCount = 300;

        var letterCount = 300;
        var digitCount = 25;
        var whitespaceCount = 10;
        var isoControlCount = 20;

        var ideographicCount = 300;
        var notIdeographicCount = 300;

        var lowerCaseCount = 150;
        var titleCaseCount = 10;
        var upperCaseCount = 150;

        var supplementaryCount = 100;

        var sum = Integer.MAX_VALUE;
        final var builder = IntStream.builder();
        var add = false;
        SearchLoop: for( var codePoint = MIN_CODE_POINT; (codePoint <= MAX_CODE_POINT) && (sum > 0); ++codePoint )
        {
            if( !isDefined( codePoint ) ) continue SearchLoop;
            if( !isValidCodePoint( codePoint ) ) continue SearchLoop;

            add = false;

            add |= (isAlphabetic( codePoint ) ? alphabeticCount > 0 : notAlphabeticCount > 0);
            add |= (isBmpCodePoint( codePoint ) ? bmpCount > 0 : notBmpCount > 0);
            add |= isDigit( codePoint ) && digitCount > 0;
            add |= (isIdeographic( codePoint ) ? ideographicCount > 0 : notIdeographicCount > 0);
            add |= isISOControl( codePoint ) && isoControlCount > 0;
            add |= isLetter( codePoint ) && letterCount > 0;
            add |= isLowerCase( codePoint ) && lowerCaseCount > 0;
            add |= isSupplementaryCodePoint( codePoint ) && supplementaryCount > 0;
            add |= isTitleCase( codePoint ) && titleCaseCount > 0;
            add |= isUpperCase( codePoint ) && upperCaseCount > 0;
            add |= isWhitespace( codePoint ) && whitespaceCount > 0;

            if( add )
            {
                if( isAlphabetic( codePoint ) )
                {
                    alphabeticCount = max( alphabeticCount - 1, 0 );
                }
                else
                {
                    notAlphabeticCount = max( notAlphabeticCount - 1, 0 );
                }
                if( isBmpCodePoint( codePoint ) )
                {
                    bmpCount = max( bmpCount - 1, 0 );
                }
                else
                {
                    notBmpCount = max( notBmpCount - 1, 0 );
                }
                if( isDigit( codePoint ) ) digitCount = max( digitCount - 1, 0 );
                if( isIdeographic( codePoint ) )
                {
                    ideographicCount = max( ideographicCount - 1, 0 );
                }
                else
                {
                    notIdeographicCount = max( notIdeographicCount - 1, 0 );
                }
                if( isISOControl( codePoint ) ) isoControlCount = max( isoControlCount - 1, 0 );
                if( isLetter( codePoint ) ) letterCount = max( letterCount - 1, 0 );
                if( isLowerCase( codePoint ) ) lowerCaseCount = max( lowerCaseCount - 1, 0 );
                if( isSupplementaryCodePoint( codePoint ) ) supplementaryCount = max( supplementaryCount - 1, 0 );
                if( isTitleCase( codePoint ) ) titleCaseCount = max( titleCaseCount - 1, 0 );
                if( isUpperCase( codePoint ) ) upperCaseCount = max( upperCaseCount - 1, 0 );
                if( isWhitespace( codePoint ) ) whitespaceCount = max( whitespaceCount - 1, 0 );

                builder.add( codePoint );

                sum = alphabeticCount + notAlphabeticCount
                    + bmpCount + notBmpCount
                    + digitCount
                    + ideographicCount + notIdeographicCount
                    + isoControlCount
                    + letterCount
                    + lowerCaseCount
                    + supplementaryCount
                    + titleCaseCount
                    + upperCaseCount
                    + whitespaceCount;
            }
        }   //  SearchLoop:

        if( sum > 0 )
        {
            final var msg = """
                alphabeticCount     = %d
                notAlphabeticCount  = %d
                bmpCount            = %d
                notBmpCount         = %d
                digitCount          = %d
                ideographicCount    = %d
                notIdeographicCount = %d
                isoControlCount     = %d
                letterCount         = %d
                lowerCaseCount      = %d
                supplementaryCount  = %d
                titleCaseCount      = %d
                upperCaseCount      = %d
                whitespaceCount     = %d""".formatted( alphabeticCount,
                notAlphabeticCount, bmpCount, notBmpCount, digitCount,
                ideographicCount, notIdeographicCount, isoControlCount,
                letterCount, lowerCaseCount, supplementaryCount,
                titleCaseCount, upperCaseCount, whitespaceCount );
            out.println( msg );
            fail( msg );
        }

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  provideCodePoints()

    /**
     *  Some tests for
     *  {@link org.tquadrat.foundation.util.CharSetUtils#convertUnicodeToASCII(CharSequence)}
     *  and
     *  {@link org.tquadrat.foundation.util.CharSetUtils#convertEscapedStringToUnicode(CharSequence)}.
     */
    @Test
    final void testConvertBackAndForth()
    {
        skipThreadTest();

        //---* Build the long String *-----------------------------------------
//        final var testData = selectCodePoints();
//        final var testString = new StringBuilder( testData.length *2 );
//
        out.println( "Building String" );
//        for( final var codePoint : testData ) testString.append( Character.toString( codePoint ) );
        final var testString = provideCodePoints()
            .mapToObj( Character::toString )
            .collect( joining() );

        out.println( "Converting to ASCII" );
        final var ascii = convertUnicodeToASCII( testString );
        assertNotNull( ascii );

        out.println( "Checking for ASCII only" );
        assertTrue( ascii.codePoints().allMatch( CharSetUtils::isPrintableASCIICharacter ) );

        out.println( "Converting back" );
        final var reverted = convertEscapedStringToUnicode( ascii );
        assertNotNull( reverted );
        assertEquals( testString.toString(), reverted );
    }   //  testConvertBackAndForth()

    /**
     *  Some tests for
     *  {@link org.tquadrat.foundation.util.CharSetUtils#unescapeUnicode(CharSequence)}.
     */
    @Test
    final void testUnescapeUnicodeWithEmptyArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            unescapeUnicode( EMPTY_CHARSEQUENCE );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testUnescapeUnicodeWithEmptyArgument()

    /**
     *  Some tests for
     *  {@link org.tquadrat.foundation.util.CharSetUtils#unescapeUnicode(CharSequence)}.
     */
    @Test
    final void testUnescapeUnicodeWithInvalidArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = ValidationException.class;
        try
        {
            unescapeUnicode( "123456" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "123456123456" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "\\u123456" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "\\u123456\\u123456" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "\\u123" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "\\u123\\u123" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "\\uXYZ0" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "\\u1234\\uXYZ0" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            unescapeUnicode( "\\u1234ABCDEF" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testUnescapeUnicodeWithInvalidArgument()

    /**
     *  Some tests for
     *  {@link org.tquadrat.foundation.util.CharSetUtils#unescapeUnicode(CharSequence)}.
     */
    @Test
    final void testUnescapeUnicodeWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            unescapeUnicode( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testUnescapeUnicodeWithNullArgument()

    /**
     *  Some tests for the methods
     *  {@link org.tquadrat.foundation.util.CharSetUtils#escapeCharacter(char)},
     *  {@link org.tquadrat.foundation.util.CharSetUtils#escapeCharacter(int)},
     *  and
     *  {@link org.tquadrat.foundation.util.CharSetUtils#unescapeUnicode(CharSequence)}
     */
    @Test
    final void testUnicodeEscapeUnescape()
    {
        skipThreadTest();

        //---* Single char character *-----------------------------------------
        var codePoint = 0x1346;
        var c = (char) codePoint;
        var actual = escapeCharacter( c );
        assertNotNull( actual );
        assertEquals( "\\u1346", actual );

        actual = unescapeUnicode( actual );
        assertNotNull( actual );
        assertEquals( actual.length(), 1 );
        assertEquals( actual.charAt( 0 ), c );

        actual = escapeCharacter( codePoint );
        assertNotNull( actual );
        assertEquals( "\\u1346", actual );

        actual = unescapeUnicode( actual );
        assertNotNull( actual );
        assertEquals( actual.length(), 1 );
        assertEquals( actual.charAt( 0 ), c );

        //---* Surrogate character *-------------------------------------------
        codePoint = 0x103B0;
        c = (char) codePoint; // This will not work!!
        actual = escapeCharacter( c );
        assertNotNull( actual );
        assertEquals( "\\u03b0", actual );
        assertNotEquals( "\\u103b0", actual );

        actual = escapeCharacter( codePoint );
        assertNotNull( actual );
        assertEquals( "\\ud800\\udfb0", actual );

        actual = unescapeUnicode( actual );
        assertNotNull( actual );
        assertEquals( actual.length(), 2 );
        assertEquals( codePoint, codePointAt( actual, 0 ) );
    }   //  testUnicodeEscapeUnescape()

    /**
     *  Some tests for the methods
     *  {@link org.tquadrat.foundation.util.CharSetUtils#escapeCharacter(int)},
     *  and
     *  {@link org.tquadrat.foundation.util.CharSetUtils#unescapeUnicode(CharSequence)}
     *
     *  @param  codePoint   The character for the test.
     */
    @ParameterizedTest
    @MethodSource( "provideCodePoints" )
    final void testUnicodeEscapeUnescape( final int codePoint )
    {
        skipThreadTest();

        /*
         * The test will succeed only for valid code points.
         */
        var actual = escapeCharacter( codePoint );
        assertNotNull( actual );
        if( isBmpCodePoint( codePoint ) )
        {
            //---* Single char *---------------------------------------
            assertEquals( 6, actual.length() );
            assertEquals( format( "\\u%04x", codePoint ), actual );
        }
        else
        {
            //---* Surrogate char *------------------------------------
            assertEquals( 12, actual.length() );
            assertEquals( format( "\\u%04x\\u%04x", ((codePoint >>> 10) + (MIN_HIGH_SURROGATE - (MIN_SUPPLEMENTARY_CODE_POINT >>> 10))), ((codePoint & 0x3ff) + MIN_LOW_SURROGATE) ), actual );
        }

        actual = unescapeUnicode( actual );
        assertNotNull( actual );
        if( isBmpCodePoint( codePoint ) )
        {
            assertEquals( actual.length(), 1 );
        }
        else
        {
            assertEquals( actual.length(), 2 );
        }
        assertEquals( codePoint, codePointAt( actual, 0 ) );
    }   //  testUnicodeEscapeUnescape()
}
//  class TestUnescapeUnicode

/*
 *  End of File
 */