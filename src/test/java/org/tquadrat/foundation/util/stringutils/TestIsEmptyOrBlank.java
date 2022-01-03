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

import static java.lang.Character.MAX_CODE_POINT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#isEmptyOrBlank(CharSequence)}
 *  and
 *  {@link StringUtils#isNotEmptyOrBlank(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestIsEmptyOrBlank.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestIsEmptyOrBlank.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestIsEmptyOrBlank" )
public class TestIsEmptyOrBlank extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides a
     *  {@link Stream}
     *  of Strings that contain only whitespace characters.
     *
     *  @return A {@code Stream} of whitespace String.
     */
    static final Stream<String> provideWhitespaceStrings()
    {
        final Builder<String> builder = Stream.builder();
        BuildLoop: for( var i = 0; i < MAX_CODE_POINT; ++i )
        {
            if( !Character.isValidCodePoint( i ) ) continue BuildLoop;
            if( !Character.isWhitespace( i ) ) continue BuildLoop;
            builder.add( Character.toString( i ) );
        }   //  BuildLoop:

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  provideWhitespaceStrings()

    /**
     *  Test for the method
     *  {@link StringUtils#isEmptyOrBlank(CharSequence)}.
     *
     *  @param  result  {@code true} if the candidate is empty or blank,
     *      {@code false} otherwise.
     *  @param  candidate   The String to test.
     */
    @SuppressWarnings( "SimplifiableAssertion" )
    @ParameterizedTest
    @CsvFileSource( resources = "IsEmptyOrBlank.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testIsEmptyOrBlank( final boolean result, final String candidate )
    {
        skipThreadTest();

        /*
         * Cannot be simplified because assertEquals() does not work properly
         * for boolean values.
         */
        assertTrue( result == isEmptyOrBlank( translateEscapes( candidate ) ) );
    }   //  testIsEmptyOrBlank()

    /**
     *  Test for the method
     *  {@link StringUtils#isEmptyOrBlank(CharSequence)}.
     *
     *  @param  candidate   The String to test; it will contain only
     *      whitespace.
     */
    @ParameterizedTest
    @MethodSource( "provideWhitespaceStrings" )
    final void testIsEmptyOrBlank( final String candidate )
    {
        skipThreadTest();

        assertTrue( isEmptyOrBlank( candidate ) );
    }   //  testIsEmptyOrBlank()

    /**
     *  Test for the method
     *  {@link StringUtils#isEmptyOrBlank(CharSequence)}.
     */
    @DisplayName( "StringUtils.isOrBlankEmpty()" )
    @Test
    final void testIsEmptyOrBlank()
    {
        skipThreadTest();

        //---* The test with the empty char sequence *-------------------------
        assertTrue( isEmptyOrBlank( EMPTY_CHARSEQUENCE ) );

        //---* The test with an empty StringBuilder *--------------------------
        assertTrue( isEmptyOrBlank( new StringBuilder() ) );

        //---* The test with an empty StringBuffer *---------------------------
        assertTrue( isEmptyOrBlank( new StringBuffer() ) );
    }   //  testIsEmptyOrBlank()

    /**
     *  Test for the method
     *  {@link StringUtils#isNotEmptyOrBlank(CharSequence)}.
     *
     *  @param  result  {@code true} if the candidate is empty or blank,
     *      {@code false} otherwise.
     *  @param  candidate   The String to test.
     */
    @SuppressWarnings( "SimplifiableAssertion" )
    @ParameterizedTest
    @CsvFileSource( resources = "IsEmptyOrBlank.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testIsNotEmptyOrBlank( final boolean result, final String candidate )
    {
        skipThreadTest();

        /*
         * Cannot be simplified because assertEquals() does not work properly
         * for boolean values.
         */
        assertTrue( !result == isNotEmptyOrBlank( translateEscapes( candidate ) ) );
    }   //  testIsNotEmptyOrBlank()

    /**
     *  Test for the method
     *  {@link StringUtils#isNotEmptyOrBlank(CharSequence)}.
     *
     *  @param  candidate   The String to test; it will contain only
     *      whitespace.
     */
    @ParameterizedTest
    @MethodSource( "provideWhitespaceStrings" )
    final void testIsNotEmptyOrBlank( final String candidate )
    {
        skipThreadTest();

        assertFalse( isNotEmptyOrBlank( candidate ) );
    }   //  testIsNotEmptyOrBlank()

    /**
     *  Test for the method
     *  {@link StringUtils#isNotEmptyOrBlank(CharSequence)}.
     */
    @Test
    final void testIsNotEmptyOrBlank()
    {
        skipThreadTest();

        //---* The test with the empty char sequence *-------------------------
        assertFalse( isNotEmptyOrBlank( EMPTY_CHARSEQUENCE ) );

        //---* The test with an empty StringBuilder *--------------------------
        assertFalse( isNotEmptyOrBlank( new StringBuilder() ) );

        //---* The test with an empty StringBuffer *---------------------------
        assertFalse( isNotEmptyOrBlank( new StringBuffer() ) );
    }   //  testIsNotEmptyOrBlank()
}
//  class TestIsEmptyOrBlank

/*
 *  End of File
 */