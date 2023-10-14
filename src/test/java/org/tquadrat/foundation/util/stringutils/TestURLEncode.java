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

import static java.lang.String.format;
import static java.lang.System.out;
import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.util.StringUtils.urlDecode;
import static org.tquadrat.foundation.util.StringUtils.urlEncode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#urlEncode(CharSequence)}
 *  and
 *  {@link StringUtils#urlDecode(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestURLEncode.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestURLEncode.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestURLEncode" )
public class TestURLEncode extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#urlDecode(CharSequence)}.
     *
     *  @param  plain   The plain text.
     *  @param  encoded The encoded text.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "URLEncode.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testUrlDecode( final String plain, final String encoded )
    {
        skipThreadTest();

        final var actual = urlDecode( translateEscapes( encoded ) );
        assertNotNull( actual );
        assertEquals( translateEscapes( plain ), actual );
    }   //  testUrlDecode()

    /**
     *  Tests for
     *  {@link StringUtils#urlEncode(CharSequence)}
     *  and
     *  {@link StringUtils#urlDecode(CharSequence)}.
     *
     *  @param  plain   The plain text.
     *  @param  encoded The encoded text.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "URLEncode.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testUrlDecodeEncode( @SuppressWarnings( "unused" ) final String plain, final String encoded )
    {
        skipThreadTest();

        final var candidate = translateEscapes( encoded );

        assertEquals( candidate, urlEncode( urlDecode( candidate ) ) );
    }   //  testUrlDecodeEncode()

    /**
     *  Tests for
     *  {@link StringUtils#urlDecode(CharSequence)}.
     */
    @Test
    final void testUrlDecodeWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            urlDecode( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testUrlDecodeWithNullArgument()

    /**
     *  Tests for
     *  {@link StringUtils#urlEncode(CharSequence)}.
     *
     *  @param  plain   The plain text.
     *  @param  encoded The encoded text.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "URLEncode.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testUrlEncode( final String plain, final String encoded )
    {
        skipThreadTest();

        final var actual = urlEncode( translateEscapes( plain ) );
        assertNotNull( actual );
        assertEquals( translateEscapes( encoded ), actual );
    }   //  testUrlEncode()

    /**
     *  Tests for
     *  {@link StringUtils#urlEncode(CharSequence)}
     *  and
     *  {@link StringUtils#urlDecode(CharSequence)}.
     *
     *  @param  plain   The plain text.
     *  @param  encoded The encoded text.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "URLEncode.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testUrlEncodeDecode( final String plain, @SuppressWarnings( "unused" ) final String encoded )
    {
        skipThreadTest();

        final var candidate = translateEscapes( plain );

        assertEquals( candidate, urlDecode( urlEncode( candidate ) ) );
    }   //  testUrlEncodeDecode()

    /**
     *  Tests for
     *  {@link StringUtils#urlEncode(CharSequence)}.
     */
    @Test
    final void testUrlEncodeNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            urlDecode( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testUrlEncodeNull()

    /**
     *  Validates the test data.
     *
     *  @param  plain   The plain text.
     *  @param  encoded The encoded text.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "URLEncode.csv", delimiter = ';', numLinesToSkip = 1 )
    final void validateTestData( final String plain, final String encoded )
    {
        skipThreadTest();

        assertEquals( translateEscapes( encoded ), encode( translateEscapes( plain ), UTF8 ) );
        assertEquals( translateEscapes( plain ), decode( translateEscapes( encoded ), UTF8 ) );
    }   //  validateTestData()
}
//  class TestURLEncode

/*
 *  End of File
 */