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

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The implementation of
 *  {@link TimeDateStringConverter}
 *  for
 *  {@link java.time.Instant}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: InstantStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: InstantStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public class InstantStringConverter extends TimeDateStringConverter<Instant>
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
    public static final InstantStringConverter INSTANCE = new InstantStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code InstantStringConverter} instance.
     */
    public InstantStringConverter() { super( Instant.class ); }

    /**
     *  Creates a new {@code InstantStringConverter} instance that uses the
     *  given formatter for the conversion back and forth.
     *
     *  @note The formatter may not drop any part of the instant,
     *      otherwise
     *      {@link #fromString(CharSequence)}
     *      may fail. This means that the formatter is only allowed to re-order
     *      the temporal fields.
     *
     *  @param  formatter   The formatter for the date/time data.
     */
    public InstantStringConverter( final DateTimeFormatter formatter )
    {
        super( Instant.class, formatter );
    }   //  InstantStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Instant parseDateTime( final CharSequence source, final Optional<DateTimeFormatter> formatter ) throws DateTimeParseException
    {
        final var retValue = formatter.map( dateTimeFormatter -> Instant.from( dateTimeFormatter.parse( source ) ) ).orElse( Instant.parse( source ) );

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
    public static final InstantStringConverter provider() { return INSTANCE; }
}
//  class InstantStringConverter

/*
 *  End of File
 */