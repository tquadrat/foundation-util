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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.NULL_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary An implementation of
 *  {@link org.tquadrat.foundation.lang.StringConverter}
 *  for arbitrary instances of
 *  {@link List }
 *  implementations.}</p>
 *  <p>The output of
 *  {@link #toString(List)}
 *  is quite different from that of a
 *  {@link Object#toString() toString()}
 *  method from one of the {@code List} implementations, and not really meant
 *  for human readers.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ListStringConverter.java 1079 2023-10-22 17:44:34Z tquadrat $
 *  @since 0.3.0
 *
 *  @UMLGraph.link
 *
 *  @param  <E> The element type of the list to convert to a String.
 */
@ClassVersion( sourceVersion = "$Id: ListStringConverter.java 1079 2023-10-22 17:44:34Z tquadrat $" )
@API( status = STABLE, since = "0.3.0" )
public class ListStringConverter<E> implements StringConverter<List<E>>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The pattern that is used to identify an entry: {@value}.
     */
    public static final String PATTERN = "\\{\\{(.*?)\\}\\}";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The instance of
     *  {@link StringConverter}
     *  that is used for the elements of the list.
     *
     *  @serial
     */
    private final StringConverter<E> m_ElementStringConverter;

    /**
     *  The factory for the list that is used by
     *  {@link #fromString(CharSequence)}.
     *
     *  @serial
     */
    private final Supplier<List<E>> m_Factory;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The pattern that is used to identify an entry.
     */
    private static final Pattern m_Pattern;

    static
    {
        try
        {
            m_Pattern = Pattern.compile( PATTERN );
        }
        catch( final PatternSyntaxException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code ListStringConverter}.
     *
     *  @param  converter   The instance of
     *      {@link StringConverter}
     *      that is used for the elements of the list.
     *  @param  factory The factory for the list that is used by
     *      {@link #fromString(CharSequence)}.
     */
    public ListStringConverter( final StringConverter<E> converter, final Supplier<List<E>> factory )
    {
        m_ElementStringConverter = requireNonNullArgument( converter, "converter" );
        m_Factory = requireNonNullArgument( factory, "factory" );
    }   //  ListStringConverter()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<E> fromString( final CharSequence source ) throws IllegalArgumentException
    {
        List<E> retValue = null;
        if( nonNull( source ) )
        {
            retValue = m_Factory.get();
            if( !source.isEmpty() )
            {
                if( source.toString().startsWith( "[" ) && source.toString().endsWith( "]" ) )
                {
                    final var matcher = m_Pattern.matcher( source );
                    while( matcher.find() )
                    {
                        retValue.add( m_ElementStringConverter.fromString( matcher.group(1) ) );
                    }
                }
                else
                {
                    retValue.add( m_ElementStringConverter.fromString( source ) );
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final List<E> source )
    {
        String retValue = null;
        if( nonNull( source ) )
        {
            final var buffer = new StringJoiner( "}},{{", "[{{", "}}]" );
            buffer.setEmptyValue( "[]" );
            for( final var element : source )
            {
                if( isNull( element ) )
                {
                    buffer.add( NULL_STRING );
                }
                else
                {
                    buffer.add( m_ElementStringConverter.toString( element ) );
                }
            }

            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ListStringConverter

/*
 *  End of File
 */