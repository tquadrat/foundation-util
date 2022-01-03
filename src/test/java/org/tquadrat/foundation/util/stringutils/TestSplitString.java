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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.lang.Objects.deepEquals;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.splitString;

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
 *  {@link StringUtils#splitString(CharSequence, int)},
 *  {@link StringUtils#splitString(CharSequence, char)}
 *  and
 *  {@link StringUtils#splitString(CharSequence, CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestSplitString.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestSplitString.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestSplitString" )
public class TestSplitString extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#splitString(CharSequence,char)},
     *  {@link StringUtils#splitString(CharSequence,CharSequence)},
     *  and
     *  {@link StringUtils#splitString(CharSequence,int)}
     *
     *  @param  data    The test data.
     */
    @ParameterizedTest
    @MethodSource( "org.tquadrat.foundation.util.helper.SplitStringProvider#provideSplitStringData" )
    final void testSplitString( final SplitStringData data )
    {
        skipThreadTest();

        final var separator = '|';
        final int codepoint = separator;
        final CharSequence separatorSequence = Character.toString( separator );

        final var expected = data.expected();
        final var input = data.input();
        String [] actual;

        actual = splitString( input, separator );
        assertNotNull( actual );
        assertTrue( actual.length > 0);
        assertTrue( deepEquals( expected, actual ) );

        actual = splitString( input, codepoint );
        assertNotNull( actual );
        assertTrue( actual.length > 0);
        assertTrue( deepEquals( expected, actual ) );

        actual = splitString( input, separatorSequence );
        assertNotNull( actual );
        assertTrue( actual.length > 0);
        assertTrue( deepEquals( expected, actual ) );
    }   //  testSplitString()

    /**
     *  Tests for
     *  {@link StringUtils#splitString(CharSequence,CharSequence)}
     */
    @Test
    final void testSplitStringWithEmptySeparatorArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            splitString( "string", EMPTY_CHARSEQUENCE );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSplitStringWithEmptySeparatorArgument()

    /**
     *  Tests for
     *  {@link StringUtils#splitString(CharSequence,CharSequence)}
     */
    @Test
    final void testSplitStringWithNullSeparatorArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            splitString( "string", null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSplitStringWithNullSeparatorArgument()

    /**
     *  Tests for
     *  {@link StringUtils#splitString(CharSequence,char)},
     *  {@link StringUtils#splitString(CharSequence,CharSequence)},
     *  and
     *  {@link StringUtils#splitString(CharSequence,int)}
     */
    @Test
    final void testSplitStringWithNullStringArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            splitString( null, ' ' );
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
            splitString( null, 42 );
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
            splitString( null, "\n" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSplitStringWithNullStringArgument()
}
//  class TestSplitString

/*
 *  End of File
 */