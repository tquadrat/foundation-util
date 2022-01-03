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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.HexUtils.convertFromHexCharArray;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.HexUtils;

/**
 *  This class provides the JUnit tests for the method
 *  {@link HexUtils#convertFromHexCharArray(char[])}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestConvertFromHexCharArray.java 883 2021-03-02 18:39:20Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.hexutils.TestConvertFromHexCharArray" )
public class TestConvertFromHexCharArray extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link HexUtils#convertFromHexCharArray(char[])}.
     */
    @Test
    public final void testConvertFromHexCharArray()
    {
        skipThreadTest();

        char [] input;
        byte [] expecteds;
        byte [] actuals;

        //---* Convert a String with just zeroes *-----------------------------
        input = "00000000".toCharArray();
        expecteds = new byte [] {0, 0, 0, 0};
        actuals = convertFromHexCharArray( input );
        assertArrayEquals( expecteds, actuals );

        //---* Convert a String with just FFs *--------------------------------
        input = "FFFFFFFF".toCharArray();
        expecteds = new byte [] {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        actuals = convertFromHexCharArray( input );
        assertArrayEquals( expecteds, actuals );

        //---* Convert a String with other values *----------------------------
        input = "123456789ABCDEF0".toCharArray();
        expecteds = new byte [] {(byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0 };
        actuals = convertFromHexCharArray( input );
        assertArrayEquals( expecteds, actuals );

        //---* Convert a String with an odd number of digits *-----------------
        input = "12345".toCharArray();
        expecteds = new byte [] {(byte)0x01, (byte)0x23, (byte)0x45 };
        actuals = convertFromHexCharArray( input );
        assertArrayEquals( expecteds, actuals );

        //---* Try to convert a String with an illegal character *-------------
        input = "1234R5ABCD".toCharArray();
        try
        {
            convertFromHexCharArray( input );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = IllegalArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        //---* Try to convert the empty String *-------------------------------
        input = new char [0];
        try
        {
            convertFromHexCharArray( input );
            fail( "Expected Exception was not thrown" );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final Class<? extends Exception> expectedException = EmptyArgumentException.class;
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), t.getClass().getName() ) );
        }

        //---* Try to convert null *-------------------------------------------
        input = null;
        try
        {
            convertFromHexCharArray( input );
            fail( "Expected Exception was not thrown" );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final Class<? extends Exception> expectedException = NullArgumentException.class;
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConvertFromHexString()
}
//  class TestConvertFromHexCharArray

/*
 *  End of File
 */