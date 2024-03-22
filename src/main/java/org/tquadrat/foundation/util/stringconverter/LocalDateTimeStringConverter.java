/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The implementation of
 *  {@link TimeDateStringConverter}
 *  for {@code java.time.LocalDateTime}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LocalDateTimeStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: LocalDateTimeStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public class LocalDateTimeStringConverter extends TimeDateStringConverter<LocalDateTime>
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
    public static final LocalDateTimeStringConverter INSTANCE = new LocalDateTimeStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LocalDateTimeStringConverter} instance.
     */
    public LocalDateTimeStringConverter() { super( LocalDateTime.class ); }

    /**
     *  Creates a new {@code LocalDateTimeStringConverter} instance.
     *
     *  @note The formatter may not drop any part of the Zoned date time,
     *      otherwise {@code fromString()} may fail. This means that the
     *      formatter is only allowed to re-order the temporal fields.
     *
     *  @param  formatter   The formatter for the date/time data.
     */
    public LocalDateTimeStringConverter( final DateTimeFormatter formatter )
    {
        super( LocalDateTime.class, formatter );
    }   //  LocalDateTimeStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final LocalDateTime parseDateTime( final CharSequence source, final Optional<DateTimeFormatter> formatter ) throws DateTimeParseException
    {
        final var retValue = LocalDateTime.from( formatter.orElse( ISO_LOCAL_DATE_TIME ).parse( source ) );

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
    public static final LocalDateTimeStringConverter provider() { return INSTANCE; }
}
//  class LocalDateTimeStringConverter

/*
 *  End of File
 */