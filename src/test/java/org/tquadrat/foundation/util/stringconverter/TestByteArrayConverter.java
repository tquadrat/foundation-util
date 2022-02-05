/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
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

import static java.nio.charset.StandardCharsets.UTF_16;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Objects;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the class
 *  {@link ByteArrayStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestByteArrayConverter.java 1007 2022-02-05 01:03:43Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestByteArrayConverter.java 1007 2022-02-05 01:03:43Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestByteArrayConverter" )
public class TestByteArrayConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link ByteArrayStringConverter}.
     */
    @Test
    final void testByteArrayConversion()
    {
        skipThreadTest();

        final var candidate = ByteArrayStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );
    }   //  testByteArrayConversion()

    /**
     *  The tests for
     *  {@link ByteArrayStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversionByteArray( final byte [] value )
    {
        skipThreadTest();

        final var candidate = ByteArrayStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertArrayEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversionByteArray()

    /**
     *  Provides test values for
     *  {@link #testValueConversionByteArray(String)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<byte []> valueProvider() throws Exception
    {
        final var builder = Stream.<byte []>builder();
        builder.add( new byte [0] );
        TestStringConverter.valueProvider()
            .filter( Objects::nonNull )
            .map( s -> s.getBytes( UTF_16 ) )
            .forEach( builder::add );
        var array = new byte [] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
        builder.add( array );
        array = new byte [] { -1 };
        builder.add( array );

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestByteArrayConverter

/*
 *  End of File
 */