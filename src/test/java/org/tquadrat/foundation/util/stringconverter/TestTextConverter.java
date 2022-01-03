/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the class
 *  {@link org.tquadrat.foundation.util.stringconverter.TextStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestTextConverter.java 907 2021-05-05 23:09:17Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestTextConverter.java 907 2021-05-05 23:09:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestTextConverter" )
public class TestTextConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link TextStringConverter}.
     */
    @Test
    final void testTextConversion()
    {
        skipThreadTest();

        final var candidate = TextStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        String actual, expected, source;

        source = "\b\t\n\f\r";
        expected = "\\b\\t\\n\\f\\r";
        actual = candidate.toString( source );
        assertEquals( expected, actual );

        source = " ";
        expected = "\\s";
        actual = candidate.toString( source );
        assertEquals( expected, actual );

        source = "  Text  ";
        expected = "\\s Text \\s";
        actual = candidate.toString( source );
        assertEquals( expected, actual );
        actual = candidate.fromString( expected );
        assertEquals( source, actual );

        source = "\s\sText\s\s";
        expected = "\\s Text \\s";
        actual = candidate.toString( source );
        assertEquals( expected, actual );
        actual = candidate.fromString( expected );
        assertEquals( source, actual );
    }   //  testTextConversion()

    /**
     *  The tests for
     *  {@link TextStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final String value )
    {
        skipThreadTest();

        final var candidate = TextStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(String)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    @SuppressWarnings( "unused" ) // ?? Method source for testValueConversion
    static final Stream<String> valueProvider() throws Exception
    {
        final var retValue = Stream.of
            (
                null,
                EMPTY_STRING,
                " ",
                "Dies ist ein Test-String",
                "äÄöÖüÜßáÁàÀéÉèÈđ¢©ſẞ",
                "\b\t\n\f\r",
                "  Text  ",
                "\b\t\n\f\r",
                "\s  Text  \s",
                "\\"
            );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestTextConverter

/*
 *  End of File
 */