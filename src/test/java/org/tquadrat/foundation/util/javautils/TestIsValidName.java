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

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.JavaUtils.isValidName;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;

/**
 *  Test for the method
 *  {@link JavaUtils#isValidName(CharSequence)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestIsValidName.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestIsValidName" )
public class TestIsValidName extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link JavaUtils#isValidName(CharSequence)}.
     *
     *  @param  result  The result of the check.
     *  @param  name    The name to check.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "IsValidNameData.csv", numLinesToSkip = 1, delimiter = ';' )
    final void testIsValidName( final boolean result, final CharSequence name ) throws Exception
    {
        skipThreadTest();

        if( result )
        {
            assertTrue( isValidName(  name.toString().translateEscapes() ) );
        }
        else
        {
            assertFalse( isValidName( name.toString().translateEscapes() ) );
        }
    }   //  testIsValidName()

    /**
     *  Tests for the method
     *  {@link JavaUtils#isValidName(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testIsValidNameWithEmptyArgument() throws Exception
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            isValidName( EMPTY_STRING );
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
    }   //  testIsValidNameWithEmptyArgument()

    /**
     *  Tests for the method
     *  {@link JavaUtils#isValidName(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testIsValidNameWithNullArgument() throws Exception
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            isValidName( null );
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
    }   //  testIsValidNameWithNullArgument()
}
//  class TestIsValidName

/*
 *  End of File
 */