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
import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.lang.Objects.deepEquals;
import static org.tquadrat.foundation.util.StringUtils.escapeRegex;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.stream;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;
import org.tquadrat.foundation.util.helper.SplitStringData;

/**
 *  Some tests for the method
 *  {@link StringUtils#stream(CharSequence, char)},
 *  {@link StringUtils#stream(CharSequence, CharSequence)},
 *  {@link StringUtils#stream(CharSequence,int)}
 *  and
 *  {@link StringUtils#stream(CharSequence,Pattern)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestStream.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestStream.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestStream" )
public class TestStream extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#stream(CharSequence,char)},
     *  {@link StringUtils#stream(CharSequence,CharSequence)},
     *  {@link StringUtils#stream(CharSequence,int)}
     *  and
     *  {@link StringUtils#stream(CharSequence,Pattern)}.
     *
     *  @param  data    The test data.
     */
    @ParameterizedTest
    @MethodSource( "org.tquadrat.foundation.util.helper.SplitStringProvider#provideSplitStringData" )
    final void testStream( final SplitStringData data )
    {
        skipThreadTest();

        final var separator = '|';
        final int codepoint = separator;
        final CharSequence separatorSequence = Character.toString( separator );
        final var pattern = compile( escapeRegex( separatorSequence.toString() ) );

        final var expected = data.expected();
        final var input = data.input();
        String [] actual;

        actual = stream( input, separator ).toArray( String []::new );
        assertNotNull( actual );
        assertTrue( actual.length > 0);
        assertTrue( deepEquals( expected, actual ) );

        actual = stream( input, codepoint ).toArray( String []::new );
        assertNotNull( actual );
        assertTrue( actual.length > 0);
        assertTrue( deepEquals( expected, actual ) );

        actual = stream( input, separatorSequence ).toArray( String []::new );
        assertNotNull( actual );
        assertTrue( actual.length > 0);
        assertTrue( deepEquals( expected, actual ) );

        actual = stream( input, pattern ).toArray( String []::new );
        assertNotNull( actual );
        assertTrue( actual.length > 0);
        assertTrue( deepEquals( expected, actual ) );
    }   //  testStream()

    /**
     *  Tests for
     *  {@link StringUtils#stream(CharSequence,CharSequence)}.
     */
    @Test
    final void testStreamWithEmptySeparatorArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            stream( "string", EMPTY_CHARSEQUENCE );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStreamWithEmptySeparatorArgument()

    /**
     *  Tests for
     *  {@link StringUtils#stream(CharSequence,CharSequence)}
     *  and
     *  {@link StringUtils#stream(CharSequence,Pattern)}.
     */
    @Test
    final void testStreamWithNullSeparatorArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            stream( "string", (CharSequence) null );
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
            stream( "string", (Pattern) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStreamWithNullSeparatorArgument()

    /**
     *  Tests for
     *  Tests for
     *  {@link StringUtils#stream(CharSequence,char)},
     *  {@link StringUtils#stream(CharSequence,CharSequence)},
     *  {@link StringUtils#stream(CharSequence,int)}
     *  and
     *  {@link StringUtils#stream(CharSequence,Pattern)}.
     */
    @Test
    final void testStreamWithNullStringArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            stream( null, ' ' );
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
            stream( null, 42 );
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
            stream( null, "\n" );
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
            stream( null, compile( ".*" ) );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStreamWithNullStringArgument()
}
//  class TestStream

/*
 *  End of File
 */