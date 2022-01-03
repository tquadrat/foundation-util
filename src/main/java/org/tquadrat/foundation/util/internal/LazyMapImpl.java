/*
 * ============================================================================
 * Copyright © 2002-20120 by Thomas Thrien.
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

import static java.util.Collections.emptySet;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.util.LazyMap;

/**
 *  The implementation for
 *  {@link LazyMap}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LazyMapImpl.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @param <K> The type of keys maintained by this map
 *  @param <V> The type of mapped values
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "preview" )
@ClassVersion( sourceVersion = "$Id: LazyMapImpl.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public sealed class LazyMapImpl<K,V> implements LazyMap<K,V>
    permits LazySortedMapImpl
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The holder for the real map.
     */
    private final Lazy<? extends Map<K,V>> m_Holder;

    /**
     *  The flag that indicates whether the provided supplier will put values
     *  to the list on initialisation.
     */
    private final boolean m_SupplierPopulates;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LazyMapImpl} instance.
     *
     *  @param  doPopulate  {@code true} if the provided supplier will put
     *      values to the map on initialisation, {@code false} if will create
     *      an empty map.
     *  @param  supplier    The supplier that initialises the internal map for
     *      this instance when it is first needed.
     */
    public LazyMapImpl( final boolean doPopulate, final Supplier<? extends Map<K,V>> supplier )
    {
        m_SupplierPopulates = doPopulate;
        m_Holder = Lazy.use( supplier );
    }   //  LazyMapImpl()

    /**
     *  Creates a new {@code LazyMapImpl} instance that is initialised with the
     *  given value.
     *
     *  @param  value   The initialisation value.
     */
    public LazyMapImpl( final Map<K,V> value )
    {
        m_SupplierPopulates = true;
        m_Holder = Lazy.of( value );
    }   //  LazyMapImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void clear()
    {
        if( m_SupplierPopulates ) init();
        m_Holder.ifPresent( Map::clear );
    }   //  clear()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean containsKey( final Object key )
    {
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().containsKey( key );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  containsKey()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean containsValue( final Object value )
    {
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().containsValue( value );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  containsValue()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<Entry<K,V>> entrySet()
    {
        if( m_SupplierPopulates ) init();
        @SuppressWarnings( "unchecked" )
        final var retValue = m_Holder.map( Map::entrySet ).orElse( emptySet() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  entrySet()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "rawtypes" )
    @Override
    public final boolean equals( final Object o )
    {
        var retValue = o == this;
        if( !retValue && (o instanceof Map other) )
        {
            /*
             * Collections are equal if their contents is equal. Refer to the
             * respective Javadoc for equals().
             */
            if( isPresent() || m_SupplierPopulates )
            {
                retValue = m_Holder.get().equals( other );
            }
            else
            {
                retValue = other.isEmpty();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final V get( final Object key )
    {
        if( m_SupplierPopulates ) init();
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = m_Holder.map( m -> m.get( key ) ).orElse( null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  get()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return m_Holder.hashCode(); }

    /**
     *  Gives access to the internal
     *  {@link Lazy}
     *  instance for derived classes.
     *
     *  @return A reference to
     *      {@link #m_Holder}.
     */
    /*
     * Not final, because it will be overridden by LazySortedMap.
     */
    protected Lazy<? extends Map<K,V>> holder() { return m_Holder; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void ifPresent( final Consumer<? super Map<K,V>> consumer )
    {
        //noinspection FunctionalExpressionCanBeFolded
        m_Holder.ifPresent( requireNonNullArgument( consumer, "consumer" )::accept );
    }   //  ifPresent()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void init() { m_Holder.get(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isEmpty()
    {
        final var retValue = (!m_Holder.isPresent() && !m_SupplierPopulates) || m_Holder.get().isEmpty();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isEmpty()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isPresent() { return m_Holder.isPresent(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<K> keySet()
    {
        if( m_SupplierPopulates ) init();
        @SuppressWarnings( "unchecked" )
        final var retValue = m_Holder.map( Map::keySet ).orElse( emptySet() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  keySet()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final V put( final K key, final V value )
    {
        final var retValue = m_Holder.get().put( key, value );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  put()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void putAll( final Map<? extends K,? extends V> map ) { m_Holder.get().putAll( map ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final V remove( final Object key )
    {
        if( m_SupplierPopulates ) init();
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = m_Holder.map( m -> m.remove( key ) ).orElse( null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  remove()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int size()
    {
        final var retValue = m_Holder.isPresent() || m_SupplierPopulates ? m_Holder.get().size() : 0;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  size()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var retValue = m_Holder.isPresent() || m_SupplierPopulates ? m_Holder.getAsString() : "[Not initialized]";

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<V> values()
    {
        if( m_SupplierPopulates ) init();
        @SuppressWarnings( "unchecked" )
        final var retValue = m_Holder.map( Map::values ).orElse( emptySet() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  values()
}
//  class LazyMapImpl

/*
 *  End of File
 */