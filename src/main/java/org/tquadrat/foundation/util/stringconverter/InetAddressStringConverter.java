/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

import static java.net.InetAddress.getByName;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isEmpty;

import java.io.Serial;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The implementation of
 *  {@link StringConverter}
 *  for
 *  {@link InetAddress}
 *  values.<br>
 *  <br>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link InetAddress#getByName(String)}
 *  to create a {@code InetAddress} instance from the given value; this means,
 *  that when a host name is given as an argument &ndash; instead of an IP4 or
 *  IP6 address String &ndash; an
 *  {@link IllegalArgumentException}
 *  is thrown when that host is unknown (cannot be resolved by DNS). But
 *  {@code fromString()} will not accept {@code null} or the empty String for
 *  {@code localhost}; for these values it will also throw an
 *  {@code IllegalArgumentException}.<br>
 *  <br>The method
 *  {@link #toString(InetAddress)}
 *  uses
 *  {@link InetAddress#getHostAddress()}
 *  to get the IP address as a String.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: InetAddressStringConverter.java 966 2022-01-04 22:28:49Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: InetAddressStringConverter.java 966 2022-01-04 22:28:49Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class InetAddressStringConverter implements StringConverter<InetAddress>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message about an invalid address or an unknown host name:
     *  {@value}.
     */
    public static final String MSG_InvalidAddress = "'%1$s' is invalid or unknown";

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
    public static final InetAddressStringConverter INSTANCE = new InetAddressStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final InetAddress fromString( final CharSequence source ) throws IllegalArgumentException
    {
        InetAddress retValue = null;
        if( nonNull( source ) )
        {
            if( isEmpty( source ) ) throw new IllegalArgumentException( format( MSG_InvalidAddress, source ) );
            try
            {
                retValue = getByName( source.toString() );
            }
            catch( final UnknownHostException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidAddress, source ), e );
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
    public static final InetAddressStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final InetAddress source )
    {
        final var retValue = nonNull( source ) ? source.getHostAddress() : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class InetAddressStringConverter

/*
 *  End of File
 */