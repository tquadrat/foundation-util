/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.isNull;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

import java.io.Serial;

/**
 *  The implementation of
 *  {@link StringConverter}
 *  for
 *  {@link String}
 *  values in BASE64 format.<br>
 *  <br>The BASE64 format returned from
 *  {@link #fromString(CharSequence)}
 *  contains the source String in
 *  {@linkplain java.nio.charset.StandardCharsets#UTF_8 UTF-8}
 *  encoding and it is itself encoded to
 *  {@linkplain java.nio.charset.StandardCharsets#US_ASCII ASCII}.<br>
 *  <br>Correspondingly,
 *  {@link #toString(String)}
 *  expects an ASCII encoded BASE64 stream containing a UTF-8 encoded
 *  String.<br>
 *  <br>Both methods are using the BASE64 <i>basic</i> encoding scheme.
 *
 *  @see java.util.Base64
 *  @see java.util.Base64#getEncoder()
 *  @see java.util.Base64#getDecoder()
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: BASE64StringConverter.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: BASE64StringConverter.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class BASE64StringConverter implements StringConverter<String>
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  An instance of this class.
     */
    public static final BASE64StringConverter INSTANCE = new BASE64StringConverter();

    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String fromString( final CharSequence source ) throws IllegalArgumentException
    {
        final var retValue = isNull( source ) ? null : new String( getEncoder().encode( source.toString().getBytes( UTF8 ) ), ASCII );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final String source )
    {
        final var retValue = isNull( source ) ? null : new String( getDecoder().decode( source.getBytes( UTF8 ) ), UTF8 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class BASE64StringConverter

/*
 *  End of File
 */