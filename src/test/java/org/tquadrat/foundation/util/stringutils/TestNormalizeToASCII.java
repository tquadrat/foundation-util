/*
 * ============================================================================
 *  Copyright © 2002-2022 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util.stringutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.util.StringUtils.normalizeToASCII;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for
 *  {@link org.tquadrat.foundation.util.StringUtils#normalizeToASCII(CharSequence)}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "SpellCheckingInspection" )
@ClassVersion( sourceVersion = "$Id: TestNormalizeToASCII.java 1021 2022-03-01 22:53:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestNormalizeToASCII" )
public class TestNormalizeToASCII extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for
     *  {@link org.tquadrat.foundation.util.StringUtils#normalizeToASCII(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testNormalizeToASCII_with_NullArgument() throws Exception
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> normalizeToASCII( null ) );
    }   //  testNormalizeToASCII_with_NullArgument()

    /**
     *  Some tests for
     *  {@link org.tquadrat.foundation.util.StringUtils#normalizeToASCII(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     *
     *  @param  candidate   The input String.
     *  @param  expected    The expected result.
     */
    @ParameterizedTest
    @CsvSource( delimiter = ';', quoteCharacter = '"', textBlock =
        """
        # candidate; expected
        "";""
        ß;ss
        Schön;Schon
        Haß;Hass
        mšk žilina;msk zilina
        Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ;This is a funky String
        Übung;Ubung
        Rübe;Rube
        Garçon;Garcon
        Ålborg;Alborg
        Øresund;Oresund
        Smørrebrød;Smorrebrod
        """ )
    final void testNormalizeToASCII( final CharSequence candidate, final String expected ) throws Exception
    {
        skipThreadTest();

        final var actual = normalizeToASCII( candidate );
        assertEquals( expected, actual );
    }   //  testNormalizeToASCII()
}
//  class TestNormalizeToASCII

/*
 *  End of File
 */