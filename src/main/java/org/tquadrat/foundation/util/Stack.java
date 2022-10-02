/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.lang.reflect.Array;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntFunction;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.lang.AutoLock;

/**
 *  <p>{@summary A stand-alone implementation for stack, without the ballast
 *  from the Collections framework.}</p>
 *  <p>This implementation is not synchronised, but thread-safe.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Stack.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.5
 *
 *  @param  <T> The type for the stack entries.
 *
 *  @UMLGraph.link
 *
 *  @see    java.util.Stack
 */
@SuppressWarnings( "NewClassNamingConvention" )
@ClassVersion( sourceVersion = "$Id: Stack.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class Stack<T>
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The stack entries.
     *
     *  @note   This class cannot be changed to a {@code record} (instead of
     *      being a {@code class}) because a {@code record} as an inner class
     *      is implicitly static, and that collides with the type argument
     *      {@code <T>} that is inherited from the surrounding class.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: Stack.java 1032 2022-04-10 17:27:44Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: Stack.java 1032 2022-04-10 17:27:44Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    private final class Entry
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The current head element.
         */
        private final T m_Head;

        /**
         *  The remaining elements.
         */
        @SuppressWarnings( "UseOfConcreteClass" )
        private final Entry m_Tail;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code Stack} object.
         *
         *  @param  head    The current head.
         *  @param  tail    The current tail; may be {@code null}, indicating
         *      an empty tail.
         */
        @SuppressWarnings( "UseOfConcreteClass" )
        public Entry( final T head, final Entry tail )
        {
            m_Head = head;
            m_Tail = tail;
        }   //  Entry()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Returns the current head.
         *
         *  @return The head.
         */
        public final T head() { return m_Head; }

        /**
         *  Returns the current tail.
         *
         *  @return The tail.
         */
        @SuppressWarnings( {"UseOfConcreteClass", "ReturnOfInnerClass"} )
        public final Entry tail() { return m_Tail; }
    }
    //  class Entry

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code Stack} objects.
     */
    @SuppressWarnings( "rawtypes" )
    public static final Stack [] EMPTY_Stack_ARRAY = new Stack [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The number of entries on the stack.
     */
    private int m_Count = 0;

    /**
     *  The entries.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private Entry m_Entries = null;

    /**
     *  The &quot;read&quot; lock.
     */
    private final AutoLock m_ReadLock;

    /**
     *  The &quot;write&quot; lock.
     */
    private final AutoLock m_WriteLock;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Stack} object.
     */
    public Stack()
    {
        final var lock = new ReentrantReadWriteLock( false );
        m_ReadLock = AutoLock.of( lock.readLock() );
        m_WriteLock = AutoLock.of( lock.writeLock() );
    }   //  Stack()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Empties the whole stack in one go.
     */
    public final void clear()
    {
        m_WriteLock.execute( () ->
        {
            m_Entries = null;
            m_Count = 0;
        } );
    }   //  clear()

    /**
     *  <p>{@summary Tests if the stack is not empty.}</p>
     *  <p>If concurrent threads will access the stack, it is still possible
     *  that this method will return {@code true}, but a call to
     *  {@link #pop()}
     *  immediately after returns {@code null}.</p>
     *
     *  @return {@code true} if there are still entries on the stack,
     *      {@code false} otherwise.
     *
     *  @see #isEmpty()
     */
    public final boolean hasMore() { return !isEmpty(); }

    /**
     *  <p>{@summary Tests if the stack is empty.}</p>
     *  <p>If concurrent threads will access the stack, it is still possible
     *  that this method will return {@code false}, but a call to
     *  {@link #pop()}
     *  immediately after returns {@code null}.</p>
     *
     *  @return {@code true} if there are no entries on the stack,
     *      {@code false} otherwise.
     *
     *  @see #hasMore()
     */
    public final boolean isEmpty()
    {
        var retValue = false;
        try( final var ignore = m_ReadLock.lock() )
        {
            retValue = isNull( m_Entries );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isEmpty()

    /**
     *  <p>{@summary Returns the first entry from the stack <i>without</i>
     *  removing it from the stack.}</p>
     *  <p>If concurrent threads will access the stack, it is still possible
     *  that this method will return a different value than a consecutive call
     *  to
     *  {@link #pop()}.</p>
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the first entry; it will be empty if the stack is empty.
     */
    public final Optional<T> peek()
    {
        final var retValue = m_ReadLock.execute( () -> nonNull( m_Entries ) ? m_Entries.head() : null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  peek()

    /**
     *  Returns the first entry from the stack and removes it.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the first entry; it will be empty if the stack is empty.
     */
    public final Optional<T> pop()
    {
        Optional<T> retValue = Optional.empty();
        try( final var ignore = m_WriteLock.lock() )
        {
            if( nonNull( m_Entries ) )
            {
                retValue = Optional.of( m_Entries.head() );
                m_Entries = m_Entries.tail();
                --m_Count;
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  pop()

    /**
     *  Add the given entry to the stack.
     *
     *  @param  entry   The value to add.
     */
    public final void push( final T entry )
    {
        requireNonNullArgument( entry, "entry" );
        m_WriteLock.execute( () ->
        {
            m_Entries = new Entry( entry, m_Entries );
            ++m_Count;
        } );
    }   //  push()

    /**
     *  <p>{@summary Adds the given entries to the stack, in LIFO order.}
     *  Nothing happens, if the provided array is empty.</p>
     *  <p>The provided array may not contain {@code null} elements.</p>
     *  <p>If the push failed for anyone element of the array, the stack
     *  remained unchanged.</p>
     *  <p>The method guarantees that the elements of the array are stored to
     *  the stack in consecutive order, even in a multithreaded
     *  environment.</p>
     *
     *  @param  entries   The values to add.
     *  @throws IllegalArgumentException    At least one element of the
     *      provided array is {@code null}.
     */
    @SafeVarargs
    public final void push( final T... entries ) throws IllegalArgumentException
    {
        for( var i = 0; i < requireNonNullArgument( entries, "entries" ).length; ++i )
        {
            if( isNull( entries [i] ) ) throw new ValidationException( format( "The entry %1$d of the arguments list is null", i ) );
        }

        m_WriteLock.execute( () ->
        {
            for( final var entry : entries )
            {
                m_Entries = new Entry( entry, m_Entries );
                ++m_Count;
            }
        } );
    }   //  push()

    /**
     *  Returns the number of entries on the stack.
     *
     *  @return The number of entries.
     */
    public final int size() { return m_Count; }

    /**
     *  <p>{@summary Returns all entries that are currently on the stack as an
     *  array without removing them, with the top most entry as the first.}
     *  Therefore, this is more or less a
     *  {@link #peek()}
     *  on the whole stack.</p>
     *  <p>If the provided array is larger that the number of elements on the
     *  stack, the exceeding entries on that array remained unchanged.</p>
     *
     *  @param  target  The target array; if this array has an insufficient
     *      size, a new array will be created.
     *  @return An array with all entries on the stack; never {@code null}. If
     *      the provided array was large enough to take all elements, it will
     *      be returned, otherwise the returned array is a new one and the
     *      provided array is unchanged.
     */
    @SuppressWarnings( {"unchecked", "MethodCanBeVariableArityMethod"} )
    public final T [] toArray( final T [] target )
    {
        requireNonNullArgument( target, "target" );

        @SuppressWarnings( {"OptionalGetWithoutIsPresent", "OverlyLongLambda"} )
        final var retValue = m_ReadLock.execute( () ->
            {
                final var result = target.length >= m_Count ? target : (T []) Array.newInstance( target.getClass().getComponentType(), m_Count );
                var index = 0;
                var entries = m_Entries;
                while( nonNull( entries ) )
                {
                    result [index++] = entries.head();
                    entries = entries.tail();
                }
                return result;
            } ).get();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toArray()

    /**
     *  <p>{@summary Returns all entries that are currently on the stack as an
     *  array without removing them, with the top most entry as the first.}
     *  Therefore, this is more or less a
     *  {@link #peek()}
     *  on the whole stack.</p>
     *
     *  @param  supplier    The supplier for the target array.
     *  @return An array with all entries on the stack; never {@code null}.
     */
    public final T [] toArray( final IntFunction<T []> supplier )
    {
        @SuppressWarnings( "OptionalGetWithoutIsPresent" )
        final var retValue = m_ReadLock.execute( () -> toArray( requireNonNullArgument( supplier, "supplier" ).apply( m_Count ) ) )
            .get();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toArray()
}
//  class Stack

/*
 *  End of File
 */