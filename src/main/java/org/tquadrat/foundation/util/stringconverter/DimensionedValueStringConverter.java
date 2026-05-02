/*
 * ============================================================================
 * Copyright © 2002-2026 by Thomas Thrien.
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

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.lang.value.Dimension;
import org.tquadrat.foundation.lang.value.DimensionedValue;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static java.util.Locale.ROOT;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;

/**
 *  <p>{@summary The abstract base class for implementations of
 *  {@link StringConverter}
 *  for dimensioned values.}</p>
 *  <p>The String representations for all dimensioned values have the same
 *  format: a numeric part followed by the unit for the dimension, separated by
 *  whitespace (one or more blanks). For example: <code>15&nbsp;m</code> or
 *  <code>16.0&nbsp;t</code>.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DimensionedValueStringConverter.java 1195 2026-04-15 21:33:40Z tquadrat $
 *  @since 0.25.3
 *
 *  @param  <D> The type for the dimension.
 *  @param  <V> The type for the dimensioned value.
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: DimensionedValueStringConverter.java 1195 2026-04-15 21:33:40Z tquadrat $" )
@API( status = STABLE, since = "0.25.3" )
public abstract class DimensionedValueStringConverter<D extends Dimension,V extends DimensionedValue<D>> implements StringConverter<V>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid value {@value}.
     */
    public static final String MSG_InvalidValue = "'%s' cannot be parsed as a dimensioned value";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The subject class for this converter.
     *
     *  @serial
     */
    private final Class<V> m_SubjectClass;

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
     *  Creates a new instance of {@code DimensionedValueStringConverter}.
     *
     *  @param  subjectClass    The subject class.
     */
    protected DimensionedValueStringConverter( final Class<V> subjectClass )
    {
        m_SubjectClass = subjectClass;
    }   //  DimensionedValueStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates an instance of
     *  {@link DimensionedValue}
     *  from the given arguments.
     *
     *  @param  number  The value.
     *  @param  dimension   The dimension.
     *  @return The dimensioned value.
     */
    protected abstract V createValue( BigDecimal number, D dimension );

    /**
     *  {@inheritDoc}
     */
    @Override
    public final V fromString( final CharSequence source ) throws IllegalArgumentException
    {
        V retValue = null;
        if( nonNull( source ) )
        {
            if( isEmptyOrBlank( source ) ) throw new IllegalArgumentException( format( MSG_InvalidValue, source ) );
            final var parts = source.toString().split( "\\s" );
            if( parts.length != 2 ) throw new IllegalArgumentException( format( MSG_InvalidValue, source ) );

            //---* Get the number *--------------------------------------------
            final var number = BigDecimalStringConverter.INSTANCE.fromString( parts [0] );

            //---* Get the dimension *-----------------------------------------
            final var dimension = unitFromSymbol( parts [1] );

            //---* Create the return value *-----------------------------------
            retValue = createValue( number, dimension );
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
    public final Collection<Class<V>> getSubjectClass() { return List.of( m_SubjectClass ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString( final V source )
    {
        final var retValue = isNull( source ) ? null : source.toString( ROOT,-1, -1 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  Returns a String representation of given value.<br>
     *  <br>The precision is applied to the numerical part only. The width
     *  includes the
     *  {@linkplain Dimension#unitSymbol() unit symbol}, too.
     *
     *  @param  source  The object to convert; can be {@code null}.
     *  @param  width   The minimum number of characters to be written to the
     *      output. If the length of the converted value is less than the width
     *      then the output will be padded by '&nbsp;' until the total number
     *      of characters equals width. The padding is at the beginning, as
     *      numerical values are usually right justified. If {@code width} is
     *      -1 then there is no minimum.
     *  @param  precision – The number of digits for the mantissa of the value.
     *      If {@code precision} is -1 then there is no explicit limit on the
     *      size of the mantissa.
     *  @return The String representation for this value.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public String toString( final V source, final int width, final int precision )
    {
        final var retValue = isNull( source ) ? null : source.toString( ROOT, width, precision );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  Determines the unit instance from the given unit symbol.
     *
     *  @param  symbol  The unit symbol.
     *  @return The unit instance.
     *  @throws IllegalArgumentException    The given unit symbol is unknown
     *      for the respective dimension.
     */
    protected abstract D unitFromSymbol( String symbol ) throws IllegalArgumentException;
}
//  class DimensionedValueStringConverter

/*
 *  End of File
 */