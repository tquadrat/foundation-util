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

import static java.lang.String.format;
import static java.util.TimeZone.getTimeZone;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;

import java.io.Serial;
import java.util.Set;
import java.util.TimeZone;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link TimeZone}
 *  values.<br>
 *  <br>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link TimeZone#getTimeZone(String)}
 *  to retrieve a {@code TimeZone} based on the given value.<br>
 *  <br>The method
 *  {@link #toString(TimeZone)}
 *  will use
 *  {@link TimeZone#getID()}
 *  to do the conversion to a String.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TimeZoneStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "UseOfObsoleteDateTimeApi" )
@ClassVersion( sourceVersion = "$Id: TimeZoneStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class TimeZoneStringConverter implements StringConverter<TimeZone>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid or unknown time zone id on the command
     *  line: {@value}.
     */
    public static final String MSG_UnknownTimeZone = "Unknown TimeZone: %1$s";

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
    public static final TimeZoneStringConverter INSTANCE = new TimeZoneStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code TimeZoneStringConverter}.
     */
    public TimeZoneStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final TimeZone fromString( final CharSequence source ) throws IllegalArgumentException
    {
        TimeZone retValue = null;
        if( nonNull( source ) )
        {
            final var timezone = source.toString();
            retValue = getTimeZone( timezone );
            if( retValue.equals( getTimeZone( "GMT") ) && !Set.of( TimeZone.getAvailableIDs() ).contains( timezone ) )
            {
                throw new IllegalArgumentException( format( MSG_UnknownTimeZone, timezone ) );
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
    public static final TimeZoneStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final TimeZone source )
    {
        final var retValue = isNull( source ) ? null : source.getID();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class TimeZoneStringConverter

/*
 *  End of File
 */