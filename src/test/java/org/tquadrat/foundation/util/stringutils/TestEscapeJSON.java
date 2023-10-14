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

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.escapeJSON;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#escapeJSON(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestEscapeJSON.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestEscapeJSON.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestEscapeJSON" )
public class TestEscapeJSON extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#escapeJSON(CharSequence)}.
     *
     *  @param  input   The input value.
     *  @param  expected    The expected result.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "EscapeJSON.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testEscapeJSON( final String input, final String expected )
    {
        skipThreadTest();

        final var i = translateEscapes( input );
        final var actual = escapeJSON( i );
        assertNotNull( actual );
        assertEquals( format( "\"%s\"", translateEscapes( expected ) ), actual );
    }   //  testEscapeJSON()

    /**
     *  Tests for
     *  {@link StringUtils#escapeJSON(CharSequence)}.
     */
    @Test
    final void testEscapeJSONWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            escapeJSON( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testEscapeJSONWithNullArgument()
}
//  class TestEscapeJSON

/*
 *  End of File
 */