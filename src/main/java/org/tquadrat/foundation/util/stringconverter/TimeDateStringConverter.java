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
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.MountPoint;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary The abstract base class for implementations of
 *  {@link StringConverter}
 *  for types that extend
 *  {@link Temporal}.}</p>
 *  <p>The format for the date/time data can be modified by applying an
 *  instance of
 *  {@link java.time.format.DateTimeFormatter}
 *  to the constructor
 *  {@link #TimeDateStringConverter(Class,DateTimeFormatter)}
 *  that is used for parsing Strings to object instances and for converting
 *  object instances to Strings.</p>
 *
 *  @param  <T> The type that is handled by this class.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TimeDateStringConverter.java 1003 2022-02-02 11:07:25Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 *
 *  @see DateTimeFormatter
 */
@SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
@ClassVersion( sourceVersion = "$Id: TimeDateStringConverter.java 1003 2022-02-02 11:07:25Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public abstract class TimeDateStringConverter<T extends Temporal> implements StringConverter<T>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid date/time on the command line: {@value}.
     */
    public static final String MSG_InvalidDateTimeFormat = "'%1$s' cannot be parsed as a valid date/time";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The formatter that is used to format the date/time data.
     */
    private final transient Optional<DateTimeFormatter> m_Formatter;

    /**
     *  The subject class for this converter.
     */
    private final Class<T> m_SubjectClass;

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

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code TimeStringConverter} instance.
     *
     *  @param   subjectClass    The subject class.
     */
    protected TimeDateStringConverter( final Class<T> subjectClass ) { this( subjectClass, Optional.empty()); }

    /**
     *  Creates a new {@code TimeStringConverter} instance that uses the given
     *  formatter for the conversion back and forth.
     *
     *  @note The formatter may not drop any part of the temporal data,
     *      otherwise
     *      {@link #fromString(CharSequence)}
     *      may fail. This means that the formatter is only allowed to re-order
     *      the temporal fields.
     *
     *  @param  subjectClass    The subject class.
     *  @param  formatter   The formatter for the date/time data.
     */
    protected TimeDateStringConverter( final Class<T> subjectClass, final DateTimeFormatter formatter )
    {
        this( subjectClass, Optional.of( requireNonNullArgument( formatter, "formatter" ) ) );
    }   //  TimeDateStringConverter()

    /**
     *  Creates a new {@code TimeStringConverter} instance.
     *
     *  @param  subjectClass    The subject class.
     *  @param  formatter   The formatter for the date/time data.
     */
    private TimeDateStringConverter( final Class<T> subjectClass, final Optional<DateTimeFormatter> formatter )
    {
        m_SubjectClass = subjectClass;
        m_Formatter = formatter;
    }   //  TimeDateStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final T fromString( final CharSequence source ) throws IllegalArgumentException
    {
        T retValue = null;
        if( nonNull( source ) )
        {
            if( isEmptyOrBlank( source ) ) throw new IllegalArgumentException( format( MSG_InvalidDateTimeFormat, source ) );
            try
            {
                retValue = parseDateTime( source, m_Formatter );
            }
            catch( final DateTimeParseException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidDateTimeFormat, source ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  Provides the subject class for this converter.
     *
     *  @return The subject class.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<Class<T>> getSubjectClass() { return List.of( m_SubjectClass ); }

    /**
     *  Parses the given String to an instance of
     *  {@link Temporal}.
     *  The caller ensures that {@code source} is not {@code null}, not the
     *  empty String and does not contain only whitespace.
     *
     *  @param  source  The String to parse.
     *  @param  formatter   The formatter for parsing the String
     *  @return The time/date value.
     *  @throws DateTimeParseException  The given value cannot be parsed to a
     *      {@code Temporal}.
     */
    @MountPoint
    protected abstract T parseDateTime( CharSequence source, Optional<DateTimeFormatter> formatter ) throws DateTimeParseException;

    /**
     *  Loads a previously serialised instance of this class from the given
     *  input stream.
     *
     *  @param  in  The input stream.
     *  @throws IOException The de-serialisation failed.
     *  @throws ClassNotFoundException  A class could not be found.
     */
    @Serial
    @SuppressWarnings( "static-method" )
    private final void readObject( final ObjectInputStream in ) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }   //  readObject()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final T source )
    {
        final var retValue = isNull( source ) ? null : m_Formatter.map( formatter -> formatter.format( source ) ).orElse( source.toString() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  Writes a serialised instance of this class to the given output stream.
     *  This fails if a
     *  {@link DateTimeFormatter}
     *  instance was assigned to this instance.
     *
     *  @param  out The output stream.
     *  @throws IOException A {@code DateTimeFormatter} was assigned to this
     *      instance.
     */
    @Serial
    private final void writeObject( final ObjectOutputStream out) throws IOException
    {
        if( m_Formatter.isPresent() ) throw new IOException( format( "Cannot serialize instance of '%s' with DateTimeFormatter set", getClass().getName() ) );
        out.defaultWriteObject();
    }   //  writeObject()
}
//  class TimeDateStringConverter

/*
 *  End of File
 */