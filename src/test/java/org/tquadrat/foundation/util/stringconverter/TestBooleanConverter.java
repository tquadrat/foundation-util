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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.testutil.TestBaseClass;

import java.util.stream.Stream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

/**
 *  Tests for the class
 *  {@link org.tquadrat.foundation.util.stringconverter.BooleanStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestBooleanConverter.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestBooleanConverter.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestBooleanConverter" )
public class TestBooleanConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link BooleanStringConverter}.
     */
    @Test
    final void testBooleanConversion()
    {
        skipThreadTest();

        final var candidate = BooleanStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        assertFalse( candidate.fromString( EMPTY_CHARSEQUENCE ) );
        assertFalse( candidate.fromString( EMPTY_STRING ) );
        assertFalse( candidate.fromString( " " ) );
        assertFalse( candidate.fromString( "Fußpilz" ) );
        assertFalse( candidate.fromString( "0" ) );
        assertFalse( candidate.fromString( "1" ) );
        assertFalse( candidate.fromString( "yes" ) );

        assertTrue( candidate.fromString( "true" ) );
        assertTrue( candidate.fromString( "True" ) );
        assertTrue( candidate.fromString( "TRUE" ) );
    }   //  testBooleanConversion()

    /**
     *  The tests for
     *  {@link BooleanStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final Boolean value )
    {
        skipThreadTest();

        final var container = StringConverter.forClass( Boolean.class );
        assertNotNull( container );
        assertTrue( container.isPresent() );
        final var candidate = container.get();
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(Boolean)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<Boolean> valueProvider() throws Exception
    {
        final var retValue = Stream.of(
            null,
            TRUE,
            FALSE
        );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestStringConverter

/*
 *  End of File
 */