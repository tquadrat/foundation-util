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

import java.time.Duration;
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
 *  {@link org.tquadrat.foundation.util.stringconverter.DurationStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDurationConverter.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestDurationConverter.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestDurationConverter" )
public class TestDurationConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link DurationStringConverter}.
     */
    @Test
    final void testDurationConversion()
    {
        skipThreadTest();

        final var candidate = DurationStringConverter.INSTANCE;
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
    }   //  testDurationConversion()

    /**
     *  The tests for
     *  {@link DurationStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final Duration value )
    {
        skipThreadTest();

        final var container = StringConverter.forClass( Duration.class );
        assertNotNull( container );
        assertTrue( container.isPresent() );
        final var candidate = container.get();
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(Duration)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<Duration> valueProvider() throws Exception
    {
        /*
         * The pattern is: PnDTnHnMn.nS
         */
        final var retValue = Stream.of(
            null,
            "P1D", "p1d", "p1D", "P1d",
            "P23D", "p23d", "p23D", "P23d",
            "PT1H", "pt1h", "pt1H", "PT1h",
            "PT23H", "pT23h", "pt23H", "Pt23h",
            "PT35H", "pt35h", "pt35H", "PT35h",
            "P21DT29H84M104.32S", "P21DT29H84M104.32S"
        ).map( s -> isNull( s ) ? null : Duration.parse( s ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestDurationConverter

/*
 *  End of File
 */