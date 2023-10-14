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

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the class
 *  {@link org.tquadrat.foundation.util.stringconverter.LongStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLongConverter.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestLongConverter.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestLongConverter" )
public class TestLongConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link IntegerStringConverter}.
     */
    @Test
    final void testLongConversion()
    {
        skipThreadTest();

        final var candidate = LongStringConverter.INSTANCE;
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
        try
        {
            candidate.fromString( " " );
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
        try
        {
            candidate.fromString( "Fußpilz" );
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

        assertEquals( candidate.fromString( "-0x01" ), Long.valueOf( -1L ) );
        assertEquals( candidate.fromString( "-013" ), Long.valueOf( -11L ) );
    }   //  testIntegerConversion()

    /**
     *  The tests for
     *  {@link LongStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final Long value )
    {
        skipThreadTest();

        final var candidate = LongStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
        if( !isNull( value ) )
        {
            if( value.longValue() >= 0 )
            {
                assertEquals( value, candidate.fromString( format( "0x%X", value ) ) );
                assertEquals( value, candidate.fromString( format( "0x%x", value ) ) );
                assertEquals( value, candidate.fromString( format( "0%o", value ) ) );
            }
        }
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(Long)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<Long> valueProvider() throws Exception
    {
        final var retValue = Stream.of(
            null,
            -513L,
            -255L,
            -129L,
            0L,
            -1L,
            1L,
            13L,
            42L,
            (long) Integer.MAX_VALUE,
            (long) Integer.MIN_VALUE,
            Long.MAX_VALUE,
            Long.MIN_VALUE
        );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestLongConverter

/*
 *  End of File
 */