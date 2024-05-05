/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

package org.tquadrat.foundation.util.stringconverter;

import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.time.Month;
import java.time.MonthDay;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the class
 *  {@link MonthDayStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestYearMonthConverter" )
public class TestMonthDayConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link MonthDayStringConverter}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void testYearMonthConversion() throws Exception
    {
        skipThreadTest();

        final var candidate = MonthDayStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( EMPTY_STRING ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( " " ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( "Fußpilz" ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( "--13-12" ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( "--12-32" ) );
    }   //  testYearMonthConversion()

    /**
     *  The tests for
     *  {@link MonthDayStringConverter}.
     *
     *  @param  value   The value for the tests.
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final MonthDay value ) throws Exception
    {
        skipThreadTest();

        final var container = StringConverter.forClass( MonthDay.class );
        assertNotNull( container );
        assertTrue( container.isPresent() );
        final var candidate = container.get();
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(MonthDay)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<MonthDay> valueProvider() throws Exception
    {
        final Set<Month> monthWith31Days = EnumSet.of( JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER );
        final var builder = Stream.<MonthDay>builder();
        MonthLoop: for( final var month : Month.values() )
        {
            for( var day = 1; day < 32; ++day )
            {
                if( (day > 29) && (month == FEBRUARY ) ) continue MonthLoop;
                if( (day > 30) && !monthWith31Days.contains( month ) ) continue MonthLoop;
                builder.accept( MonthDay.of( month, day ) );
            }
        }   //  MonthLoop:

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestYearMonthConverter

/*
 *  End of File
 */