/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
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

package org.tquadrat.foundation.util.systemutils;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.SystemUtils.convertIPAddress;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SystemUtils;

/**
 *  Some tests for the method
 *  {@link SystemUtils#convertIPAddress(CharSequence)}.
 *  from the class
 *  {@link SystemUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestConvertIPAddress.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestConvertIPAddress.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.systemutils.TestConvertIPAddress" )
public class TestConvertIPAddress extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the method
     *  {@link SystemUtils#convertIPAddress(CharSequence)}
     *
     *  @param  candidate   The value for the test.
     */
    @ParameterizedTest
    @ValueSource( strings =
        {   // IPv6:
            "::", "102:304:506:708:90a:b0c:d0e:f10",

            // IPv4:
            "192.168.26.126", "127.0.0.1"
        }
    )
    final void testConvertIPAddress( final String candidate )
    {
        skipThreadTest();

        InetAddress result = null;
        try
        {
            result = convertIPAddress( candidate );
            assertNotNull( result );
            if( candidate.contains( ":" ) )
            {
                assertTrue( result instanceof Inet6Address );
            }
            else
            {
                assertTrue( result instanceof Inet4Address );
            }
            assertTrue( result.toString().startsWith( "/" ) );
        }
        catch( final AssertionError e )
        {
            if( result != null )
            {
                out.printf( "Result: %s (Class: %s)\n", result.getHostAddress(), result.getClass().getName() );
            }
            throw e;
        }
        catch( final IllegalArgumentException e )
        {
            fail( format( "Value '%s' could not be converted to a valid IP Address", candidate ), e );
        }
    }   //  testConvertIPAddress()

    /**
     *  Test for the method
     *  {@link SystemUtils#convertIPAddress(CharSequence)}
     */
    @Test
    final void testConvertIPAddressWithEmptyArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            convertIPAddress( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConvertIPAddressWithEmptyArgument()

    /**
     *  Test for the method
     *  {@link SystemUtils#convertIPAddress(CharSequence)}
     *
     *  @param  candidate   The value for the test.
     */
    @ParameterizedTest
    @ValueSource( strings =
        { "1", "1.", "1.1", "1.1.", "1.1.1", "1.1.1.", "257.1.1.2", "Otto",
          "1..1.1", " ", "1 .2 .3 .4", "30.168.1.255.1", "127.1",
          "192.168.1.256", "-1.2.3.4", "3...3", ":", ":Walter:" }
    )
    final void testConvertIPAddressWithInvalidArgument( final String candidate )
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
        try
        {
            final var result = convertIPAddress( candidate );
            out.printf( "Result: %s (Class: %s)\n", result.getHostAddress(), result.getClass().getName() );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConvertIPAddressWithInvalidArgument()

    /**
     *  Test for the method
     *  {@link SystemUtils#convertIPAddress(CharSequence)}
     */
    @Test
    final void testConvertIPAddressWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            convertIPAddress( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConvertIPAddressWithNullArgument()
}
//  class TestConvertIPAddress

/*
 *  End of File
 */