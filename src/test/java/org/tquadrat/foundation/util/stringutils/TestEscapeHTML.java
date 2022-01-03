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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.escapeHTML;
import static org.tquadrat.foundation.util.StringUtils.unescapeHTML;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#escapeHTML(CharSequence)},
 *  {@link StringUtils#escapeHTML(Appendable, CharSequence)},
 *  {@link StringUtils#unescapeHTML(CharSequence)},
 *  and
 *  {@link StringUtils#escapeHTML(Appendable, CharSequence)}.
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestEscapeHTML.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestEscapeHTML.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestEscapeHTML" )
public class TestEscapeHTML extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for
     *  {@link StringUtils#escapeHTML(CharSequence)}.
     *
     *  @param  plain   The plain text.
     *  @param  escaped The same text with the HTML&nbsp;5 entities.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "EscapeHTML.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testEscapeHTML( final String plain, final String escaped )
    {
        skipThreadTest();

        assertEquals( translateEscapes( escaped ), escapeHTML( translateEscapes( plain ) ) );
    }   //  testEscapeHTML()

    /**
     *  Some tests for
     *  {@link StringUtils#escapeHTML(CharSequence)}.
     */
    @Test
    final void testEscapeHTMLWithNullArgument()
    {
        skipThreadTest();

        assertNull( escapeHTML( null ) );
    }   //  testEscapeHTMLWithNullArgument()

    /**
     *  Test for the methods
     *  {@link StringUtils#escapeHTML(CharSequence)}
     *  and
     *  {@link StringUtils#unescapeHTML(CharSequence)}.<br>
     *  <br>Because HTML&nbsp;5 maps some Unicode characters to multiple
     *  entities, the following should be always {@code true}:
     *  <pre><code>unescape( escape( text ) ) == text</code></pre> while the
     *  opposite<pre><code>escape( unescape( text ) ) == text</code></pre> may
     *  fail, depending on the contents of {@code text}.
     *
     *  @param  input1  The plain text.
     *  @param  input2  The same text with the HTML&nbsp;5 entities.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "EscapeHTML.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testHTMLEscapeUnescape( final String input1, final String input2 )
    {
        skipThreadTest();

        final var plain = translateEscapes( input1 );
        final var escaped = translateEscapes( input2 );

        final var actual = escapeHTML( plain );
        if( nonNull( plain ) )
        {
            assertNotNull( actual );
            assertEquals( escaped, actual );
        }
        else
        {
            assertNull( actual );
        }
        final var revert = unescapeHTML( actual );
        if( nonNull( plain ) )
        {
            assertNotNull( revert );
        }
        else
        {
            assertNull( revert );
        }
        assertEquals( plain, revert );
    }   //  testHTMLEscapeUnescape()

    /**
     *  Test for the methods
     *  {@link StringUtils#escapeHTML(Appendable,CharSequence)}
     *  and
     *  {@link StringUtils#unescapeHTML(Appendable,CharSequence)}.<br>
     *  <br>Because HTML&nbsp;5 maps some Unicode characters to multiple
     *  entities, the following should be always {@code true}:
     *  <pre><code>unescape( escape( text ) ) == text</code></pre> while the
     *  opposite<pre><code>escape( unescape( text ) ) == text</code></pre> may
     *  fail, depending on the contents of {@code text}.
     *
     *  @param  input1  The plain text.
     *  @param  input2  The same text with the HTML&nbsp;5 entities.
     *  @throws IOException Something really unexpected went significantly
     *      wrong.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "EscapeHTML.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testHTMLEscapeUnescapeAppendable( final String input1, @SuppressWarnings( "unused" ) final String input2 ) throws IOException
    {
        skipThreadTest();

        final var plain = translateEscapes( input1 );

        final var appendable = new StringBuilder();

        escapeHTML( appendable, plain );
        final var actual = appendable.toString();
        assertNotNull( actual );
        appendable.setLength( 0 );

        unescapeHTML( appendable, actual );
        final var revert = appendable.toString();
        assertNotNull( revert );
        if( nonNull( plain ) )
        {
            assertEquals( plain, revert );
        }
        else
        {
            assertTrue( revert.isEmpty() );
        }
    }   //  testHTMLEscapeUnescapeAppendable()

    /**
     *  Some tests for
     *  {@link StringUtils#unescapeHTML(CharSequence)}.
     *
     *  @param  plain   The plain text.
     *  @param  escaped The same text with the HTML&nbsp;5 entities.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "EscapeHTML.csv", delimiter = ';', numLinesToSkip = 1 )
    final void testUnescapeHTML( final String plain, final String escaped )
    {
        skipThreadTest();

        assertEquals( translateEscapes( plain ), unescapeHTML( translateEscapes( escaped ) ) );
    }   //  testUnescapeHTML()

    /**
     *  Some tests for
     *  {@link StringUtils#unescapeHTML(CharSequence)}.
     */
    @Test
    final void testUnescapeHTMLWithNullArgument()
    {
        skipThreadTest();

        assertNull( unescapeHTML( null ) );
    }   //  testUnescapeHTMLWithNullArgument()
}
//  class TestEscapeHTML

/*
 *  End of File
 */