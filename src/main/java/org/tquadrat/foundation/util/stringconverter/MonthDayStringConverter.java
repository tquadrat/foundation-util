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

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The implementation of
 *  {@link TimeDateStringConverter}
 *  for {@code java.time.MonthDay}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: MonthDayStringConverter.java 1130 2024-05-05 16:16:09Z tquadrat $
 *  @since 0.4.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: MonthDayStringConverter.java 1130 2024-05-05 16:16:09Z tquadrat $" )
@API( status = STABLE, since = "0.4.6" )
public class MonthDayStringConverter extends TimeDateStringConverter<MonthDay>
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
    public static final MonthDayStringConverter INSTANCE = new MonthDayStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code MonthDayStringConverter} instance.
     */
    public MonthDayStringConverter() { super( MonthDay.class ); }

    /**
     *  Creates a new {@code MonthDayStringConverter} instance.
     *
     *  @note The formatter may not drop neither month nor day, otherwise
     *      {@code fromString()} may fail. This means that the formatter is
     *      only allowed to re-order the temporal fields.
     *
     *  @param  formatter   The formatter for the temporal accessor.
     */
    public MonthDayStringConverter( final DateTimeFormatter formatter )
    {
        super( MonthDay.class, formatter );
    }   //  MonthDayStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final MonthDay parseDateTime( final CharSequence source, final Optional<DateTimeFormatter> formatter ) throws DateTimeParseException
    {
        final var retValue = formatter.map( dateTimeFormatter -> MonthDay.parse( source, dateTimeFormatter ) ).orElse( MonthDay.parse( source ) );

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
    public static final MonthDayStringConverter provider() { return INSTANCE; }
}
//  class MonthDayStringConverter

/*
 *  End of File
 */