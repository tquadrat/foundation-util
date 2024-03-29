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

package org.tquadrat.foundation.util.javautils;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.JavaUtils.composeSetterName;
import static org.tquadrat.foundation.util.StringUtils.capitalize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;

/**
 *  Test for the method
 *  {@link JavaUtils#composeSetterName(String)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestComposeSetterName.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestComposeSetterName" )
public class TestComposeSetterName extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for the usage of
     *  {@link JavaUtils#composeSetterName(String)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testComposeSetterName() throws Exception
    {
        skipThreadTest();

        final String actual;
        final String expected;
        final String propertyName;

        propertyName = "property";
        expected = format( "set%s", capitalize( propertyName ) );
        actual = composeSetterName( propertyName );
        assertNotNull( actual );
        assertEquals( expected, actual );
    }   //  testComposeSetterName()

    /**
     *  Test method for the failure of
     *  {@link JavaUtils#composeSetterName(String)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testComposeSetterNameWithEmptyArgument() throws Exception
    {
        skipThreadTest();

        /*
         *  The empty string is not a valid property name.
         */
        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            final var result = composeSetterName( EMPTY_STRING );
            assertNotNull( result );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeSetterNameWithEmptyArgument()

    /**
     *  Test method for the failure of
     *  {@link JavaUtils#composeSetterName(String)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testComposeSetterNameWithNullArgument() throws Exception
    {
        skipThreadTest();

        /*
         *  null is not a valid property name.
         */
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            final var result = composeSetterName( null );
            assertNotNull( result );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeSetterNameWithNullArgument()
}
//  class TestComposeSetterName

/*
 *  End of File
 */