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

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.util.StringUtils.format;

/**
 *  Tests for the class
 *  {@link org.tquadrat.foundation.util.stringconverter.PatternStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestPatternConverter.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestPatternConverter.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestPatternConverter" )
public class TestPatternConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link PatternStringConverter}.
     */
    @Test
    final void testPatternConversion()
    {
        skipThreadTest();

        final var candidate = PatternStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
        try
        {
            candidate.fromString( "**" );
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

        assertEquals( candidate.fromString( EMPTY_CHARSEQUENCE ).pattern(), compile( EMPTY_STRING ).pattern() );
        assertEquals( candidate.fromString( " " ).pattern(), compile( " " ).pattern() );
        assertEquals( candidate.fromString( "Fußpilz" ).pattern(), compile( "Fußpilz" ).pattern() );
    }   //  testPatternConversion()

    /**
     *  The tests for
     *  {@link PatternStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final Pattern value )
    {
        skipThreadTest();

        final var container = StringConverter.forClass( Pattern.class );
        assertNotNull( container );
        assertTrue( container.isPresent() );
        final var candidate = container.get();
        assertNotNull( candidate );

        if( isNull( value ) )
        {
            assertNull( candidate.toString( value ) );
            assertNull( candidate.fromString( candidate.toString( value ) ) );
        }
        else
        {
            assertEquals( value.pattern(), candidate.fromString( candidate.toString( value ) ).pattern() );
        }
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(Pattern)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<Pattern> valueProvider() throws Exception
    {
        final var retValue = Stream.of
            (
                null,
                compile( ".*" ),
                compile( ".?" ),
                compile( "(.)" )
            );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestPatternConverter

/*
 *  End of File
 */