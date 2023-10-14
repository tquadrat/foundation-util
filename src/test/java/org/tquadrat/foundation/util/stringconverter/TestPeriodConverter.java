/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;

import java.time.Period;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the class
 *  {@link org.tquadrat.foundation.util.stringconverter.PeriodStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestPeriodConverter.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestPeriodConverter.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestPeriodConverter" )
public class TestPeriodConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link PeriodStringConverter}.
     */
    @Test
    final void testPeriodConversion()
    {
        skipThreadTest();

        final var candidate = PeriodStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
        try
        {
            candidate.fromString( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testPeriodConversion()

    /**
     *  The tests for
     *  {@link PeriodStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final Period value )
    {
        skipThreadTest();

        final var container = StringConverter.forClass( Period.class );
        assertNotNull( container );
        assertTrue( container.isPresent() );
        final var candidate = container.get();
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(Period)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<Period> valueProvider() throws Exception
    {
        /*
         * The patterns are: PnYnMnD and @code PnW
         */
        final var retValue = Stream.of(
            null,
            "P1W", "p1w", "p1W", "P1W",
            "P23D", "p23d", "p23D", "P23d",
            "P1Y", "p1y", "p1Y", "P1y",
            "P23M", "p23m", "p23M", "P23m",
            "P35M", "p35m", "p35M", "P35m",
            "P21Y29M84D"
        ).map( s -> isNull( s ) ? null : Period.parse( s ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestPeriodConverter

/*
 *  End of File
 */