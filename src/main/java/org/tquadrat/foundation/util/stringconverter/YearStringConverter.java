/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static java.time.format.SignStyle.NORMAL;
import static java.time.temporal.ChronoField.YEAR;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;

import java.io.Serial;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The implementation of
 *  {@link TimeDateStringConverter}
 *  for {@code java.time.Year}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: YearStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: YearStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public class YearStringConverter extends TimeDateStringConverter<Year>
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
    public static final YearStringConverter INSTANCE = new YearStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code YearStringConverter} instance.
     */
    public YearStringConverter() { super( Year.class ); }

    /**
     *  Creates a new {@code YearStringConverter} instance.
     *
     *  @note The formatter may not drop any part of the temporal value,
     *      otherwise
     *      {@link #fromString(CharSequence)}
     *      may fail. This means that the formatter is only allowed to re-order
     *      the temporal fields.
     *
     *  @param  formatter   The formatter for the date/time data.
     */
    public YearStringConverter( final DateTimeFormatter formatter )
    {
        super( Year.class, formatter );
    }   //  YearStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Year parseDateTime( final CharSequence source, final Optional<DateTimeFormatter> formatter ) throws DateTimeParseException
    {
        Year retValue = null;
        if( nonNull( source ) )
        {
            /*
             * We use Year.toString() when converting a Year to a String; this
             * results in the String "-333" for the year of the Battle at
             * Knossos.
             * Unfortunately, the method Year.parse(CharSequence) will call
             * internally the method Year.parse(CharSequence,DateTimeFormatter)
             * with the internally defined instance PARSER as its second
             * argument.
             *
             * PARSER is defined as below:
             *    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
             *         .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
             *         .toFormatter();
             *
             * This will cause a DateTimeParseException for all years with less
             * than four digits. Consequently, we have to define our own
             * DateTimeFormatter when none was provided to the StringConverter.
             *
             * When constructing the DateTimeFormatter, a call to
             * DateTimeFormatterBuilder.parseLenient() is required if we keep
             * the SignStyle EXCEEDS_PAD, because no sign is printed for years
             * after 0. We decided to use NORMAL instead, staying with strict
             * parsing.
             */
            final var parser = formatter.orElseGet( () ->
                new DateTimeFormatterBuilder()
//                    .parseLenient()
//                    .appendValue( YEAR, 1, 10, EXCEEDS_PAD )
                    .appendValue( YEAR, 1, 10, NORMAL )
                    .toFormatter() );
            retValue = Year.parse( source, parser );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseDateTime()

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link org.tquadrat.foundation.lang.StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    public static final YearStringConverter provider() { return INSTANCE; }
}
//  class YearStringConverter

/*
 *  End of File
 */