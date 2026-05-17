/*
 * ============================================================================
 *  Copyright © 2002-2026 by Thomas Thrien.
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

package org.tquadrat.foundation.util.template;

import static java.time.temporal.ChronoField.YEAR;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Formattable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.Template;
import org.tquadrat.foundation.util.stringconverter.LocalDateStringConverter;

/**
 *  <p>{@summary Some tests for
 *  {@link Template#replaceVariable(Map...)}.}</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestReplaceVariable.java 1239 2026-05-10 22:34:21Z tquadrat $
 *  @since 0.25.4
 */
@ClassVersion( sourceVersion = "$Id: TestReplaceVariable.java 1239 2026-05-10 22:34:21Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.template.TestReplaceVariable" )
public class TestReplaceVariable extends TestBaseClass
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for
     *  {@link Template#replaceVariable(Map[])}.
     *
     *  @throws Exception   Something went awfully wrong.
     */
    @Test
    final void testReplaceVariable() throws Exception
    {
        skipThreadTest();

        final var text = """
            FirstLine
            Replacement: ${variable}
            LastLine
            """;

        final Map<String,Object> source = new HashMap<>();
        final var candidate = assertDoesNotThrow( () -> new Template( text ) );
        assertInstanceOf( Template.class, candidate );
        assertThrows( NullArgumentException.class, () -> candidate.replaceVariable( (Map<String,? extends Object>[]) null ) );

        //noinspection RedundantOperationOnEmptyContainer
        source.clear();
        var actual = assertDoesNotThrow( () -> candidate.replaceVariable( source ) );
        assertEquals( text, actual );

        source.put( "variable", null );
        actual = assertDoesNotThrow( () -> candidate.replaceVariable( source ) );
        assertEquals( text, actual );

        source.put( "variable", "value" );
        var expected = """
            FirstLine
            Replacement: value
            LastLine
            """;
        actual = assertDoesNotThrow( () -> candidate.replaceVariable( source ) );
        assertEquals( expected, actual );

        source.put( "variable", 123456 );
        expected = """
            FirstLine
            Replacement: 123456
            LastLine
            """;
        actual = assertDoesNotThrow( () -> candidate.replaceVariable( source ) );
        assertEquals( expected, actual );

        final Formattable formattable = (formatter,flags,width,precision) -> formatter.format( "value-%d %d %d", flags, width, precision );
        source.put( "variable", formattable );
        expected = """
            FirstLine
            Replacement: value-0 -1 -1
            LastLine
            """;
        actual = assertDoesNotThrow( () -> candidate.replaceVariable( source ) );
        assertEquals( expected, actual );

        final var now = LocalDate.now();
        source.put( "variable", now );
        expected = """
            FirstLine
            Replacement: %s
            LastLine
            """.formatted( now.toString() );
        actual = assertDoesNotThrow( () -> candidate.replaceVariable( source ) );
        assertEquals( expected, actual );

        final var formatter = new DateTimeFormatterBuilder().appendValue( YEAR ).toFormatter();
        final StringConverter<LocalDate> stringConverter = new LocalDateStringConverter( formatter );
        candidate.registerStringConverter( LocalDate.class, stringConverter );
        source.put( "variable", now );
        expected = """
            FirstLine
            Replacement: %d
            LastLine
            """.formatted( now.getYear() );
        actual = assertDoesNotThrow( () -> candidate.replaceVariable( source ) );
        assertEquals( expected, actual );
    }   //  testReplaceVariable()
}
//  class TestReplaceVariable

/*
 *  End of File
 */