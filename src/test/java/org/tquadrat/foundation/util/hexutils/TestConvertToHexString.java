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
import static org.tquadrat.foundation.util.HexUtils.convertToHexString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.HexUtils;

/**
 *  This class provides the JUnit tests for the method
 *  {@link HexUtils#convertToHexString(byte[])}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "removal" )
@ClassVersion( sourceVersion = "$Id: TestConvertToHexString.java 1021 2022-03-01 22:53:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.hexutils.TestConvertToHexString" )
public class TestConvertToHexString extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link HexUtils#convertToHexString(byte[])}.
     */
    @Test
    public final void testConvertToHexString()
    {
        skipThreadTest();

        byte [] input;
        String expected;
        String actual;

        //---* Convert an Array with just zeroes *-----------------------------
        input = new byte [] {0, 0, 0, 0};
        expected = "00000000";
        actual = convertToHexString( input );
        assertEquals( actual, expected );

        //---* Convert an Array with just 255s *-------------------------------
        input = new byte [] {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        expected = "FFFFFFFF";
        actual = convertToHexString( input );
        assertEquals( actual, expected );

        //---* Convert an Array with other values *----------------------------
        input = new byte [] {(byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0 };
        expected = "123456789ABCDEF0";
        actual = convertToHexString( input );
        assertEquals( actual, expected );

        //---* Try to convert the empty Array *--------------------------------
        input = new byte [0];
        try
        {
            convertToHexString( input );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = EmptyArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        //---* Try to convert null *-------------------------------------------
        input = null;
        try
        {
            convertToHexString( input );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = NullArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }
    }   //  testConvertToHexStringByteArray()
}
//  class TestHexUtils

/*
 *  End of File
 */