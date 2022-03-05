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

package org.tquadrat.foundation.util.hexutils;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.HexUtils.convertToHexDigit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.HexUtils;

/**
 *  This class provides the JUnit tests for the method
 *  {@link HexUtils#convertToHexDigit(int)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestConvertToHexDigit.java 1022 2022-03-03 23:03:40Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.hexutils.TestConvertToHexDigit" )
public class TestConvertToHexDigit extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the method
     *  {@link HexUtils#convertToHexDigit(int)}.
     */
    @Test
    public final void testConvertToHexDigit()
    {
        skipThreadTest();

        try
        {
            convertToHexDigit( -1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = ValidationException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        try
        {
            convertToHexDigit( 16 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = ValidationException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        assertEquals( '0', convertToHexDigit( 0 ) );
        assertEquals( '1', convertToHexDigit( 1 ) );
        assertEquals( '2', convertToHexDigit( 2 ) );
        assertEquals( '3', convertToHexDigit( 3 ) );
        assertEquals( '4', convertToHexDigit( 4 ) );
        assertEquals( '5', convertToHexDigit( 5 ) );
        assertEquals( '6', convertToHexDigit( 6 ) );
        assertEquals( '7', convertToHexDigit( 7 ) );
        assertEquals( '8', convertToHexDigit( 8 ) );
        assertEquals( '9', convertToHexDigit( 9 ) );
        assertEquals( 'A', convertToHexDigit( 10 ) );
        assertEquals( 'B', convertToHexDigit( 11 ) );
        assertEquals( 'C', convertToHexDigit( 12 ) );
        assertEquals( 'D', convertToHexDigit( 13 ) );
        assertEquals( 'E', convertToHexDigit( 14 ) );
        assertEquals( 'F', convertToHexDigit( 15 ) );
    }   //  testConvertToHexDigit()
}
//  class TestConvertToHexDigit

/*
 *  End of File
 */