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
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.quote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#quote(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestQuote.java 1060 2023-09-24 19:21:40Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestQuote.java 1060 2023-09-24 19:21:40Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestQuote" )
public class TestQuote extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#quote(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testQuote( ) throws Exception
    {
        skipThreadTest();

        String expected;
        String input;
        String result;

        input = null;
        expected = null;
        result = quote( input );
        assertEquals( expected, result );

        input = EMPTY_STRING;
        expected = "\"\"";
        result = quote( input );
        assertEquals( expected, result );

        input = " ";
        expected = "\" \"";
        result = quote( input );
        assertEquals( expected, result );

        input = "TestValue";
        expected = "\"TestValue\"";
        result = quote( input );
        assertEquals( expected, result );
    }   //  testQuote()
}
//  class TestPad

/*
 *  End of File
 */