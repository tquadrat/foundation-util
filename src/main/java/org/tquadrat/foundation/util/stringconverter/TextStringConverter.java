/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
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

package org.tquadrat.foundation.util.stringconverter;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;

import java.io.Serial;
import java.util.stream.IntStream;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary An implementation of
 *  {@link StringConverter}
 *  for text values.}</p>
 *  <p>Although technically, a text is a String and therefore a conversion to
 *  String would be redundant (at best), semantically there is difference, and
 *  this implementation of {@code StringConverter} takes care of that.</p>
 *  <p>Other than for the conversions performed by
 *  {@link StringStringConverter},
 *  the results are not identical with the input values: a text may contain
 *  several special characters, like new lines, tabs, backspaces or form feeds.
 *  These will be translated by the
 *  {@link #toString(String)}
 *  method into escape sequences,
 *  while
 *  {@link #fromString(CharSequence)}
 *  will translate the escape sequences back to the special characters,
 *  according to the table below.</p>
 *  <table border="1">
 *      <caption>Special Characters and their escape sequences</caption>
 *      <thead>
 *          <tr>
 *              <th>Name</th><th>Code</th><th>Escape</th><th>Comment</th>
 *          </tr>
 *      </thead>
 *      <tbody>
 *          <tr>
 *              <td>backspace</td><td>U+0008</td><td>\b</td><td>&nbsp;</td>
 *          </tr>
 *          <tr>
 *              <td>horizontal tab</td><td>U+0009</td><td>\t</td><td>&nbsp;</td>
 *          </tr>
 *          <tr>
 *              <td>line feed</td><td>U+000A</td><td>\n</td><td>The UNIX line termination, also used in Java internally as the new-line character</td>
 *          </tr>
 *          <tr>
 *              <td>form feed</td><td>U+000C</td><td>\f</td><td>Rarely used</td>
 *          </tr>
 *          <tr>
 *              <td>carriage return</td><td>U+000D</td><td>\r</td><td>The Windows line termination is CRLF or \r\n</td>
 *          </tr>
 *          <tr>
 *              <td>space</td><td>U+0020</td><td>\s</td><td>A space or blank will be escaped only if it is the very first or the last character of a text</td>
 *          </tr>
 *      </tbody>
 *  </table>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TextStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: TextStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class TextStringConverter implements StringConverter<String>
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *  An instance of this class.
     */
    public static final TextStringConverter INSTANCE = new TextStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code TextStringConverter}.
     */
    public TextStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the escape sequence for the given special character, or the
     *  character itself if it does not need an escape.
     *
     *  @param  c   The character.
     *  @return  The escape sequence.
     */
    private final IntStream escape( @SuppressWarnings( "StandardVariableNames" ) final int c )
    {
        final var retValue = switch( c )
        {
            case '\b' -> IntStream.of( '\\', 'b' );
            case '\t' -> IntStream.of( '\\', 't' );
            case '\n' -> IntStream.of( '\\', 'n' );
            case '\f' -> IntStream.of( '\\', 'f' );
            case '\r' -> IntStream.of( '\\', 'r' );
            case '\\' -> IntStream.of( '\\', '\\' );
            default -> c < 0x20
                ? IntStream.concat( IntStream.of( '\\' ), Integer.toString( c, 8 ).codePoints() )
                : IntStream.of( c );
        };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escape()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String fromString( final CharSequence source ) throws IllegalArgumentException
    {
        final var retValue = isNull( source ) ? null : source.toString().translateEscapes();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final String source )
    {
        String retValue = null;
        if( nonNull( source ) )
        {
            var text = source;
            var prefix = EMPTY_STRING;
            var suffix = EMPTY_STRING;
            if( text.startsWith( " " ) )
            {
                prefix = "\\s";
                text = text.length() > 1 ? text.substring( 1 ) : EMPTY_STRING;
            }
            if( text.endsWith( " " ) )
            {
                suffix = "\\s";
                text = text.substring( 0, text.length() - 1 );
            }
            final var codePoints = text.codePoints()
                .flatMap( this::escape )
                .toArray();
            retValue = prefix + new String( codePoints, 0, codePoints.length ) + suffix;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class TextStringConverter

/*
 *  End of File
 */