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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.capitalize;
import static org.tquadrat.foundation.util.StringUtils.decapitalize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#capitalize(CharSequence)}
 *  and
 *  {@link StringUtils#decapitalize(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestCapitalize.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestCapitalize.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestCapitalize" )
public class TestCapitalize extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#capitalize(CharSequence)}.
     *
     *  @param  input   The input String.
     *  @param  expected    The expected output.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "Capitalize.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testCapitalize( final String input, final String expected )
    {
        skipThreadTest();

        final var candidate = translateEscapes( input );
        final var actual = capitalize( candidate );
        if( nonNull( candidate ) ) assertNotNull( actual );
        assertEquals( translateEscapes( expected ), actual );
    }   //  testCapitalize()

    /**
     *  Tests for
     *  {@link StringUtils#capitalize(CharSequence)}.
     */
    @Test
    final void testCapitalizeWithNullArgument()
    {
        skipThreadTest();

        assertNull( capitalize( null ) );
    }   //  testCapitalizeWithNullArgument()

    /**
     *  Tests for
     *  {@link StringUtils#decapitalize(CharSequence)}.
     *
     *  @param  input   The input String.
     *  @param  expected    The expected output.
     */
    @SuppressWarnings( "deprecation" )
    @ParameterizedTest
    @CsvFileSource( resources = "Decapitalize.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testDecapitalize( final String input, final String expected )
    {
        skipThreadTest();

        final var candidate = translateEscapes( input );
        final var result = translateEscapes( expected );
        String actual;

        actual = decapitalize( candidate );
        if( nonNull( candidate ) ) assertNotNull( actual );
        assertEquals( result, actual );

        actual = StringUtils.uncapitalize( candidate );
        if( nonNull( candidate ) ) assertNotNull( actual );
        assertEquals( result, actual );
    }   //  testDecapitalize()

    /**
     *  Tests for
     *  {@link StringUtils#decapitalize(CharSequence)}.
     */
    @SuppressWarnings( "deprecation" )
    @Test
    final void testDecapitalizeWithNullArgument()
    {
        skipThreadTest();

        assertNull( decapitalize( null ) );

        assertNull( StringUtils.uncapitalize( null ) );
    }   //  testDecapitalizeWithNullArgument()
}
//  class TestCapitalize

/*
 *  End of File
 */