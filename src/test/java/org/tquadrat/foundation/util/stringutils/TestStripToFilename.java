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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.stripToFilename;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#stripToFilename(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestStripToFilename.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestStripToFilename.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestStripToFilename" )
public class TestStripToFilename extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#stripToFilename(CharSequence)}.
     *
     *  @param  result  {@code true} if the input is valid, {@code false}
     *      otherwise.
     *  @param  input   The input String.
     *  @param  output  The expected result
     *
     */
    @ParameterizedTest
    @CsvFileSource( resources = "StripToFilename.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testStripToFilename( final boolean result, final String input, final String output )
    {
        skipThreadTest();

        final var candidate = translateEscapes( input );
        final var expected = translateEscapes( output );

        if( result )
        {
            final var actual = stripToFilename( candidate );
            assertNotNull( actual );
            assertTrue( isNotEmptyOrBlank( actual ) );
            assertEquals( expected, actual );
        }
        else
        {
            final Class<? extends Throwable> expectedException = ValidationException.class;
            try
            {
                stripToFilename( input );
                fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
            }
            catch( final AssertionError e ) { throw e; }
            catch( final Throwable t )
            {
                final var isExpectedException = expectedException.isInstance( t );
                if( !isExpectedException ) t.printStackTrace( out );
                assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
            }
        }
    }   //  testStripToFilename()

    /**
     *  Tests for
     *  {@link StringUtils#stripToFilename(CharSequence)}.
     */
    @Test
    final void testStripToFilenameWithEmptyArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            stripToFilename( EMPTY_CHARSEQUENCE );
            fail( format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStripToFilenameWithEmptyArgument()

    /**
     *  Tests for
     *  {@link StringUtils#stripToFilename(CharSequence)}.
     */
    @Test
    final void testStripToFilenameWithInvalidArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = ValidationException.class;

        var argument = ":\\/;*\"'@|?<>";
        try
        {
            stripToFilename( argument );
            fail( format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        argument = "   ";
        try
        {
            stripToFilename( argument );
            fail( format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        argument = "\t\n";
        try
        {
            stripToFilename( argument );
            fail( format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStripToFilenameWithInvalidArgument()

    /**
     *  Tests for
     *  {@link StringUtils#stripToFilename(CharSequence)}.
     */
    @Test
    final void testStripToFilenameWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            stripToFilename( null );
            fail( format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStripToFilenameWithNullArgument()
}
//  class TestStripToFilename

/*
 *  End of File
 */