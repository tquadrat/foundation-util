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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;

import java.io.Serial;
import java.util.Currency;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link Currency}
 *  values.}</p>
 *  <p>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link Currency#getInstance(String)}
 *  to retrieve a {@code Currency} based on the given ISO&nbsp;4217 code.}.</p>
 *  <p>The method
 *  {@link #toString(Currency)}
 *  will use
 *  {@link Currency#getCurrencyCode()}
 *  to return the ISO&nbsp;4217 code to the given {@code Currency}
 *  instance.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CurrencyStringConverter.java 997 2022-01-26 14:55:05Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CurrencyStringConverter.java 997 2022-01-26 14:55:05Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class CurrencyStringConverter implements StringConverter<Currency>
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
    public static final CurrencyStringConverter INSTANCE = new CurrencyStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Currency fromString( final CharSequence source ) throws IllegalArgumentException
    {
        final var retValue = nonNull( source ) ? Currency.getInstance( source.toString() ) : null;

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
    public static final CurrencyStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final Currency source )
    {
        final var retValue = nonNull( source ) ? source.getCurrencyCode() : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ZoneIdStringConverter

/*
 *  End of File
 */