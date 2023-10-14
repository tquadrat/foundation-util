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

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.stripTags;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#stripTags(CharSequence)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestStripTags.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestStripTags.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestStripTags" )
public class TestStripTags extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#stripTags(CharSequence)}.
     */
    @DisplayName( "StringUtils.stripTags() with valid argument" )
    @Test
    final void testStripTags()
    {
        skipThreadTest();

        String actual, expected, input;

        input = EMPTY_STRING;
        expected = input;
        actual = stripTags( input );
        assertNotNull( actual );
        assertEquals( expected, actual );

        input = " ";
        expected = EMPTY_STRING;
        actual = stripTags( input );
        assertNotNull( actual );
        assertEquals( expected, actual );

        input = "No Tags";
        expected = input;
        actual = stripTags( input );
        assertNotNull( actual );
        assertEquals( expected, actual );

        input = "<!-- Comment -->";
        expected = EMPTY_STRING;
        actual = stripTags( input );
        assertNotNull( actual );
        assertEquals( expected, actual );

        input = """
                         \s
                <!-- Comment -->                 \s     
                           \s
             """;
        expected = EMPTY_STRING;
        actual = stripTags( input );
        assertNotNull( actual );
        assertEquals( expected, actual );

        input = """
                <html>
                    <head>
                        <meta/>
                        </head>
                        <body>
                            <a href='…'>       Simple          <br>
                            <br>           Text       </a>
                        </body>
                    </html>
                """;
        expected = "Simple Text";
        actual = stripTags( input );
        assertNotNull( actual );
        assertEquals( expected, actual );
    }   //  testStripTags()

    /**
     *  Tests for
     *  {@link StringUtils#stripTags(CharSequence)}.
     */
    @Test
    final void testStripTagsWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            stripTags( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStripTagsWithNullArgument()
}
//  class TestStripTags

/*
 *  End of File
 */