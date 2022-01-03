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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.StringUtils.isEmpty;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#isEmpty(CharSequence)}
 *  {@link StringUtils#isNotEmpty(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestIsEmpty.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestIsEmpty.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestIsEmpty" )
public class TestIsEmpty extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the method
     *  {@link StringUtils#isEmpty(CharSequence)}.
     *
     *  @param  result  {@code true} if the candidate is empty, {@code false}
     *      otherwise.
     *  @param  candidate   The String to test.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "IsEmpty.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testIsEmpty( final boolean result, final String candidate )
    {
        skipThreadTest();

        assertEquals( result, isEmpty( translateEscapes( candidate ) ) );
    }   //  testIsEmpty()

    /**
     *  Test for the method
     *  {@link StringUtils#isEmpty(CharSequence)}.
     */
    @Test
    final void testIsEmpty()
    {
        skipThreadTest();

        //---* The test with some whitespace *---------------------------------
        assertFalse( isEmpty( "\n" ) );

        //---* The test with the empty char sequence *-------------------------
        assertTrue( isEmpty( EMPTY_CHARSEQUENCE ) );

        //---* The test with an empty StringBuilder *--------------------------
        assertTrue( isEmpty( new StringBuilder() ) );

        //---* The test with an empty StringBuffer *---------------------------
        assertTrue( isEmpty( new StringBuffer() ) );
    }   //  testIsEmpty()

    /**
     *  Test for the method
     *  {@link StringUtils#isNotEmpty(CharSequence)}.
     *
     *  @param  result  {@code true} if the candidate is empty, {@code false}
     *      otherwise.
     *  @param  candidate   The String to test.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "IsEmpty.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testIsNotEmpty( final boolean result, final String candidate )
    {
        skipThreadTest();

        assertEquals( !result, isNotEmpty( translateEscapes( candidate ) ) );
    }   //  testIsNotEmpty()

    /**
     *  Test for the method
     *  {@link StringUtils#isEmpty(CharSequence)}.
     */
    @Test
    final void testIsNotEmpty()
    {
        skipThreadTest();

        //---* The test with some whitespace *---------------------------------
        assertTrue( isNotEmpty( "\n" ) );

        //---* The test with the empty char sequence *-------------------------
        assertFalse( isNotEmpty( EMPTY_CHARSEQUENCE ) );

        //---* The test with an empty StringBuilder *--------------------------
        assertFalse( isNotEmpty( new StringBuilder() ) );

        //---* The test with an empty StringBuffer *---------------------------
        assertFalse( isNotEmpty( new StringBuffer() ) );
    }   //  testIsNotEmpty()
}
//  class TestIsEmpty

/*
 *  End of File
 */