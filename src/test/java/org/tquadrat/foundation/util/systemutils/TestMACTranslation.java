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

import static java.lang.String.format;
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.SystemUtils.formatNodeIdAsMAC;
import static org.tquadrat.foundation.util.SystemUtils.getMACAddress;
import static org.tquadrat.foundation.util.SystemUtils.getNodeId;
import static org.tquadrat.foundation.util.SystemUtils.translateMACToNodeId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SystemUtils;

/**
 *  Some tests for the methods
 *  {@link SystemUtils#formatNodeIdAsMAC(long)}
 *  and
 *  {@link SystemUtils#translateMACToNodeId(String)}
 *  from the class
 *  {@link SystemUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestMACTranslation.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestMACTranslation.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.systemutils.TestMACTranslation" )
public class TestMACTranslation extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link SystemUtils#formatNodeIdAsMAC(long)}.
     */
    @Test
    final void testFormatNodeIdAsMACWithInvalidArgument()
    {
        skipThreadTest();

        final Class<? extends Exception> expectedException = ValidationException.class;
        try
        {
            formatNodeIdAsMAC( 0xFFFFFFFFFFFFL + 1 );  // Too long
            fail( format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testFormatNodeIdAsMACWithInvalidArgument()

    /**
     *  Tests the translation of a MAC address back and forth.
     *
     *  @param  macAddress    The MAC address for the test.
     *  @param  nodeId   The node id for the test.
     *
     *  @see SystemUtils#formatNodeIdAsMAC(long)
     *  @see SystemUtils#translateMACToNodeId(String)
     */
    @ParameterizedTest
    @CsvFileSource( resources = "MACTranslation.csv", numLinesToSkip = 1, delimiter = ';' )
    final void testMACTranslation( final String macAddress, final long nodeId )
    {
        skipThreadTest();

        final var candidate = translateEscapes( macAddress );

        final var currentNodeId = translateMACToNodeId( candidate );
        assertEquals( nodeId, currentNodeId );
        final var currentMACAddress = formatNodeIdAsMAC( currentNodeId );
        assertNotNull( currentMACAddress );
        assertEquals( candidate.toUpperCase( ROOT ), currentMACAddress );
    }   //  testMACTranslation()

    /**
     *  Tests the translation of a MAC address back and forth.
     *
     *  @see SystemUtils#formatNodeIdAsMAC(long)
     *  @see SystemUtils#translateMACToNodeId(String)
     */
    @Test
    final void testMACTranslation()
    {
        skipThreadTest();

        final var currentNodeId = getNodeId();
        final var currentMACAddress = getMACAddress();
        assertEquals( formatNodeIdAsMAC( currentNodeId ), currentMACAddress );
        assertEquals( translateMACToNodeId( currentMACAddress ), currentNodeId );
    }   //  testMACTranslation()

    /**
     *  Tests for the method
     *  {@link SystemUtils#translateMACToNodeId(String)}.
     */
    @Test
    final void testTranslateMACToNodeIdWithEmptyArgument()
    {
        skipThreadTest();

        assertThrows( EmptyArgumentException.class, () -> translateMACToNodeId( EMPTY_STRING ) );
    }   //  testTranslateMACToNodeIdWithEmptyArgument()

    /**
     *  Tests for the method
     *  {@link SystemUtils#translateMACToNodeId(String)}.
     */
    @Test
    final void testTranslateMACToNodeIdWithInvalidArgument()
    {
        skipThreadTest();

        //---* "MAC Address" is too long *-------------------------------------
        assertThrows( ValidationException.class, () -> translateMACToNodeId( "12-34-56-78-9A-BC-DE" ) );

        //---* "MAC Address" is too short *------------------------------------
        assertThrows( ValidationException.class, () -> translateMACToNodeId( "12-34-56-78-9A" ) );

        //---* "MAC Address" is too short *------------------------------------
        assertThrows( NumberFormatException.class, () -> translateMACToNodeId( "12-34-56-78-9A-XY" ) );

        //---* "MAC Address" is not even half way correct *--------------------
        assertThrows( NumberFormatException.class, () -> translateMACToNodeId( "MAC Address" ) );

        //---* MAC Address with wrong separators *-----------------------------
        assertThrows( IllegalArgumentException.class, () -> translateMACToNodeId( "123456789ABC" ) );
    }   //  testTranslateMACToNodeIdWithInvalidArgument()

    /**
     *  Tests for the method
     *  {@link SystemUtils#translateMACToNodeId(String)}.
     */
    @Test
    final void testTranslateMACToNodeIdWithNullArgument()
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> translateMACToNodeId( null ) );
    }   //  testTranslateMACToNodeIdWithNullArgument()
}
//  class TestMACTranslation

/*
 *  End of File
 */