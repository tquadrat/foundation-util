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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.substringBetween;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Tests for
 *  {@link StringUtils#substringBetween(String,String,String)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "SpellCheckingInspection" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestSubstringBetween" )
public class TestSubstringBetween extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for
     *  {@link StringUtils#substringBetween(String,String,String)}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void testSubstringBetween() throws Exception
    {
        skipThreadTest();

        String input, open, close, expected;
        Optional<String> result;

        //---* The tests for the samples from the documentation *--------------
        /*
         *  substringBetween( "wx[b]yz", "[", "]" )    = "b"
         *  substringBetween( null, *, * )             = Optional.empty()
         *  substringBetween( *, null, * )             = Optional.empty()
         *  substringBetween( *, *, null )             = Optional.empty()
         *  substringBetween( "", "", "" )             = ""
         *  substringBetween( "", "", "]" )            = Optional.empty()
         *  substringBetween( "", "[", "]" )           = Optional.empty()
         *  substringBetween( "yabcz", "", "" )        = ""
         *  substringBetween( "yabcz", "y", "z" )      = "abc"
         *  substringBetween( "yabczyabcz", "y", "z" ) = "abc"
         */

        input = "wx[b]yz";
        open = "[";
        close = "]";
        expected = "b";
        result = substringBetween( input, open, close );
        assertTrue( result.isPresent() );
        assertEquals( expected, result.get() );

        input = null;
        open = null;
        close = null;
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = null;
        open = null;
        close = "]";
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = null;
        open = "[";
        close = null;
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = null;
        open = "[";
        close = "]";
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = "wx[b]yz";
        open = null;
        close = "]";
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = "wx[b]yz";
        open = "[";
        close = null;
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = "wx[b]yz";
        open = "[";
        close = null;
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = EMPTY_STRING;
        open = EMPTY_STRING;
        close = EMPTY_STRING;
        expected = EMPTY_STRING;
        result = substringBetween( input, open, close );
        assertTrue( result.isPresent() );
        assertEquals( expected, result.get() );

        input = EMPTY_STRING;
        open = EMPTY_STRING;
        close = "]";
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = EMPTY_STRING;
        open = "[";
        close = EMPTY_STRING;
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = EMPTY_STRING;
        open = "[";
        close = "]";
        result = substringBetween( input, open, close );
        assertTrue( result.isEmpty() );

        input = "yabcz";
        open = EMPTY_STRING;
        close = EMPTY_STRING;
        expected = EMPTY_STRING;
        result = substringBetween( input, open, close );
        assertTrue( result.isPresent() );
        assertEquals( expected, result.get() );

        input = "yabcz";
        open = "y";
        close = "z";
        expected = "abc";
        result = substringBetween( input, open, close );
        assertTrue( result.isPresent() );
        assertEquals( expected, result.get() );

        input = "yabczyabcz";
        open = "y";
        close = "z";
        expected = "abc";
        result = substringBetween( input, open, close );
        assertTrue( result.isPresent() );
        assertEquals( expected, result.get() );
    }   //  testSubstringBetween()
}
//  class TestSubstringBetween

/*
 *  End of File
 */