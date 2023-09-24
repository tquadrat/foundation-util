/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util.stringconverter;

import static java.lang.String.format;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.stringconverter.NumberStringConverter.MSG_InvalidNumberFormat;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary An implementation for the interface
 *  {@link StringConverter}
 *  for
 *  {@link Date}.}</p>
 *  <p>This converter translates an instance of {@code Date} into a String
 *  containing an number that represents the milliseconds since the begin of
 *  the epoch (1970-01-01T00:00:00 UTC); conversely it expects such a String to
 *  convert it to an instance of {@code Date}. This approach circumvents the
 *  issues that exists with the numerous String formats that otherwise exists
 *  for {@code Date}, especially when it comes to parsing based on different
 *  locales.</p>
 *
 *  @see Date#Date(long)
 *  @see Date#getTime()
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DateLongStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "UseOfObsoleteDateTimeApi" ) // Obviously unavoidable …
@ClassVersion( sourceVersion = "$Id: DateLongStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = Status.STABLE, since = "0.1.0" )
public class DateLongStringConverter implements StringConverter<Date>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An instance of this class.
     */
    public static final DateLongStringConverter INSTANCE = new DateLongStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code DateLongStringConverter}.
     */
    public DateLongStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Date fromString( final CharSequence source ) throws IllegalArgumentException
    {
        Date retValue = null;
        if( nonNull( source ) )
        {
            try
            {
                final var time = Long.parseLong( source.toString() );
                retValue = new Date( time );
            }
            catch( final NumberFormatException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidNumberFormat, source ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  Provides the subject class for this converter.
     *
     * @return The subject class.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<Class<?>> getSubjectClass() { return List.of( Date.class); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final Date source )
    {
        final var retValue = isNull( source ) ? null : Long.toString( source.getTime() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class DateLongStringConverter

/*
 *  End of File
 */