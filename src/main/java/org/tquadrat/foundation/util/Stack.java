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

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.lang.AutoLock;

/**
 *  A stand-alone implementation for stack, without the ballast from the
 *  Collections framework. <br>
 *  <br>This implementation is not synchronised, but thread-safe.
 *
 *  @note This class does not implement the interface
 *      {@link java.util.Stack}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Stack.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @param  <T> The type for the stack entries.
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Stack.java 820 2020-12-29 20:34:22Z tquadrat $" )
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
     *  @version $Id: Stack.java 820 2020-12-29 20:34:22Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: Stack.java 820 2020-12-29 20:34:22Z tquadrat $" )
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
        @SuppressWarnings( "InstanceVariableOfConcreteClass" )
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
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private Entry m_Entries = null;

    /**
     *  The read lock.
     */
    private final AutoLock m_ReadLock;

    /**
     *  The write lock.
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
        try( @SuppressWarnings( "unused" ) final var l = m_WriteLock.lock() )
        {
            m_Entries = null;
            m_Count = 0;
        }
    }   //  clear()

    /**
     *  Tests if the stack is not empty.<br>
     *  <br>If concurrent threads will access the stack, it is still possible
     *  that this method will return {@code true}, but a call to
     *  {@link #pop()}
     *  immediately after returns {@code null}.
     *
     *  @return {@code true} if there are still entries on the stack,
     *      {@code false} otherwise.
     *
     *  @see #isEmpty()
     */
    public final boolean hasMore()
    {
        var retValue = false;
        try( @SuppressWarnings( "unused" ) final var l = m_ReadLock.lock() )
        {
            retValue = nonNull( m_Entries );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasMore()

    /**
     *  Tests if the stack is empty.<br>
     *  <br>If concurrent threads will access the stack, it is still possible
     *  that this method will return {@code false}, but a call to
     *  {@link #pop()}
     *  immediately after returns {@code null}.
     *
     *  @return {@code true} if there are no entries on the stack,
     *      {@code false} otherwise.
     *
     *  @see #hasMore()
     */
    public final boolean isEmpty()
    {
        var retValue = false;
        try( @SuppressWarnings( "unused" ) final var l = m_ReadLock.lock() )
        {
            retValue = isNull( m_Entries );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isEmpty()

    /**
     *  Returns the first entry from the stack <i>without</i> removing it from
     *  the stack.<br>
     *  <br>If concurrent threads will access the stack, it is still possible
     *  that this method will return a different value than a consecutive call
     *  to
     *  {@link #pop()}.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the first entry; will be empty if the stack is empty.
     */
    public final Optional<T> peek()
    {
        Optional<T> retValue = Optional.empty();
        try( @SuppressWarnings( "unused" ) final var l = m_ReadLock.lock() )
        {
            if( nonNull( m_Entries ) )
            {
                retValue = Optional.of( m_Entries.head() );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  peek()

    /**
     *  Returns the first entry from the stack and removes it.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the first entry; will be empty if the stack is empty.
     */
    public final Optional<T> pop()
    {
        Optional<T> retValue = Optional.empty();
        try( @SuppressWarnings( "unused" ) final var l = m_WriteLock.lock() )
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
        try( @SuppressWarnings( "unused" ) final var l = m_WriteLock.lock() )
        {
            m_Entries = new Entry( requireNonNullArgument( entry, "entry" ), m_Entries );
            ++m_Count;
        }
    }   //  push()

    /**
     *  Add the given entries to the stack, in LIFO order. Nothing happens, if
     *  the provided array is empty.<br>
     *  <br>The provided array may not contain {@code null} elements.<br>
     *  <br>If the push failed for one element of the array, the stack
     *  remained unchanged.<br>
     *  <br>The method guarantees that the elements of the array are stored to
     *  the stack in consecutive order, even in a multi-threaded environment.
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

        try( @SuppressWarnings( "unused" ) final var l = m_WriteLock.lock() )
        {
            for( final var entry : entries )
            {
                m_Entries = new Entry( entry, m_Entries );
                ++m_Count;
            }
        }
    }   //  push()

    /**
     *  Returns the number of entries on the stack.
     *
     *  @return The number of entries.
     */
    public final int size() { return m_Count; }

    /**
     *  Returns all entries that are currently on the stack as an array without
     *  removing them, with the top most entry as the first. Therefore this is
     *  more or less a
     *  {@link #peek()}
     *  on the whole stack.<br>
     *  <br>If the provided array is larger that the number of elements on the
     *  stack, the exceeding entries on that array remained unchanged.
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

        T [] retValue = null;

        try( @SuppressWarnings( "unused" ) final var l = m_ReadLock.lock() )
        {
            retValue = target.length >= m_Count ? target : (T []) Array.newInstance( target.getClass().getComponentType(), m_Count );
            var index = 0;
            var entries = m_Entries;
            while( nonNull( entries ) )
            {
                retValue [index++] = entries.head();
                entries = entries.tail();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toArray()
}
//  class Stack

/*
 *  End of File
 */