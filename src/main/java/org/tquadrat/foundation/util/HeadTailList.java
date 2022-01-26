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

package org.tquadrat.foundation.util;

import static java.util.Collections.reverse;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.internal.HeadTailListImpl;

/**
 *  <p>{@summary A {@code HeadTailList} is an unmodifiable list data
 *  structure}.</p>
 *  <p>Each modifying operation on it will create a new instance <i>of the
 *  list</i>, leaving the original list one unchanged.</p>
 *  <p>But only the list itself is copied, not the entries in it – these are
 *  shared amongst all copies of the list. Consequently changes to the data
 *  stored in the list should be avoided.</p>
 *  <p>To create a new instance of {@code HeadTailList}, call one of</p>
 *  <ul>
 *      <li>{@link #empty()} for an empty list</li>
 *      <li>{@link #from(Object[])} to initialise the list with some
 *          elements</li>
 *      <li>{@link #from(Collection)} to initialise the new list from a
 *          {@link Collection}</li>
 *  </ul>
 *
 *  @param  <T> The element type of the list.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: HeadTailList.java 995 2022-01-23 01:09:35Z tquadrat $
 *  @since 0.0.4
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@ClassVersion( sourceVersion = "$Id: HeadTailList.java 995 2022-01-23 01:09:35Z tquadrat $" )
@API( status = STABLE, since = "0.0.4" )
public sealed interface HeadTailList<T> extends Iterable<T>
    permits HeadTailListImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns a new list with the given element as the head, and this list as
     *  the tail.
     *
     *  @param  element The head for the new list.
     *  @return The new list.
     */
    public HeadTailList<T> add( final T element );

    /**
     *  Returns a new list where the given list is appended to the end of this
     *  list.
     *
     *  @param  list    The list to append.
     *  @return The new list.
     */
    public default HeadTailList<T> append( final HeadTailList<T> list )
    {
        final var retValue = requireNonNullArgument( list, "list" ).prepend( this );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  append()

    /**
     *  Returns a new list where the given list is appended to the end of this
     *  list.
     *
     *  @param  list    The list to append.
     *  @return The new list.
     */
    public default HeadTailList<T> append( final Collection<T> list )
    {
        final var retValue = from( requireNonNullArgument( list, "list" ) ).prepend( this );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  append()

    /**
     *  Checks whether the list contains an element that is equal to the given
     *  one.
     *
     *  @param  element The element to look for.
     *  @return {@code true} if the element is in the list, {@code false}
     *      otherwise.
     */
    public default boolean contains( final T element )
    {
        var retValue = false;
        if( nonNull( element ) )
        {
            //noinspection ForLoopWithMissingComponent
            for( final var i = iterator(); i.hasNext() && !retValue; ) retValue = i.next().equals( element );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  contains()

    /**
     *  <p>{@summary Returns an empty list.}</p>
     *  <p>Each call to this method will return the same instance.</p>
     *
     *  @param  <E> The element type for the list.
     *  @return The empty list.
     */
    public static <E> HeadTailList<E> empty() { return HeadTailListImpl.empty(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean equals( Object o );

    /**
     *  Does the same as
     *  {@link #forEach(Consumer)}, but starting with last element first.
     *
     *  @param  action  The action to perform.
     */
    public void forEachReverse( final Consumer<? super T> action );

    /**
     *  Creates a new list from the given
     *  {@link Collection}.
     *
     *  @param  <E> The element type for the list.
     *  @param  source  The collection.
     *  @return The new list.
     */
    public static <E> HeadTailList<E> from( final Collection<E> source )
    {
        HeadTailList<E> retValue = empty();
        if( !requireNonNullArgument( source, "source" ).isEmpty() )
        {
            final var list = new ArrayList<>( source );
            reverse( list );
            for( final var e : list ) retValue = retValue.add( e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  from()

    /**
     *  Creates a new list from the given elements.
     *
     *  @param  <E> The element type for the list.
     *  @param  elements    The elements.
     *  @return The new list.
     */
    public static <E> HeadTailList<E> from( final E... elements )
    {
        HeadTailList<E> retValue = empty();
        for( var i = requireNonNullArgument( elements, "elements" ).length; i > 0; --i )
        {
            retValue = retValue.add( elements [i - 1] );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  from()

    /**
     *  <p>{@summary Returns the element that is identified by the given
     *  index}</p>
     *  <p>The index is in reverse order of the insertion sequence; this means
     *  tha the element added first has the highest index value
     *  (<code>size()&nbsp;-&nbsp;1</code>).</p>
     *
     *  @param  index   The index for the wanted element; 0 indicates the head.
     *  @return The element with the given index.
     *  @throws IndexOutOfBoundsException   The given index is out of the range
     *      <code>index&nbsp;&lt;&nbsp;0&nbsp;||&nbsp;index&nbsp;&gt;=&nbsp;size()</code>.
     */
    public default T get( final int index ) throws IndexOutOfBoundsException
    {
        if( head().isEmpty() || (index < 0) ) throw new IndexOutOfBoundsException( Integer.toString( index ) );
        final var retValue = index == 0 ? head().get() : tail().get( index - 1 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  get()

    /**
     *  {@inheritDoc}
     */
    @Override
    public int hashCode();

    /**
     *  <p>{@summary Returns the head of the list.}</p>
     *  <p>This will be
     *  {@linkplain Optional#empty() empty}
     *  only for the
     *  {@linkplain #empty() empty list}.</p>
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the head.
     */
    public Optional<T> head();

    /**
     *  Checks whether the list is empty.
     *
     *  @return {@code true} if the list is empty, {@code false} otherwise.
     */
    public boolean isEmpty();

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "AbstractMethodOverridesAbstractMethod" )
    @Override
    public Iterator<T> iterator();

    /**
     *  <p>{@summary Returns a new list where the given list is merged into
     *  this list.}</p>
     *  <p>This means that each element from this list is followed by an
     *  element of the other list; if one list is shorter than the other one,
     *  the elements of the longer one will just be appended to the end of the
     *  new list.</p>
     *
     *  @param  list    The list to merge into this one.
     *  @return The new list.
     */
    public default HeadTailList<T> merge( final HeadTailList<? extends T> list )
    {
        @SuppressWarnings( "unchecked" )
        var retValue = (HeadTailList<T>) empty();
        final var j = requireNonNullArgument( list, "list" ).revert().iterator();
        final var i = revert().iterator();
        while( i.hasNext() && j.hasNext() )
        {
            retValue = retValue.add( i.next() ).add( j.next() );
        }
        while( i.hasNext() ) retValue = retValue.add( i.next() );
        while( j.hasNext() ) retValue = retValue.add( j.next() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  merge()

    /**
     *  Returns a new list where this list is appended to the end of the given
     *  list.
     *
     *  @param  list    The list to prepend.
     *  @return The new list.
     */
    public default HeadTailList<T> prepend( final HeadTailList<T> list )
    {
        final var arg = requireNonNullArgument( list, "list" );
        var retValue = this;
        for( final var e : arg.revert() ) retValue = retValue.add( e );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  prepend()

    /**
     *  Returns a new list where the entries of the given list are placed
     *  before the first entry of this list.
     *
     *  @param  list    The list to prepend.
     *  @return The new list.
     */
    public default HeadTailList<T> prepend( final Collection<T> list )
    {
        var retValue = this;
        if( !requireNonNullArgument( list, "list" ).isEmpty() )
        {
            final var l = new ArrayList<>( list );
            reverse( l );
            for( final var e : l ) retValue = retValue.add( e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  prepend()

    /**
     *  Returns a copy of this list that does not contain the given element.
     *
     *  @param  element The element to be removed.
     *  @return The copy of the list.
     */
    public default HeadTailList<T> remove( final T element )
    {
        var retValue = this;
        if( nonNull( element ) )
        {
            retValue = empty();
            for( final var e : revert() )
            {
                if( !e.equals( element ) ) retValue = retValue.add( e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  remove()

    /**
     *  Returns a new copy of this list where the head element is replaced by
     *  the given element.
     *
     *  @param  newHead The element that is the new head for the list.
     *  @return The copy with the new head.
     */
    public default HeadTailList<T> replaceHead( final T newHead )
    {
        final var retValue = tail().add( requireNonNullArgument( newHead, "newHead" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceHead()

    /**
     *  Revert the list; that means that the sequence of the elements in the
     *  new list is in reverse order that in this one.
     *
     *  @return The new list with the reverted order of the elements.
     */
    public default HeadTailList<T> revert()
    {
        @SuppressWarnings( "unchecked" )
        var retValue = (HeadTailList<T>) empty();
        for( final var e : this ) retValue = retValue.add( e );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  revert()

    /**
     *  Returns the size of the list.
     *
     *  @return The size of the list.
     */
    public int size();

    /**
     *  Returns a
     *  {@link Stream}
     *  that is backed by this list.
     *
     *  @return The stream.
     */
    public Stream<T> stream();

    /**
     *  Returns the tail of the list.
     *
     *  @return The tail list.
     */
    public HeadTailList<T> tail();

    /**
     *  Returns the contents of this list as an array.
     *
     *  @return The array.
     */
    public Object [] toArray();

    /**
     *  <p>{@summary Returns the contents of this list in the provided
     *  array.}</p>
     *  <p>If the provided array is larger that the number of elements on the
     *  stack, the exceeding entries on that array remained unchanged.</p>
     *
     *  @param  target  The target array; if this array has an insufficient
     *      size, a new array will be created.
     *  @return An array with all entries from the list; never {@code null}. If
     *      the provided array was large enough to take all elements, it will
     *      be returned, otherwise the returned array is a new one and the
     *      provided array is unchanged.
     */
    @SuppressWarnings( "MethodCanBeVariableArityMethod" )
    public T [] toArray( final T [] target );

    /**
     *  <p>{@summary Returns the contents of this list in  array that is
     *  provided by the given supplier.}</p>
     *  <p>If the provided array is larger that the number of elements on the
     *  stack, the exceeding entries on that array remained unchanged.</p>
     *  <p>If the array is too small, a new array will be created.</p>
     *
     *  @param  supplier    The supplier for the target array.
     *  @return An array with all entries on the stack; never {@code null}. If
     *      the provided array was large enough to take all elements, it will
     *      be returned, otherwise the returned array is a new one and the
     *      provided array is unchanged.
     */
    public default T [] toArray( final IntFunction<T []> supplier )
    {
        return toArray( supplier.apply( size() ) );
    }   //  toArray()
}
//  interface HeadTailList

/*
 *  End of File
 */