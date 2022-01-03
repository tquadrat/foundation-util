/*
 * ============================================================================
 * Copyright © 2002-2019 by Thomas Thrien.
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

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The implementation of
 *  {@link TimeDateStringConverter}
 *  for {@code java.time.LocalTime}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LocalTimeStringConverter.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: LocalTimeStringConverter.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public class LocalTimeStringConverter extends TimeDateStringConverter<LocalTime>
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
    public static final LocalTimeStringConverter INSTANCE = new LocalTimeStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LocalTimeStringConverter} instance.
     */
    public LocalTimeStringConverter() { super( LocalTime.class ); }

    /**
     *  Creates a new {@code LocalTimeStringConverter} instance.
     *
     *  @note The formatter may not drop any part of the time,
     *      otherwise
     *      {@link #fromString(CharSequence)}
     *      may fail. This means that the formatter is only allowed to re-order
     *      the temporal fields.
     *
     *  @param  formatter   The formatter for the date/time data.
     */
    public LocalTimeStringConverter( final DateTimeFormatter formatter )
    {
        super( LocalTime.class, formatter );
    }   //  LocalTimeStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final LocalTime parseDateTime( final CharSequence source, final Optional<DateTimeFormatter> formatter ) throws DateTimeParseException
    {
        final var retValue = LocalTime.from( formatter.orElse( ISO_LOCAL_TIME ).parse( source ) );

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
    public static final LocalTimeStringConverter provider() { return INSTANCE; }
}
//  class LocalTimeStringConverter

/*
 *  End of File
 */