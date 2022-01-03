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

import static java.lang.System.out;
import static java.util.Locale.GERMANY;
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.time.Instant;
import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;
import org.tquadrat.foundation.util.helper.FormattableObject;

/**
 *  Some tests for the methods
 *  {@link StringUtils#format(String, Object...)}
 *  and
 *  {@link StringUtils#format(Locale, String, Object...)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestFormat.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestFormat.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestFormat" )
public class TestFormat extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#format(String,Object...)}
     *  and
     *  {@link StringUtils#format(Locale,String,Object...)}
     */
    @Test
    final void testFormat()
    {
        skipThreadTest();

        String actual, expected, format;
        final var args = new Object [] { "Text", Instant.now() };

        format = "String";
        actual = format( format, args );
        expected = String.format( format, args );
        assertEquals( expected, actual );
        actual = format( GERMANY, format, args );
        expected = String.format( GERMANY, format, args );
        assertEquals( expected, actual );

        format = "%2$s %1$s";
        actual = format( format, args );
        expected = String.format( format, args );
        assertEquals( expected, actual );
        actual = format( GERMANY, format, args );
        expected = String.format( GERMANY, format, args );
        assertEquals( expected, actual );
    }   //  testFormat()

    /**
     *  Tests for
     *  {@link StringUtils#format(String,Object...)}
     *  and
     *  {@link StringUtils#format(Locale,String,Object...)}
     */
    @Test
    final void testFormatFormattable()
    {
        skipThreadTest();

        String actual, expected;
        final var format = "Some Text before %2$s Some Text in between %1$s Some Text after";
        final var now = Instant.now();
        final var args = new Object [] { new FormattableObject( now ), now };

        expected = "Classname: "+ args [0].getClass().getName() + ": Now: " + now.toString() + " (never too late!)";
        assertEquals( expected + expected, format( "%s", args [0] ) );
        assertEquals( expected + expected, String.format( "%s", args [0] ) );

        actual = format( format, args );
        expected = String.format( format, args );
        assertEquals( expected, actual );
        actual = format( GERMANY, format, args );
        expected = String.format( GERMANY, format, args );
        assertEquals( expected, actual );
    }   //  testFormat()

    /**
     *  Tests for
     *  {@link StringUtils#format(String,Object...)}
     *  and
     *  {@link StringUtils#format(Locale,String,Object...)}
     */
    @Test
    final void testFormatWithNullFormatArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullPointerException.class;
        try
        {
            format( (String) null, "String", 6 );
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
            format( ROOT, null, "String", 6 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testFormatWithNullFormatArgument()
}
//  class TestFormat

/*
 *  End of File
 */