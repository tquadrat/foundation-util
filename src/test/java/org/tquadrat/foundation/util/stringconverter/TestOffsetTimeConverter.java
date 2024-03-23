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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
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
 *  {@link OffsetTimeStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestOffsetTimeConverter.java 1125 2024-03-22 15:34:01Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestOffsetTimeConverter.java 1125 2024-03-22 15:34:01Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestOffsetTimeConverter" )
public class TestOffsetTimeConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link OffsetTimeStringConverter}.
     */
    @Test
    final void testOffsetTimeConversion()
    {
        skipThreadTest();

        final var candidate = OffsetTimeStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( EMPTY_STRING ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( " " ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( "Fußpilz" ) );
    }   //  testOffsetdTimeConversion()

    /**
     *  The tests for
     *  {@link OffsetTimeStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final OffsetTime value )
    {
        skipThreadTest();

        final var container = StringConverter.forClass( OffsetTime.class );
        assertNotNull( container );
        assertTrue( container.isPresent() );
        final var candidate = container.get();
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(OffsetDateTime)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<OffsetTime> valueProvider() throws Exception
    {
        final var offset = ZoneOffset.ofHours( 2 );
        final var retValue = Stream.of
            (
                null,
                OffsetTime.now(),
                OffsetTime.of( LocalTime.of( 17, 22, 0 ), offset )
            );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestOffsetTimeConverter

/*
 *  End of File
 */