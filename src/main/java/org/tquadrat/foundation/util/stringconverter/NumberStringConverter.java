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
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The base class for implementations of
 *  {@link StringConverter}
 *  for types that extend
 *  {@link java.lang.Number}.
 *
 *  @param  <T> The type that is handled by this class.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NumberStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: NumberStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public abstract sealed class NumberStringConverter<T extends Number> implements StringConverter<T>
    permits org.tquadrat.foundation.util.stringconverter.BigDecimalStringConverter,
        org.tquadrat.foundation.util.stringconverter.BigIntegerStringConverter,
        org.tquadrat.foundation.util.stringconverter.ByteStringConverter,
        org.tquadrat.foundation.util.stringconverter.DoubleStringConverter,
        org.tquadrat.foundation.util.stringconverter.FloatStringConverter,
        org.tquadrat.foundation.util.stringconverter.IntegerStringConverter,
        org.tquadrat.foundation.util.stringconverter.LongStringConverter,
        org.tquadrat.foundation.util.stringconverter.ShortStringConverter
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for a String argument to
     *  {@link #fromString(CharSequence)}
     *  that cannot be parsed to a proper number value: {@value}.
     */
    public static final String MSG_InvalidNumberFormat = "'%1$s' cannot be parsed as a valid number";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The subject classes for this converter.
     *
     *  @serial
     */
    private final Collection<Class<?>> m_SubjectClasses;

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
     *  Creates a new instance of {@code NumberStringConverter}.
     *
     *  @param  subjectClasses  The subject classes.
     */
    protected NumberStringConverter( final Class<?>... subjectClasses )
    {
        m_SubjectClasses = List.of( subjectClasses );
    }   //  NumberStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Parses the given String to a number. The String is not {@code null},
     *  not empty, and it will not contain blanks only. Leading or trailing
     *  blanks have been cut off.
     *
     *  @param  value   The String to parse.
     *  @return The number.
     *  @throws NumberFormatException   The given value cannot be parsed to a
     *      number.
     */
    protected abstract T parseNumber( String value ) throws NumberFormatException;

    /**
     *  {@inheritDoc}
     */
    @Override
    public final T fromString( final CharSequence source ) throws IllegalArgumentException
    {
        T retValue = null;
        if( nonNull( source ) )
        {
            if( isEmptyOrBlank( source ) )
            {
                throw new IllegalArgumentException( format( MSG_InvalidNumberFormat, source ) );
            }
            try
            {
                retValue = parseNumber( source.toString().trim() );
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
    public final Collection<Class<?>> getSubjectClass() { return m_SubjectClasses; }
}
//  class NumberStringConverter

/*
 *  End of File
 */