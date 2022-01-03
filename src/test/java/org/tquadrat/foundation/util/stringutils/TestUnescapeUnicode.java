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

package org.tquadrat.foundation.util.stringutils;

import static java.lang.Character.MAX_CODE_POINT;
import static java.lang.Character.MIN_CODE_POINT;
import static java.lang.Character.MIN_HIGH_SURROGATE;
import static java.lang.Character.MIN_LOW_SURROGATE;
import static java.lang.Character.MIN_SUPPLEMENTARY_CODE_POINT;
import static java.lang.Character.codePointAt;
import static java.lang.Character.isBmpCodePoint;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#unescapeUnicode(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestUnescapeUnicode.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( {"MisorderedAssertEqualsArguments", "removal"} )
@ClassVersion( sourceVersion = "$Id: TestUnescapeUnicode.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestUnescapeUnicode" )
public class TestUnescapeUnicode extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides code points for
     *  {@link #testUnicodeEscapeUnescape(int)}.
     *
     *  @return The stream with the code points.
     */
    static final IntStream provideCodePoints()
    {
        final var retValue = IntStream.range( MIN_CODE_POINT, MAX_CODE_POINT + 1 )
            .filter( Character::isValidCodePoint )
            .filter( Character::isDefined );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  provideCodePoints()

    /**
     *  Some tests for
     *  {@link StringUtils#unescapeUnicode(CharSequence)}.
     */
    @Test
    final void testUnescapeUnicodeWithEmptyArgument()
    {
        skipThreadTest();
        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            StringUtils.unescapeUnicode( EMPTY_CHARSEQUENCE );
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
     *  {@link StringUtils#unescapeUnicode(CharSequence)}.
     */
    @Test
    final void testUnescapeUnicodeWithInvalidArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = ValidationException.class;
        try
        {
            StringUtils.unescapeUnicode( "123456" );
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
            StringUtils.unescapeUnicode( "123456123456" );
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
            StringUtils.unescapeUnicode( "\\u123456" );
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
            StringUtils.unescapeUnicode( "\\u123456\\u123456" );
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
            StringUtils.unescapeUnicode( "\\u123" );
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
            StringUtils.unescapeUnicode( "\\u123\\u123" );
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
            StringUtils.unescapeUnicode( "\\uXYZ0" );
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
            StringUtils.unescapeUnicode( "\\u1234\\uXYZ0" );
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
            StringUtils.unescapeUnicode( "\\u1234ABCDEF" );
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
     *  {@link StringUtils#unescapeUnicode(CharSequence)}.
     */
    @Test
    final void testUnescapeUnicodeWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            StringUtils.unescapeUnicode( null );
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
     *  {@link StringUtils#escapeUnicode(char)},
     *  {@link StringUtils#escapeUnicode(int)},
     *  and
     *  {@link StringUtils#unescapeUnicode(CharSequence)}
     */
    @Test
    final void testUnicodeEscapeUnescape()
    {
        skipThreadTest();

        //---* Single char character *-----------------------------------------
        var codePoint = 0x1346;
        var c = (char) codePoint;
        var actual = StringUtils.escapeUnicode( c );
        assertNotNull( actual );
        assertEquals( "\\u1346", actual );

        actual = StringUtils.unescapeUnicode( actual );
        assertNotNull( actual );
        assertEquals( actual.length(), 1 );
        assertEquals( actual.charAt( 0 ), c );

        actual = StringUtils.escapeUnicode( codePoint );
        assertNotNull( actual );
        assertEquals( "\\u1346", actual );

        actual = StringUtils.unescapeUnicode( actual );
        assertNotNull( actual );
        assertEquals( actual.length(), 1 );
        assertEquals( actual.charAt( 0 ), c );

        //---* Surrogate character *-------------------------------------------
        codePoint = 0x103B0;
        c = (char) codePoint; // This will not work!!
        actual = StringUtils.escapeUnicode( c );
        assertNotNull( actual );
        assertEquals( "\\u03b0", actual );
        assertNotEquals( "\\u103b0", actual );

        actual = StringUtils.escapeUnicode( codePoint );
        assertNotNull( actual );
        assertEquals( "\\ud800\\udfb0", actual );

        actual = StringUtils.unescapeUnicode( actual );
        assertNotNull( actual );
        assertEquals( actual.length(), 2 );
        assertEquals( codePoint, codePointAt( actual, 0 ) );
    }   //  testUnicodeEscapeUnescape()

    /**
     *  Some tests for the methods
     *  {@link StringUtils#escapeUnicode(int)},
     *  and
     *  {@link StringUtils#unescapeUnicode(CharSequence)}
     *
     *  @param  codePoint   The character for the test.
     */
    @Disabled
    @ParameterizedTest
    @MethodSource( "provideCodePoints" )
    final void testUnicodeEscapeUnescape( final int codePoint )
    {
        skipThreadTest();

        /*
         * The test will succeed only for valid code points.
         */
        var actual = StringUtils.escapeUnicode( codePoint );
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

        actual = StringUtils.unescapeUnicode( actual );
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