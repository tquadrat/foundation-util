/*
 * ============================================================================
 *  Copyright © 2002-2024 by Thomas Thrien.
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.substringsBetween;

import java.util.List;
import java.util.SequencedCollection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Tests for
 *  {@link StringUtils#substringsBetween(String,String,String)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestSubstringsBetween" )
public class TestSubstringsBetween extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#substringsBetween(String,String,String)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testSubstringsBetween() throws Exception
    {
        skipThreadTest();

        String input, open, close;
        SequencedCollection<String> result, expected;

        //---* The tests for the samples from the documentation *--------------
        /*
         *  substringsBetween( "[a][b][c]", "[", "]" ) = ["a","b","c"]
         *  substringsBetween( null, *, * )            = []
         *  substringsBetween( *, null, * )            = []
         *  substringsBetween( *, *, null )            = []
         *  substringsBetween( "", "[", "]" )          = []
         */
        input = "[a][b][c]";
        open = "[";
        close = "]";
        expected = List.of( "a","b","c" );
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertFalse( result.isEmpty() );
        assertEquals( expected, result );

        input = null;
        open = "[";
        close = "]";
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = null;
        open = null;
        close = "]";
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = null;
        open = "[";
        close = null;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = null;
        open = null;
        close = null;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = "[a][b][c]";
        open = null;
        close = "]";
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = "[a][b][c]";
        open = null;
        close = null;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = "[a][b][c]";
        open = "[";
        close = null;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = EMPTY_STRING;
        open = "[";
        close = "]";
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        //---* Other corner cases *--------------------------------------------
        input = EMPTY_STRING;
        open = EMPTY_STRING;
        close = "]";
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = EMPTY_STRING;
        open = "]";
        close = EMPTY_STRING;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = EMPTY_STRING;
        open = EMPTY_STRING;
        close = EMPTY_STRING;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = "[a][b][c]";
        open = EMPTY_STRING;
        close = "]";
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = "[a][b][c]";
        open = "]";
        close = EMPTY_STRING;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        input = "[a][b][c]";
        open = EMPTY_STRING;
        close = EMPTY_STRING;
        expected = List.of();
        result = substringsBetween( input, open, close );
        assertNotNull( result );
        assertTrue( result.isEmpty() );
    }   //  testSubstringsBetween()
}
//  class TestSubstringsBetween

/*
 *  End of File
 */