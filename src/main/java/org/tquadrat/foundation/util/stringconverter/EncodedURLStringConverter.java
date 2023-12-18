/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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

package org.tquadrat.foundation.util.stringconverter;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.io.Serial;
import java.net.URL;
import java.nio.charset.Charset;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link URL}
 *  values.}
 *  Different from
 *  {@link URLStringConverter},
 *  this implementation expects an &quot;{@code application/x-www-form-url}&quot;
 *  encoded String as the argument for
 *  {@link #fromString(CharSequence)}
 *  and returns such a String from
 *  {@link #toString(URL)}.</p>
 *  <p>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link java.net.URLDecoder#decode(String, Charset)}
 *  to get the decode URL String, then it calls
 *  {@link URLStringConverter#fromString(CharSequence)}
 *  to create a {@code URL} instance from it.</p>
 *  <p>{@link #toString(URL)}
 *  uses
 *  {@link URLStringConverter#toString(URL)}
 *  to get a String out of the URL and encodes it with a call to
 *  {@link java.net.URLEncoder#encode(String, Charset)}.</p>
 *  <p>Based on the {@href http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars World Wide Web Consortium Recommendation}
 *  both methods ({@code fromString()} and {@code toString()}) are using
 *  {@code UTF-8} as the
 *  {@linkplain Charset <code>charset</code> argument}.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: EncodedURLStringConverter.java 1004 2022-02-02 11:23:44Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: EncodedURLStringConverter.java 1004 2022-02-02 11:23:44Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class EncodedURLStringConverter extends URLStringConverter
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The character encoding that is used at default.
     *
     *  @serial
     */
    private final Charset m_Encoding;

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
     *  <p>{@summary An instance of this class.}</p>
     *  <p>This instance uses
     *  {@linkplain org.tquadrat.foundation.lang.CommonConstants#UTF8 UTF-8}
     *  as the default encoding.</p>
     */
    public static final EncodedURLStringConverter INSTANCE = new EncodedURLStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code EncodedURLStringConverter} that uses
     *  {@linkplain org.tquadrat.foundation.lang.CommonConstants#UTF8 UTF-8}
     *  as the default encoding.
     */
    public EncodedURLStringConverter() { this( UTF8 ); }

    /**
     *  Creates a new instance of {@code EncodedURLStringConverter} that uses
     *  the given encoding as the default encoding.
     *
     *  @param  encoding    The default character encoding.
     *
     *  @see java.net.URLEncoder#encode(String, Charset)
     *  @see java.net.URLDecoder#decode(String, Charset)
     */
    public EncodedURLStringConverter( final Charset encoding )
    {
        m_Encoding = requireNonNullArgument( encoding, "encoding" );
    }   //  EncodedURLStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Converts the given String that contains a valid
     *  {@code application/x-www-form-url} encoded URL into an instance of
     *  {@link URL},
     *  using the provided character encoding.
     *
     *  @param  source  The source; can be {@code null}.
     *  @param  charset The character encoding of the String.
     *  @return The respective URL, or {@code null} if the source was already
     *      {@code null}.
     *  @throws IllegalArgumentException    The source was either not a valid
     *      URL, or the encoding was invalid.
     */
    public final URL fromString( final CharSequence source, final Charset charset ) throws IllegalArgumentException
    {
        requireNonNullArgument( charset, "charset" );
        URL retValue = null;
        if( nonNull( source ) )
        {
            final var url = decode( source.toString(), charset );
            retValue = super.fromString( url );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  {@inheritDoc}
     *  <p>This method uses the default character encoding as set by the
     *  constructor.</p>
     */
    @Override
    public URL fromString( final CharSequence source ) throws IllegalArgumentException
    {
        final var retValue = fromString( source, m_Encoding );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  Converts the given
     *  {@link URL},
     *  into an {@code application/x-www-form-url} encoded String
     *  representation, using the provided character encoding.
     *
     *  @param  source  The source; can be {@code null}.
     *  @param  charset The character encoding for the String.
     *  @return The respective String representation, or {@code null} if the
     *      source was already {@code null}.
     *  @throws IllegalArgumentException    The source was either not a valid
     *      URL, or the encoding was invalid.
     */
    public final String toString( final URL source, final Charset charset )
    {
        requireNonNullArgument( charset, "charset" );
        final var url = super.toString( source);
        final var retValue = nonNull( url ) ? encode( url, charset ) : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  {@inheritDoc}
     *  <p>This method uses the default character encoding as set by the
     *  constructor.</p>
     */
    @Override
    public final String toString( final URL source )
    {
        final var retValue = toString( source, m_Encoding );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class EncodedURLStringConverter

/*
 *  End of File
 */