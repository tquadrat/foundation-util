/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
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

package org.tquadrat.foundation.util.internal;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.io.Serial;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Pair;
import org.tquadrat.foundation.util.RangeMap;

/**
 *  The implementation of the interface
 *  {@link RangeMap}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: RangeMapImpl.java 995 2022-01-23 01:09:35Z tquadrat $
 *  @since 0.0.7
 *
 *  @param <T>  The type of the mapped value.
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: RangeMapImpl.java 995 2022-01-23 01:09:35Z tquadrat $" )
@API( status = STABLE, since = "0.0.7" )
public sealed class RangeMapImpl<T> implements RangeMap<T>
    permits FinalRangeMap
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  This flag determines if the limit belongs to the range or not.
     */
    private boolean m_Includes = false;

    /**
     *  The Ranges.
     */
    private final Set<Pair<Double, ? extends T>> m_Ranges = new TreeSet<>( Comparator.comparing( Pair::left ) );

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = -1094863662311849594L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code RangeMap} instance. For this instance, the limit
     *  does not belong to the range.
     *
     *  @see #RangeMapImpl(boolean)
     */
    public RangeMapImpl() { this( false ); }

    /**
     *  Creates a new {@code RangeMap} instance.
     *
     *  @param  includes    {@code true} if the limit belongs to the
     *      range, {@code false} otherwise.
     */
    public RangeMapImpl( final boolean includes )
    {
        m_Includes = includes;
    }   //  RangeMap()

    /**
     *  Creates a new {@code RangeMap} instance from an existing one.
     *
     *  @param  other   The other instance.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public RangeMapImpl( final RangeMapImpl<? extends T> other )
    {
        m_Includes = requireNonNullArgument( other, "other" ).m_Includes;
        m_Ranges.addAll( other.m_Ranges );
    }   //  RangeMap()

    /**
     *  Creates a new {@code RangeMap} instance from an instance of
     *  {@link Map}.
     *
     *  @param  includes    {@code true} if the limit belongs to the
     *      range, {@code false} otherwise.
     *  @param  map   The map instance.
     */
    public RangeMapImpl( final boolean includes, final Map<? extends Number,? extends T> map )
    {
        this( includes );
        requireNonNullArgument( map, "map" )
            .forEach( ( key, value ) -> m_Ranges.add( new Pair<>( Double.valueOf( key.doubleValue() ), value ) ) );
    }   //  RangeMap()

    /**
     *  Creates a new {@code RangeMap} instance from a list of
     *  pairs.
     *
     *  @param  includes    {@code true} if the limit belongs to the
     *      range, {@code false} otherwise.
     *  @param  nvpList The list of entries.
     */
    public RangeMapImpl( final boolean includes, final Collection<? extends Map.Entry<? extends Number, ? extends T>> nvpList )
    {
        this( includes );
        requireNonNullArgument( nvpList, "nvpList" ).forEach( e -> m_Ranges.add( new Pair<>( Double.valueOf( e.getKey().doubleValue() ), e.getValue() ) ) );
    }   //  RangeMap()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public RangeMapImpl<T> addRange( final double key, final T value )
    {
        m_Ranges.add( new Pair<>( Double.valueOf( key ), requireNonNullArgument( value, "value" ) ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addRange()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void clear() { m_Ranges.clear(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final RangeMap<T> copy( final boolean modifiable )
    {
        final var retValue = modifiable ? new RangeMapImpl<>( this ) : new FinalRangeMap<>( this );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  copy()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Pair<Double,T> [] entries()
    {
        @SuppressWarnings( "unchecked" )
        final Pair<Double,T> [] retValue = m_Ranges.toArray( Pair []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  entries()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final T get( final double key )
    {
        if( m_Ranges.isEmpty() ) throw new IllegalStateException( "RangeMap does not have any entries" );

        final var iterator = m_Ranges.iterator();
        T retValue;
        Pair<Double,? extends T> entry;
        do
        {
            entry = iterator.next();
            retValue = entry.right();
        }
        while( (m_Includes ? entry.left() < key : entry.left() <= key) && iterator.hasNext() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  get()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isEmpty() { return m_Ranges.isEmpty(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public RangeMap<T> removeRange( final double key )
    {
        m_Ranges.remove( new Pair<>( Double.valueOf( key ), (T) null ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  removeRange()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public RangeMapImpl<T> setDefault( final T defaultValue )
    {
        final var entry = new Pair<>( Double.valueOf( Double.MAX_VALUE ), requireNonNullArgument( defaultValue, "defaultValue" ) );
        if( !m_Ranges.add( entry ) )
        {
            m_Ranges.remove( entry );
            m_Ranges.add( entry );
        }

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setDefault()

    /**
     *  Returns an immutable copy of the given
     *  {@link RangeMapImpl}.
     *
     *  @param  <T> The type of the mapped value.
     *  @param  other   The other Range map.
     *  @return The immutable copy.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public static <T> RangeMapImpl<T> unmodifiableRangeMap( final RangeMapImpl<? extends T> other )
    {
        final RangeMapImpl<T> retValue = new FinalRangeMap<>( requireNonNullArgument( other, "other" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  unmodifiableRangeMap()
}
//  class RangeMapImpl

/*
 *  End of File
 */