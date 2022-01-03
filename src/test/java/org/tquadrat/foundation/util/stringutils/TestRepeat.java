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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.repeat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#repeat(char, int)},
 *  {@link StringUtils#repeat(int, int)}
 *  and
 *  {@link StringUtils#repeat(CharSequence, int)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestRepeat.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestRepeat.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestRepeat" )
public class TestRepeat extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#repeat(CharSequence, int)},
     *  {@link StringUtils#repeat(char, int)},
     *  and
     *  {@link StringUtils#repeat(int, int)}.
     */
    @Test
    final void testRepeat()
    {
        skipThreadTest();

        assertNull( repeat( null, 10 ) );

        assertEquals( EMPTY_STRING, repeat( '-', -3 ) );
        assertEquals( EMPTY_STRING, repeat( (int) '-', -3 ) );
        assertEquals( EMPTY_STRING, repeat( "-", -3 ) );

        var expected = "-".repeat( 10 );
        assertEquals( expected, repeat( expected.charAt( 0 ), 10 ) );
        assertEquals( expected, repeat( expected.codePointAt( 0 ), 10 ) );
        assertEquals( expected, repeat( expected.substring( 0, 1 ), 10 ) );

        var codePoint = 0x1F6C6;
        expected = "🛆🛆🛆🛆🛆🛆🛆🛆🛆🛆";
        final var actual = repeat( codePoint, 10 );
        assertEquals( expected, actual );

        codePoint = 0xFFFFFF;
        assertNull( repeat( codePoint, 10 ) );
    }   //  testRepeat()
}
//  class TestRepeat

/*
 *  End of File
 */