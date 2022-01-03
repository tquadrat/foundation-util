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
import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link Duration}
 *  values.<br>
 *  <br>While the method
 *  {@link StringConverter#toString(Object) toString(Duration)}
 *  simply uses
 *  {@link Duration#toString()},
 *  the method
 *  {@link #fromString(CharSequence)}
 *  uses
 *  {@link Duration#parse(CharSequence)}
 *  to create the {@code Duration} instance for the given value. The formats
 *  accepted are based on the ISO-8601 duration format {@code PnDTnHnMn.nS}
 *  with days considered to be exactly 24 hours.<br>
 *  <br>The string starts with an optional sign, denoted by the ASCII negative
 *  ('-') or positive ('+') symbol. If negative, the whole period is
 *  negated.<br>
 *  <br>The ASCII letter &quot;P&quot; is next in upper or lower case. There
 *  are then four sections, each consisting of a number and a suffix.<br>
 *  <br>The sections have suffixes in ASCII of &quot;D&quot;, &quot;H&quot;,
 *  &quot;M&quot; and &quot;S&quot; for days, hours, minutes and seconds,
 *  accepted in upper or lower case.<br>
 *  <br>The suffixes must occur in order. The ASCII letter &quot;T&quot; must
 *  occur before the first occurrence, if any, of an hour, minute or second
 *  section.<br>
 *  <br>At least one of the four sections must be present, and if &quot;T&quot;
 *  is present there must be at least one section after the &quot;T&quot;.<br>
 *  <br>The number part of each section must consist of one or more ASCII
 *  digits. The number may be prefixed by the ASCII negative or positive
 *  symbol. The number of days, hours and minutes must parse to a {@code long}.
 *  The number of seconds must parse to a {@code long} with optional fraction.
 *  The decimal point may be either a dot or a comma. The fractional part may
 *  have from zero to 9 digits.
 *
 *  @note The leading plus/minus sign, and negative values for other units are
 *      not originally part of the ISO-8601 standard.
 *
 *  @see Duration
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DurationStringConverter.java 892 2021-04-03 18:07:28Z tquadrat $
 *  @since 0.0.7
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: DurationStringConverter.java 892 2021-04-03 18:07:28Z tquadrat $" )
@API( status = STABLE, since = "0.0.7" )
public final class DurationStringConverter implements StringConverter<Duration>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid duration String: {@value}.
     */
    public static final String MSG_InvalidDuration = "'%1$s' cannot be parsed as a valid duration";

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
    public static final DurationStringConverter INSTANCE = new DurationStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Duration fromString( final CharSequence source ) throws IllegalArgumentException
    {
        Duration retValue = null;
        if( nonNull( source ) )
        {
            try
            {
                retValue = Duration.parse( source );
            }
            catch( final DateTimeParseException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidDuration, source ), e );
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
    public static final DurationStringConverter provider() { return INSTANCE; }
}
//  class DurationStringConverter

/*
 *  End of File
 */