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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.stripXMLComments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#stripXMLComments(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestStripXMLComment.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestStripXMLComment.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestStripXMLComment" )
public class TestStripXMLComment extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#stripXMLComments(CharSequence)}.
     */
    @Test
    final void testStripXMLComments()
    {
        skipThreadTest();

        String actual, candidate, expected;

        candidate = EMPTY_STRING;
        expected = candidate;
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = "No Comment";
        expected = candidate;
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = "<!-- Comment only -->";
        expected = EMPTY_STRING;
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = "Text <!-- The comment --> with a comment";
        expected = "Text  with a comment";
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = "Multi-line text\n<!-- The comment -->\nwith a comment";
        expected = "Multi-line text\n\nwith a comment";
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );
    }   //  testStripXMLComments()

    /**
     *  Tests for
     *  {@link StringUtils#stripXMLComments(CharSequence)}.
     */
    @Test
    final void testStripXMLCommentsWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            stripXMLComments( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStripXMLCommentsNull()
}
//  class testStripXMLCommentsWithNullArgument

/*
 *  End of File
 */