/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

package org.tquadrat.foundation.util.uniqueidutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.UniqueIdUtils.fromXMLId;
import static org.tquadrat.foundation.util.UniqueIdUtils.randomUUID;
import static org.tquadrat.foundation.util.UniqueIdUtils.toXMLId;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.BlankArgumentException;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.UniqueIdUtils;

/**
 *  This class tests the conversion of UUIDs to XML safe ids back and force as
 *  implemented in
 *  {@link UniqueIdUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestUUIDCreation.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.uniqueidutils.TestXMLIdCreation" )
public class TestXMLIdCreation extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Check the argument validation for
     *  {@link UniqueIdUtils#toXMLId(UUID)}
     *  and
     *  {@link UniqueIdUtils#fromXMLId(CharSequence)}.
     */
    @Test
    final void testArguments()
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> toXMLId( null ) );

        assertThrows( NullArgumentException.class, () -> fromXMLId( null ) );
        assertThrows( EmptyArgumentException.class, () -> fromXMLId( EMPTY_STRING ) );
        assertThrows( BlankArgumentException.class, () -> fromXMLId( " " ) );
        assertThrows( IllegalArgumentException.class, () -> fromXMLId( "ABC" ) ); // No hyphen
        assertThrows( IllegalArgumentException.class, () -> fromXMLId( "12-23-34" ) ); // Too many hyphens
        assertThrows( IllegalArgumentException.class, () -> fromXMLId( "1-2" ) ); // Invalid characters
        assertThrows( IllegalArgumentException.class, () -> fromXMLId( "I-I" ) ); // Invalid characters
        assertThrows( IllegalArgumentException.class, () -> fromXMLId( "O-O" ) ); // Invalid characters
        assertThrows( IllegalArgumentException.class, () -> fromXMLId( "Q-Q" ) ); // Invalid characters
    }   //  testArguments()

    /**
     *  Tests the conversion back and forth.
     */
    @Test
    final void testConversion()
    {
        skipThreadTest();

        for( var i = 0; i < 1; ++i )
        {
            final var uuid = randomUUID();
            final var xmlId = toXMLId( uuid );
            assertNotNull( xmlId );
            assertEquals( uuid, fromXMLId( xmlId ) );
        }
    }   //  testConversion()
}
//  class TestXMLIdCreation

/*
 *  End of File
 */