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
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyListIterator;
import static java.util.Spliterators.emptySpliterator;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_Object_ARRAY;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.util.LazyList;

/**
 *  The implementation for
 *  {@link LazyList}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LazyListImpl.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @param  <E> The type of elements in this list.
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "OverlyComplexClass"} )
@ClassVersion( sourceVersion = "$Id: LazyListImpl.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class LazyListImpl<E> implements LazyList<E>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The holder for the real list.
     */
    private final Lazy<? extends List<E>> m_Holder;

    /**
     *  The flag that indicates whether the provided supplier will put values
     *  to the list on initialisation.
     */
    private final boolean m_SupplierPopulates;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LazyListImpl} instance.
     *
     *  @param  doPopulate  {@code true} if the provided supplier will put
     *      values to the list on initialisation, {@code false} if will create
     *      an empty list.
     *  @param  supplier    The supplier that initialises the internal list
     *      for this instance when it is first needed.
     */
    public LazyListImpl( final boolean doPopulate, final Supplier<? extends List<E>> supplier )
    {
        m_Holder = Lazy.use( supplier );
        m_SupplierPopulates = doPopulate;
    }   //  LazyListImpl()

    /**
     *  Creates a new {@code LazyListImpl} instance that is initialised with the
     *  given value.
     *
     *  @param  value   The initialisation value.
     */
    public LazyListImpl( final List<E> value )
    {
        m_Holder = Lazy.of( value );
        m_SupplierPopulates = true;  // … although this is redundant …
    }   //  LazyListImpl()

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
    public final void add( final int index, final E element )
    {
        if( !isPresent() && !m_SupplierPopulates && (index != 0) ) throw new IndexOutOfBoundsException( index );

        m_Holder.get().add( index, element );
    }   //  add()

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
    public final boolean addAll( final int index, final Collection<? extends E> c )
    {
        final var retValue = !c.isEmpty() && m_Holder.get().addAll( index, c );

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
        m_Holder.ifPresent( List::clear );
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
        if( !retValue && (o instanceof List other) )
        {
            /*
             * Refer to the Javadoc for List::equals: Lists are considered
             * equal if they have the same contents.
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
        m_Holder.ifPresent( l -> l.forEach( action ) );
    }   //  forEach()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final E get( final int index )
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.orElseThrow( () -> new IndexOutOfBoundsException( index ) ).get( index );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  get()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void ifPresent( final Consumer<? super List<E>> consumer )
    {
        if( m_SupplierPopulates ) init();
        //noinspection FunctionalExpressionCanBeFolded
        m_Holder.ifPresent( requireNonNullArgument( consumer, "consumer" )::accept );
    }   //  ifPresent()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int indexOf( final Object o )
    {
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = m_Holder.isPresent() || m_SupplierPopulates ? m_Holder.get().indexOf( o ) : -1;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  indexOf()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return m_Holder.hashCode(); }

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
    public final Iterator<E> iterator()
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.map( List::iterator ).orElse( emptyIterator() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  iterator()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int lastIndexOf( final Object o )
    {
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = m_Holder.isPresent() || m_SupplierPopulates ? m_Holder.get().lastIndexOf( o ) : -1;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  lastIndexOf()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final ListIterator<E> listIterator()
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.map( List::listIterator ).orElse( emptyListIterator() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  listIterator()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final ListIterator<E> listIterator( final int index )
    {
        if( m_SupplierPopulates ) init();
        final var retValue = index == 0 ? listIterator() : m_Holder.orElseThrow( () -> new IndexOutOfBoundsException( index ) ).listIterator( index );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  listIterator()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "unlikely-arg-type" )
    @Override
    public final boolean remove( final Object o )
    {
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().remove( o );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  remove()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final E remove( final int index )
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.orElseThrow( () -> new IndexOutOfBoundsException( index ) ).remove( index );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  remove()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "unlikely-arg-type" )
    @Override
    public final boolean removeAll( final Collection<?> c )
    {
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
    public final void replaceAll( final UnaryOperator<E> operator )
    {
        if( m_SupplierPopulates ) init();
        m_Holder.ifPresent( l -> l.replaceAll( operator ) );
    }   //  replaceAll()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean retainAll( final Collection<?> c )
    {
        @SuppressWarnings( "unlikely-arg-type" )
        final var retValue = (m_Holder.isPresent() || m_SupplierPopulates) && m_Holder.get().retainAll( c );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retainAll()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final E set( final int index, final E element )
    {
        if( m_SupplierPopulates ) init();
        final var retValue =  m_Holder.orElseThrow( () -> new IndexOutOfBoundsException( index ) ).set( index, element );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  set()

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
    public final void sort( final Comparator<? super E> c )
    {
        if( m_SupplierPopulates ) init();
        m_Holder.ifPresent( l -> l.sort( c ) );
    }   //  sort()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Spliterator<E> spliterator()
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.map( List::spliterator ).orElse( emptySpliterator() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  spliterator()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final List<E> subList( final int fromIndex, final int toIndex )
    {
        /*
         * From the description of List.subList(): "Returns a view of the
         * portion of this list between the specified fromIndex, inclusive, and
         * toIndex, exclusive. (If fromIndex and toIndex are equal, the
         * returned list is empty.)
         *
         * The implementation in AbstractList.subList() uses a method
         * subListRangeCheck() for the validation of the index values; that
         * method is implemented like this:
         *
         * static void subListRangeCheck( int fromIndex, int toIndex, int size)
         * {
         *     if( fromIndex < 0 ) throw new IndexOutOfBoundsException( "fromIndex = " + fromIndex );
         *     if( toIndex > size ) throw new IndexOutOfBoundsException( "toIndex = " + toIndex );
         *     if( fromIndex > toIndex ) throw new IllegalArgumentException( "fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")" );
         * }
         *
         * From this, the sublist of an empty list is a empty list, if the
         * fromIndex and the toIndex are both 0.
         */
        final List<E> retValue;
        if( (fromIndex == toIndex) && (toIndex == 0) )
        {
            retValue = emptyList();
        }
        else
        {
            if( m_SupplierPopulates ) init();
            retValue = m_Holder.orElseThrow( IndexOutOfBoundsException::new ).subList( fromIndex, toIndex );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  subList()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Object [] toArray()
    {
        if( m_SupplierPopulates ) init();
        final var retValue = m_Holder.map( List::toArray ).orElse( EMPTY_Object_ARRAY );

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
            if( retValue.length > 0 )
            {
                //noinspection AssignmentToNull
                retValue [0] = null;
            }
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
        final var retValue = m_Holder.isPresent() || m_SupplierPopulates ? m_Holder.getAsString() : "[Not initialized]";

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class LazyListImpl

/*
 *  End of File
 */