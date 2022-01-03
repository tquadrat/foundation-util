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

package org.tquadrat.foundation.util.stringutils;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.abbreviate;
import static org.tquadrat.foundation.util.StringUtils.abbreviateMiddle;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#abbreviate(CharSequence, int)},
 *  {@link StringUtils#abbreviate(CharSequence, int, int)},
 *  and
 *  {@link StringUtils#abbreviateMiddle(CharSequence, int)},
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestAbbreviate.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestAbbreviate.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestAbbreviate" )
public class TestAbbreviate extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#abbreviate(CharSequence,int)}.
     *
     *  @param  result  {@code true} for a successful execution, {@code false}
     *      for a failure.
     *  @param  input   The input String.
     *  @param  maxWidth    The maximum target length of the output String.
     *  @param  expected    The expected result.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "Abbreviate1.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testAbbreviate1( final boolean result, final String input, final int maxWidth, final String expected )
    {
        skipThreadTest();

        if( result )
        {
            //---* The happy path *--------------------------------------------
            final var actual = abbreviate( input, maxWidth );
            assertNotNull( actual );
            assertEquals( expected, actual );
        }
        else
        {
            //---* The maxWidth argument is invalid *--------------------------
            final Class<? extends Throwable> expectedException = ValidationException.class;
            try
            {
                abbreviate( input, maxWidth );
                fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
            }
            catch( final AssertionError e ) { throw e; }
            catch( final Throwable t )
            {
                final var isExpectedException = expectedException.isInstance( t );
                if( !isExpectedException ) t.printStackTrace( out );
                assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
            }
        }
    }   //  testAbbreviate1()

    /**
     *  Tests for
     *  {@link StringUtils#abbreviate(CharSequence,int)}.
     *
     *  @param  repetitionInfo  The test repetition info.
     */
    @RepeatedTest( 7 )
    final void testAbbreviate1WithNullTextArgument( final RepetitionInfo repetitionInfo )
    {
        skipThreadTest();

        final var maxWidth = repetitionInfo.getCurrentRepetition() - 2;

        /*
         * If text is null, the method returns null, no matter what value is
         * given for maxWidth.
         */
        assertNull( abbreviate( null, maxWidth ) );
    }   //  testAbbreviate1WithNullTextArgument()

    /**
     *  Tests for
     *  {@link StringUtils#abbreviate(CharSequence,int,int)}.
     *
     *  @param  result  {@code true} for a successful execution, {@code false}
     *      for a failure.
     *  @param  input   The input String.
     *  @param  offset  The offset for the abbreviation.
     *  @param  maxWidth    The maximum target length of the output String.
     *  @param  expected    The expected result.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "Abbreviate2.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testAbbreviate2( final boolean result, final String input, final int offset, final int maxWidth, final String expected )
    {
        skipThreadTest();

        if( result )
        {
            //---* The happy path *--------------------------------------------
            final var actual = abbreviate( input, offset, maxWidth );
            assertNotNull( actual );
            assertEquals( expected, actual );
        }
        else
        {
            //---* The maxWidth argument is invalid *--------------------------
            final Class<? extends Throwable> expectedException = ValidationException.class;
            try
            {
                abbreviate( input, offset, maxWidth );
                fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
            }
            catch( final AssertionError e ) { throw e; }
            catch( final Throwable t )
            {
                final var isExpectedException = expectedException.isInstance( t );
                if( !isExpectedException ) t.printStackTrace( out );
                assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
            }
        }
    }   //  testAbbreviate2()

    /**
     *  Tests for
     *  {@link StringUtils#abbreviate(CharSequence,int,int)}.
     *
     *  @param  repetitionInfo  The test repetition info.
     */
    @RepeatedTest( 7 )
    final void testAbbreviate2WithNullTextArgument( final RepetitionInfo repetitionInfo )
    {
        skipThreadTest();

        final var maxWidth = repetitionInfo.getCurrentRepetition() - 2;

        /*
         * If text is null, the method returns null, no matter what value is
         * given for maxWidth.
         */
        assertNull( abbreviate( null, -maxWidth, maxWidth ) );
        assertNull( abbreviate( null, -1, maxWidth ) );
        assertNull( abbreviate( null, 0, maxWidth ) );
        assertNull( abbreviate( null, 1, maxWidth ) );
        assertNull( abbreviate( null, maxWidth, maxWidth ) );
        assertNull( abbreviate( null, maxWidth * 2, maxWidth ) );
    }   //  testAbbreviate2WithNullTextArgument()

    /**
     *  Tests for
     *  {@link StringUtils#abbreviateMiddle(CharSequence,int)}.
     *
     *  @param  result  {@code true} for a successful execution, {@code false}
     *      for a failure.
     *  @param  input   The input String.
     *  @param  maxWidth    The maximum target length of the output String.
     *  @param  expected    The expected result.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "AbbreviateMiddle.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testAbbreviateMiddle( final boolean result, final String input, final int maxWidth, final String expected )
    {
        skipThreadTest();

        if( result )
        {
            //---* The happy path *--------------------------------------------
            final var actual = abbreviateMiddle( input, maxWidth );
            assertNotNull( actual );
            assertEquals( expected, actual );
        }
        else
        {
            //---* The maxWidth argument is invalid *--------------------------
            final Class<? extends Throwable> expectedException = ValidationException.class;
            try
            {
                abbreviateMiddle( input, maxWidth );
                fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
            }
            catch( final AssertionError e ) { throw e; }
            catch( final Throwable t )
            {
                final var isExpectedException = expectedException.isInstance( t );
                if( !isExpectedException ) t.printStackTrace( out );
                assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
            }
        }
    }   //  testAbbreviateMiddleFail()

    /**
     *  Tests for
     *  {@link StringUtils#abbreviateMiddle(CharSequence,int)}.
     *
     *  @param  repetitionInfo  The test repetition info.
     */
    @DisplayName( "StringUtils.abbreviateMiddle(CharSequence,int) with null argument" )
    @RepeatedTest( 8 )
    final void testAbbreviateMiddleWithNullTextArgument( final RepetitionInfo repetitionInfo )
    {
        skipThreadTest();

        final var maxWidth = repetitionInfo.getCurrentRepetition() - 2;

        assertNull( abbreviateMiddle( null, maxWidth ) );
    }   //  testAbbreviateMiddleWithNullTextArgument()
}
//  class TestAbbreviate

/*
 *  End of File
 */