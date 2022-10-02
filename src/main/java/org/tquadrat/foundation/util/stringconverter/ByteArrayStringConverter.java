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

import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.ASCII;
import static org.tquadrat.foundation.lang.Objects.isNull;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary The implementation of
 *  {@link StringConverter}
 *  for {@code byte} arrays.}</p>
 *  <p>The output from
 *  {@link #toString(byte[])}
 *  will be in BASE64 format, encoded to
 *  {@linkplain java.nio.charset.StandardCharsets#US_ASCII ASCII}.</p>
 *  <p>Correspondingly,
 *  {@link #fromString(CharSequence)}
 *  expects an ASCII encoded BASE64 stream.</p>
 *  <p>Both methods are using the BASE64 <i>basic</i> encoding scheme.</p>
 *
 *  @see java.util.Base64
 *  @see java.util.Base64#getEncoder()
 *  @see java.util.Base64#getDecoder()
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ByteArrayStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ByteArrayStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class ByteArrayStringConverter implements StringConverter<byte[]>
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  An instance of this class.
     */
    public static final ByteArrayStringConverter INSTANCE = new ByteArrayStringConverter();

    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code ByteArrayStringConverter}.
     */
    public ByteArrayStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final byte [] fromString( final CharSequence source ) throws IllegalArgumentException
    {
        final var retValue = isNull( source ) ? null : getDecoder().decode( source.toString().getBytes( ASCII ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final ByteArrayStringConverter provider() { return INSTANCE; }

    /**
     *  Provides the subject class for this converter.
     *
     *  @return The subject class.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<Class<?>> getSubjectClass() { return List.of( byte [].class ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final byte [] source )
    {
        final var retValue = isNull( source ) ? null : new String( getEncoder().encode( source ), ASCII );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ByteArrayStringConverter

/*
 *  End of File
 */