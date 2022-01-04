/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

import static java.util.Collections.emptyIterator;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_Object_ARRAY;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.util.LazySet;

/**
 *  The implementation for
 *  {@link LazySet}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LazySetImpl.java 966 2022-01-04 22:28:49Z tquadrat $
 *  @since 0.0.5
 *
 *  @param  <E> The type of elements in this set.
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@ClassVersion( sourceVersion = "$Id: LazySetImpl.java 966 2022-01-04 22:28:49Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class LazySetImpl<E> implements LazySet<E>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The holder for the real set.
     */
    private final Lazy<? extends Set<E>> m_Holder;

    /**
     *  The flag that indicates whether the provided supplier will put values
     *  to the set on initialisation.
     */
    private final boolean m_SupplierPopulates;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LazySetImpl} instance.
     *
     *  @param  doPopulate  {@code true} if the provided supplier will put
     *      values to the set on initialisation, {@code false} if it will
     *      create an empty set.
     *  @param  supplier    The supplier that initialises the internal set
     *      for this instance when it is first needed.
     */
    public LazySetImpl( final boolean doPopulate, final Supplier<? extends Set<E>> supplier )
    {
        m_Holder = Lazy.use( supplier );
        m_SupplierPopulates = doPopulate;
    }   //  LazySetImpl()

    /**
     *  Creates a new {@code LazySetImpl} instance that is initialised with the
     *  given value.
     *
     *  @param  value   The initialisation value.
     */
    public LazySetImpl( final Set<E> value )
    {
        m_Holder = Lazy.of( value );
        m_SupplierPopulates = true;
    }   //  LazySetImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean add( final E e ) { return m_Holder.get().add( e ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean addAll( final Collection<? extends E> c )
    {
        final var retValue = !c.isEmpty() && m_Holder.get().addAll( c );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  addAll()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void clear()
    {
        if( m_SupplierPopulates ) init();
        m_Holder.ifPresent( Set::clear );
    }   //  clear()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean contains( final Object o )
    {
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().contains( o );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  contains()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean containsAll( final Collection<?> c )
    {
        java.util.Objects.requireNonNull( c );
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().containsAll( c );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  contains()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( {"rawtypes", "TypeMayBeWeakened", "preview"} )
    @Override
    public final boolean equals( final Object o )
    {
        var retValue = o == this;
        if( !retValue && (o instanceof Set other) )
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
    public final void forEach( final Consumer<? super E> action )
    {
        if( m_SupplierPopulates ) init();
        java.util.Objects.requireNonNull( action );
        m_Holder.ifPresent(s -> s.forEach( action ) );
    }   //  forEach()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return m_Holder.hashCode(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void ifPresent( final Consumer<? super Set<E>> consumer )
    {
        if( m_Holder.isPresent() ) requireNonNullArgument( consumer, "consumer" ).accept( m_Holder.get() );
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
    public final boolean isEmpty() { return (!m_Holder.isPresent() && !m_SupplierPopulates) || m_Holder.get().isEmpty(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isPresent() { return m_Holder.isPresent(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Iterator<E> iterator()
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.map( Set::iterator ).orElse( emptyIterator() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  iterator()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "unlikely-arg-type" )
    @Override
    public final boolean remove( final Object o ) { return (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().remove( o ); }

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "unlikely-arg-type" )
    @Override
    public final boolean removeAll( final Collection<?> c )
    {
        java.util.Objects.requireNonNull( c );
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().removeAll( c );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  removeAll()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean removeIf( final Predicate<? super E> filter )
    {
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().removeIf( filter );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  removeIf()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean retainAll( final Collection<?> c )
    {
        java.util.Objects.requireNonNull( c );
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().retainAll( c );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retainAll()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int size() { return (m_Holder.isPresent() || m_SupplierPopulates) ? m_Holder.get().size() : 0; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Spliterator<E> spliterator()
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.map( s -> Spliterators.spliterator( s, Spliterator.DISTINCT ) ).orElse( Spliterators.emptySpliterator() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  spliterator()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Object [] toArray()
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.map( Set::toArray ).orElse( EMPTY_Object_ARRAY );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toArray()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final <T> T [] toArray( final T [] a )
    {
        var retValue = a;
        if( m_Holder.isPresent() || m_SupplierPopulates )
        {
            retValue = m_Holder.get().toArray( a );
        }
        else
        {
            if( retValue.length > 0 ) //noinspection AssignmentToNull
                retValue [0] = null;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toArray()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) ? m_Holder.getAsString() : "[Not initialized]";

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class LazySetImpl

/*
 *  End of File
 */