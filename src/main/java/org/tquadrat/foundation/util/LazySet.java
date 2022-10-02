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

package org.tquadrat.foundation.util;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.internal.LazySetImpl;

/**
 *  The interface for a
 *  {@link Set}
 *  that will be initialised only when required.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LazySet.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.5
 *
 *  @param  <E> The type of elements in this set.
 *
 *  @see org.tquadrat.foundation.lang.Lazy
 *
 *  @note   There is no implementation of a {@code map()} method in this
 *      interface because it is assumed that this would be confusing: such a
 *      {@code map()} method would operate on the whole list that is wrapped by
 *      this value, and not on an entry as one would expect. Refer to
 *      {@link org.tquadrat.foundation.lang.Lazy#map(java.util.function.Function)}.
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: LazySet.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed interface LazySet<E> extends Set<E>
    permits LazySetImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  If this {@code LazySet} instance has been initialised already, the
     *  provided
     *  {@link Consumer}
     *  will be executed; otherwise nothing happens.
     *
     *  @param  consumer    The consumer.
     */
    public void ifPresent( final Consumer<? super Set<E>> consumer );

    /**
     *  Forces the initialisation of this {@code LazySet} instance.
     */
    public void init();

    /**
     *  Checks whether this {@code LazySet} instance has been initialised
     *  already.
     *
     *  @return {@code true} if the instance was initialised, {@code false}
     *      otherwise.
     */
    public boolean isPresent();

    /**
     *  Creates a new {@code LazySet} instance that is already initialised.
     *
     *  @param  <E> The type of elements in this set.
     *  @param  value   The value.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <E> LazySet<E> of( final Set<E> value )
    {
        return new LazySetImpl<>( requireNonNullArgument( value, "value" ) );
    }   //  of()

    /**
     *  Creates a new {@code LazySet} instance that uses the given supplier to
     *  create the internal map, but that supplier does not provide values on
     *  initialisation.
     *
     *  @param  <E> The type of elements in this set.
     *  @param  supplier    The supplier that initialises for the new instance
     *      of {@code LazySet} when needed.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <E> LazySet<E> use( final Supplier<? extends Set<E>> supplier )
    {
        return new LazySetImpl<>( false, supplier );
    }   //  use()

    /**
     *  Creates a new {@code LazySet} instance that uses the given supplier to
     *  initialise.
     *
     *  @param  <E> The type of elements in this set.
     *  @param  doPopulate  {@code true} if the provided supplier will put
     *      entries to the set on initialisation, {@code false} if it will
     *      create an empty set.
     *  @param  supplier    The supplier that initialises for the new instance
     *      of {@code LazySet} when needed.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <E> LazySet<E> use( final boolean doPopulate, final Supplier<? extends Set<E>> supplier )
    {
        return new LazySetImpl<>( doPopulate, supplier );
    }   //  use()
}
//  interface LazySet

/*
 *  End of File
 */