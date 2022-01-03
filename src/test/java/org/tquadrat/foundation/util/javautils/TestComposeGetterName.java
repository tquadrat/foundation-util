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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.JavaUtils.composeGetterName;
import static org.tquadrat.foundation.util.StringUtils.capitalize;
import static org.tquadrat.foundation.util.StringUtils.format;

/**
 *  Test for the method
 *  {@link JavaUtils#composeGetterName(String)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestComposeGetterName.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestComposeGetterName" )
public class TestComposeGetterName extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for the usage of
     *  {@link JavaUtils#composeGetterName(String)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testComposeGetterName() throws Exception
    {
        skipThreadTest();

        final String actual;
        final String expected;
        final String propertyName;

        propertyName = "property";
        expected = format( "get%s", capitalize( propertyName ) );
        actual = composeGetterName( propertyName );
        assertNotNull( actual );
        assertEquals( expected, actual );
    }   //  testComposeGetterName()

    /**
     *  Test method for the failure of
     *  {@link JavaUtils#composeGetterName(String)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testComposeGetterNameWithEmptyArgument() throws Exception
    {
        skipThreadTest();

        /*
         *  The empty string is not a valid property name.
         */
        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            final var result = composeGetterName( EMPTY_STRING );
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
    }   //  testComposeGetterNameWithEmptyArgument()

    /**
     *  Test method for the failure of
     *  {@link JavaUtils#composeGetterName(String)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testComposeGetterNameWithNullArgument() throws Exception
    {
        skipThreadTest();

        /*
         *  null is not a valid property name.
         */
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            final var result = composeGetterName( EMPTY_STRING );
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
    }   //  testComposeGetterNameWithNullArgument()
}
//  class TestComposeGetterName

/*
 *  End of File
 */