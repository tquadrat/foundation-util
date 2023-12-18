/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util.stringconverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for
 *  {@link ListStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestListStringConverter.java 1079 2023-10-22 17:44:34Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestListStringConverter.java 1079 2023-10-22 17:44:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestListStringConverter" )
public class TestListStringConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the constructor for
     *  {@link ListStringConverter}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void testConstructor() throws Exception
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> new ListStringConverter<>( (StringStringConverter) null, ArrayList::new ) );
        assertThrows( NullArgumentException.class, () -> new ListStringConverter<>( StringStringConverter.INSTANCE, null ) );
    }   //  testConstructor()

    /**
     *  Some basic tests for
     *  {@link ListStringConverter#fromString(List)}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void testFromString1() throws Exception
    {
        skipThreadTest();

        final var candidate = new ListStringConverter<>( StringStringConverter.INSTANCE, ArrayList::new );

        assertNull( candidate.fromString( null ) );

        var input = EMPTY_STRING;
        var expected = List.<String>of();
        assertEquals( expected, candidate.fromString( input ) );

        input = "one";
        expected = new ArrayList<>( List.of( input ) );
        assertEquals( expected, candidate.fromString( input ) );

        input = "[]";
        expected = List.of();
        assertEquals( expected, candidate.fromString( input ) );

        input = "[{{one}},{{two}},{{three}},{{four}}]";
        expected = new ArrayList<>( List.of( "one", "two", "three", "four" ) );
        assertEquals( expected, candidate.fromString( input ) );
    }   //  testFromString1()

    /**
     *  Some basic tests for
     *  {@link ListStringConverter#toString(List)}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void testToString1() throws Exception
    {
        skipThreadTest();

        final var candidate = new ListStringConverter<>( StringStringConverter.INSTANCE, ArrayList::new );

        assertNull( candidate.toString( null ) );

        var input = List.<String>of();
        var expected = "[]";
        assertEquals( expected, candidate.toString( input ) );

        input = List.of( "one", "two", "three", "four" );
        expected = "[{{one}},{{two}},{{three}},{{four}}]";
        assertEquals( expected, candidate.toString( input ) );
    }   //  testToString1()
}
//  class TestListStringConverter

/*
 *  End of File
 */