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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;
import org.tquadrat.foundation.util.helper.VariableNameProvider;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

/**
 *  Some tests for the method
 *  {@link StringUtils#isValidVariableName(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestIsValidVariableName.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( {"MisorderedAssertEqualsArguments", "removal"} )
@ClassVersion( sourceVersion = "$Id: TestIsValidVariableName.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestIsValidVariableName" )
public class TestIsValidVariableName extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#isValidVariableName(CharSequence)}
     *
     *  @param  data    The data for the test.
     */
    @ParameterizedTest
    @MethodSource( "org.tquadrat.foundation.util.helper.VariableNameProvider#provideVariableNames" )
    final void testIsValidVariableName( final VariableNameProvider.Result data )
    {
        skipThreadTest();

        final var result = data.valid();
        final var name = data.variableName();

        assertEquals( result, StringUtils.isValidVariableName( name ), format( "Name: %s", name ) );
    }   //  testIsValidVariableName()

    /**
     *  Tests for
     *  {@link StringUtils#isValidVariableName(CharSequence)}.
     */
    @Test
    final void testIsValidVariableNameWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            StringUtils.isValidVariableName( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testIsValidVariableNameWithNullArgument()
}
//  class TestIsValidVariableName

/*
 *  End of File
 */
