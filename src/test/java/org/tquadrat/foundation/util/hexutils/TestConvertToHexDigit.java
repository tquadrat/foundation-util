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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.HexUtils;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.HexUtils.convertToHexDigit;

/**
 *  This class provides the JUnit tests for the method
 *  {@link HexUtils#convertToHexDigit(int)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestConvertToHexDigit.java 820 2020-12-29 20:34:22Z tquadrat $" )
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

        assertEquals( convertToHexDigit( 0 ), '0' );
        assertEquals( convertToHexDigit( 1 ), '1' );
        assertEquals( convertToHexDigit( 2 ), '2' );
        assertEquals( convertToHexDigit( 3 ), '3' );
        assertEquals( convertToHexDigit( 4 ), '4' );
        assertEquals( convertToHexDigit( 5 ), '5' );
        assertEquals( convertToHexDigit( 6 ), '6' );
        assertEquals( convertToHexDigit( 7 ), '7' );
        assertEquals( convertToHexDigit( 8 ), '8' );
        assertEquals( convertToHexDigit( 9 ), '9' );
        assertEquals( convertToHexDigit( 10 ), 'A' );
        assertEquals( convertToHexDigit( 11 ), 'B' );
        assertEquals( convertToHexDigit( 12 ), 'C' );
        assertEquals( convertToHexDigit( 13 ), 'D' );
        assertEquals( convertToHexDigit( 14 ), 'E' );
        assertEquals( convertToHexDigit( 15 ), 'F' );
    }   //  testConvertToHexDigit()
}
//  class TestConvertToHexDigit

/*
 *  End of File
 */