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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.checkTextLen;
import static org.tquadrat.foundation.util.StringUtils.checkTextLenNull;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.CharSequenceTooLongException;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#checkTextLen(String, CharSequence, int)}
 *  and
 *  {@link StringUtils#checkTextLenNull(String, CharSequence, int)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestCheckTextLen.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestCheckTextLen.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestCheckTextLen" )
public class TestCheckTextLen extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#checkTextLen(String,CharSequence,int)}
     *
     *  @param  result  {@code true} if the execution should be successful,
     *      {@code false} if it is expected to fail.
     *  @param  text    The text to check.
     *  @param  maxLength   The maximum length for the text.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "CheckTextLen.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testCheckTextLen( final boolean result, final String text, final int maxLength )
    {
        skipThreadTest();

        final var candidate = translateEscapes( text );

        if( result )
        {
            final var actual = checkTextLen( "name", candidate, maxLength );
            assertNotNull( actual );
            assertTrue( actual.length() <= maxLength );
            assertEquals( candidate, actual );
        }
        else
        {
            final Class<? extends Throwable> expectedException = CharSequenceTooLongException.class;
            try
            {
                checkTextLen( "name", candidate, maxLength );
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
    }   //  testCheckTextLen()

    /**
     *  Tests for
     *  {@link StringUtils#checkTextLenNull(String,CharSequence,int)}
     *
     *  @param  result  {@code true} if the execution should be successful,
     *      {@code false} if it is expected to fail.
     *  @param  text    The text to check.
     *  @param  maxLength   The maximum length for the text.
     */
    @ParameterizedTest
    @CsvFileSource( resources = {"CheckTextLen.csv", "CheckTextLenNull.csv"}, delimiter = ';', numLinesToSkip = 1 )
    final void testCheckTextLenNull( final boolean result, final String text, final int maxLength )
    {
        skipThreadTest();

        if( result )
        {
            final var actual = checkTextLenNull( "name", text, maxLength );
            if( nonNull( text ) )
            {
                assertNotNull( actual );
                assertTrue( actual.length() <= maxLength );
                assertEquals( text, actual );
            }
            else
            {
                assertNull( actual );
            }
        }
        else
        {
            final Class<? extends Throwable> expectedException = CharSequenceTooLongException.class;
            try
            {
                checkTextLenNull( "name", text, maxLength );
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
    }   //  testCheckTextLenNull()

    /**
     *  Tests for
     *  {@link StringUtils#checkTextLenNull(String,CharSequence,int)}.
     */
    @Test
    final void testCheckTextLenNullWithEmptyNameArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            checkTextLenNull( EMPTY_STRING, "String", 6 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckTextLenNullWithEmptyNameArgument()

    /**
     *  Tests for
     *  {@link StringUtils#checkTextLenNull(String,CharSequence,int)}.
     */
    @Test
    final void testCheckTextLenNullWithNullNameArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            checkTextLenNull( null, "String", 6 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckTextLenNullWithNullNameArgument()

    /**
     *  Tests for
     *  {@link StringUtils#checkTextLen(String,CharSequence,int)}.
     */
    @Test
    final void testCheckTextLenWithEmptyNameArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            checkTextLen( EMPTY_STRING, "text", -1 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckTextLenWithEmptyNameArgument()

    /**
     *  Tests for
     *  {@link StringUtils#checkTextLen(String,CharSequence,int)}.
     */
    @Test
    final void testCheckTextLenWithEmptyTextArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            checkTextLen( "name", EMPTY_STRING, -1 );
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
            checkTextLen( "name", EMPTY_STRING, 0 );
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
            checkTextLen( "name", EMPTY_STRING, -6 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckTextLenWithEmptyTextArgument()

    /**
     *  Tests for
     *  {@link StringUtils#checkTextLen(String,CharSequence,int)}.
     */
    @Test
    final void testCheckTextLenWithNullNameArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            checkTextLen( null, "text", -1 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckTextLenWithNullNameArgument()

    /**
     *  Tests for
     *  {@link StringUtils#checkTextLen(String,CharSequence,int)}.
     */
    @Test
    final void testCheckTextLenWithNullTextArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            checkTextLen( "name", null, -1 );
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
            checkTextLen( "name", null, 0 );
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
            checkTextLen( "name", null, 6 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCheckTextLenWithNullTextArgument()
}
//  class TestCheckTextLen

/*
 *  End of File
 */