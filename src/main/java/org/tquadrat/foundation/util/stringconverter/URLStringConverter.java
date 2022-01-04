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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.io.Serial;
import java.net.MalformedURLException;
import java.net.URL;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link URL}
 *  values.<br>
 *  <br>The method
 *  {@link #fromString(CharSequence)}
 *  will use the constructor
 *  {@link URL#URL(String)}
 *  to create a {@code URL} instance from the given value.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: URLStringConverter.java 966 2022-01-04 22:28:49Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: URLStringConverter.java 966 2022-01-04 22:28:49Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class URLStringConverter implements StringConverter<URL>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid URL on the command line: {@value}.
     */
    public static final String MSG_InvalidURL = "'%1$s' cannot be parsed as a valid URL";

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
    public static final URLStringConverter INSTANCE = new URLStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public URL fromString( final CharSequence source ) throws IllegalArgumentException
    {
        URL retValue = null;
        if( nonNull( source ) )
        {
            try
            {
                retValue = new URL( source.toString() );
            }
            catch( final MalformedURLException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidURL, source ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link org.tquadrat.foundation.lang.StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final URLStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final URL source )
    {
        final var retValue = isNull( source ) ? null : source.toExternalForm();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class URLStringConverter

/*
 *  End of File
 */