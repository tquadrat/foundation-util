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
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.breakString;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#breakString(CharSequence, int)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestBreakString.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestBreakString.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestBreakString" )
public class TestBreakString extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#breakString(CharSequence,int)}.
     *
     *  @param  argument    The argument for the test.
     */
    @SuppressWarnings( "SimplifyStreamApiCallChains" )
    @ParameterizedTest
    @CsvFileSource( resources = "BreakString.csv" )
    final void testBreakString( final String argument )
    {
        skipThreadTest();

        final var value = translateEscapes( argument );

        final var chunk = 4;
        final var argLen = value.length();
        final var actual = breakString( value, chunk );
        assertNotNull( actual );
        final var actualArray = actual.toArray( String []::new );
        assertEquals( (argLen / chunk) + Integer.signum( argLen % chunk ), actualArray.length );
        assertEquals( value, stream( actualArray ).collect( Collectors.joining() ) );
        assertEquals( value, String.join( EMPTY_STRING, actualArray ) );
        stream( actualArray ).limit( argLen / chunk ).forEach( a -> assertEquals( chunk, a.length() ) );
    }   //  testBreakString()

    /**
     *  Tests for
     *  {@link StringUtils#breakString(CharSequence,int)}.
     */
    @Test
    final void testBreakStringWithInvalidChunkArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = ValidationException.class;
        try
        {
            breakString( "String", -1 );
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
            breakString( "String", 0 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testBreakStringWithInvalidChunkArgument()

    /**
     *  Tests for
     *  {@link StringUtils#breakString(CharSequence,int)}.
     */
    @Test
    final void testBreakStringWithNullTextArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            breakString( null, 10 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testBreakStringWithNullTextArgument()

}
//  class TestBreakString

/*
 *  End of File
 */