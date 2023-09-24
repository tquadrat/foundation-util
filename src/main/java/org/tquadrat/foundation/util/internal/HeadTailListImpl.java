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

package org.tquadrat.foundation.util.internal;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SIZED;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.Objects.hash;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.HeadTailList;

/**
 *  The implementation for the interface
 *  {@link HeadTailList}
 *
 *  @param  <T> The element type of the list.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: HeadTailListImpl.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.4
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: HeadTailListImpl.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.4" )
public final class HeadTailListImpl<T> implements HeadTailList<T>
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The implementation of the interface
     *  {@link Iterator}
     *  for instances of
     *  {@link HeadTailListImpl}
     *
     *  @param  <T> The element type of the list.
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: HeadTailListImpl.java 1032 2022-04-10 17:27:44Z tquadrat $
     *  @since 0.0.4
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: HeadTailListImpl.java 1032 2022-04-10 17:27:44Z tquadrat $" )
    @API( status = INTERNAL, since = "0.0.4" )
    private static class IteratorImpl<T> implements Iterator<T>
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The current list.
         */
        private HeadTailList<T> m_CurrentList;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code IteratorImpl} instance.
         *
         *  @param  list   The list to iterate.
         */
        @SuppressWarnings( "BoundedWildcard" )
        public IteratorImpl( final HeadTailList<T> list )
        {
            m_CurrentList = list;
        }   //  IteratorImpl()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final boolean hasNext() { return !m_CurrentList.isEmpty(); }

        /**
         *  {@inheritDoc}
         */
        @Override
        public T next()
        {
            if( m_CurrentList.isEmpty() ) throw new NoSuchElementException( "Empty List!" );

            @SuppressWarnings( "OptionalGetWithoutIsPresent" )
            final var retValue = m_CurrentList.head().get();
            m_CurrentList = m_CurrentList.tail();

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  next()
    }
    //  IteratorImpl()

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The hash code for this instance.
     */
    private final int m_HashCode;

    /**
     *  The head element.
     */
    private final T m_Head;

    /**
     *  The size of the list.
     */
    private final int m_Size;

    /**
     *  The tail list.
     */
    private final HeadTailList<T> m_Tail;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The empty list.
     */
    private static final HeadTailList<?> m_EmptyList = new HeadTailListImpl<>();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates the empty {@code HeadTailListImpl} instance.
     */
    private HeadTailListImpl()
    {
        m_Head = null;
        m_Tail = this;
        m_HashCode = 961;
        m_Size = 0;
    }   //  HeadTailListImpl()

    /**
     *  Creates a new {@code HeadTailListImpl} instance.
     *
     *  @param  head    The head element.
     *  @param  tail    The tail list.
     */
    private HeadTailListImpl( final T head, final HeadTailList<T> tail )
    {
        assert nonNull( head ) : "head is null";
        assert nonNull( tail ) : "tail is null";

        m_Head = head;
        m_Tail = tail;
        m_HashCode = hash( head, tail );
        m_Size = 1 + tail.size();
    }   //  HeadTailListImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final HeadTailList<T> add( final T element )
    {
        final var retValue = new HeadTailListImpl<>( requireNonNullArgument( element, "element" ), this );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  add()

    /**
     *  <p>{@summary Returns an empty list.}</p>
     *  <p>Each call to this method will return the same instance.</p>
     *
     *  @param  <E> The element type for the list.
     *  @return The empty list.
     */
    @SuppressWarnings( "unchecked" )
    public static final <E> HeadTailList<E> empty() { return (HeadTailList<E>) m_EmptyList; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean equals( final Object o )
    {
        var retValue = this == o;
        if( !retValue && (o instanceof final HeadTailList<?> other) )
        {
            retValue = (m_Size == other.size()) && (m_HashCode == other.hashCode());
            if( retValue )
            {
                //noinspection OptionalGetWithoutIsPresent
                retValue = m_Head.equals( other.head().get() )
                    && m_Tail.equals( other.tail() );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void forEachReverse( final Consumer<? super T> action )
    {
        internalForEachReverse( requireNonNullArgument( action, "action" ) );
    }   //  forEach()

    /**
     *  This is the implementation of
     *  {@link #forEachReverse(Consumer)};
     *  splitting this into two methods spares the null-check on each
     *  invocation.
     *
     *  @param  action  The action.
     */
    private final void internalForEachReverse( final Consumer<? super T> action )
    {
        if( nonNull( m_Head ) )
        {
            ((HeadTailListImpl<T>) m_Tail).internalForEachReverse( action );
            action.accept( m_Head );
        }
    }   //  _forEachReverse()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return m_HashCode; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<T> head() { return Optional.ofNullable( m_Head ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isEmpty() { return this == m_EmptyList; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Iterator<T> iterator()
    {
        final var retValue = new IteratorImpl<>( this );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  iterator()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int size() { return m_Size; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Spliterator<T> spliterator()
    {
        @SuppressWarnings( "ConstantExpression" )
        final var retValue = Spliterators.spliterator( iterator(), (long) m_Size, SIZED | NONNULL | IMMUTABLE );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  spliterator()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Stream<T> stream()
    {
        final var retValue = StreamSupport.stream( spliterator(), false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  stream()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final HeadTailList<T> tail() { return m_Tail; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Object [] toArray()
    {
        final var retValue = new Object [m_Size];
        var index = 0;
        for( final var t : this ) retValue [index++] = t;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toArray()

    /**
     *  {@inheritDoc}
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public final T [] toArray( final T [] target )
    {
        final var retValue = requireNonNullArgument( target, "target" ).length >= m_Size ? target : (T []) Array.newInstance( target.getClass().getComponentType(), m_Size );
        var index = 0;
        for( final var t : this ) retValue [index++] = t;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toArray()
}
//  class HeadTailListImpl

/*
 *  End of File
 */