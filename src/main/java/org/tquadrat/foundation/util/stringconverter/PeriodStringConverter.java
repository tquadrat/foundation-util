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
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.io.Serial;
import java.time.Period;
import java.time.format.DateTimeParseException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link Period}
 *  values.<br>
 *  <br>While the method
 *  {@link StringConverter#toString(Object) toString(Period)}
 *  simply uses
 *  {@link Period#toString()},
 *  the method
 *  {@link #fromString(CharSequence)}
 *  uses
 *  {@link Period#parse(CharSequence)}
 *  to create the {@code Period} instance for the given value. The formats
 *  accepted are based on the ISO-8601 period formats {@code PnYnMnD} and
 *  {@code PnW}.<br>
 *  <br>The string starts with an optional sign, denoted by the ASCII negative
 *  ('-') or positive ('+') symbol. If negative, the whole period is
 *  negated.<br>
 *  <br>The ASCII letter &quot;P&quot; is next in upper or lower case.<br>
 *  <br>There are then four sections, each consisting of a number and a suffix.
 *  At least one of the four sections must be present.<br>
 *  <br>The sections have suffixes in ASCII of &quot;Y&quot;, &quot;M&quot;,
 *  &quot;W&quot; and &quot;D&quot; for years, months, weeks and days, accepted
 *  in upper or lower case.<br>
 *  <br>The suffixes must occur in order. The number part of each section must
 *  consist of ASCII digits. The number may be prefixed by the ASCII negative
 *  or positive symbol. The number must parse to an {@code int}.
 *
 *  @note The leading plus/minus sign, and negative values for other units are
 *      not originally part of the ISO-8601 standard.
 *  @note In addition, ISO-8601 does not permit mixing between the
 *      {@code PnYnMnD} and {@code PnW} formats. Any week-based input is
 *      multiplied by 7 and treated as a number of days.
 *
 *  @see Period
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PeriodStringConverter.java 892 2021-04-03 18:07:28Z tquadrat $
 *  @since 0.0.7
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PeriodStringConverter.java 892 2021-04-03 18:07:28Z tquadrat $" )
@API( status = STABLE, since = "0.0.7" )
public final class PeriodStringConverter implements StringConverter<Period>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid period String: {@value}.
     */
    public static final String MSG_InvalidPeriod = "'%1$s' cannot be parsed as a valid period";

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
    public static final PeriodStringConverter INSTANCE = new PeriodStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Period fromString( final CharSequence source ) throws IllegalArgumentException
    {
        Period retValue = null;
        if( nonNull( source ) )
        {
            try
            {
                retValue = Period.parse( source );
            }
            catch( final DateTimeParseException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidPeriod, source ), e );
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
    public static final PeriodStringConverter provider() { return INSTANCE; }
}
//  class PeriodStringConverter

/*
 *  End of File
 */