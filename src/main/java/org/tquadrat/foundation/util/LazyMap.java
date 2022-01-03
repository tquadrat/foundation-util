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

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.internal.LazyMapImpl;

/**
 *  The interface for a
 *  {@link Map}
 *  that will be initialised only when required.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LazyMap.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @param <K> The type of keys maintained by this map.
 *  @param <V> The type of mapped values.
 *
 *  @see org.tquadrat.foundation.lang.Lazy
 *
 *  @note   There is no implementation of a {@code map()} method in this
 *      interface because it is assumed that this would be confusing: such a
 *      {@code map()} method would operate on the whole map that is wrapped by
 *      this value, and not on a entry as one would expect. Refer to
 *      {@link org.tquadrat.foundation.lang.Lazy#map(java.util.function.Function)}.
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "preview" )
@ClassVersion( sourceVersion = "$Id: LazyMap.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed interface LazyMap<K,V> extends Map<K,V>
    permits LazySortedMap, LazyMapImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  If this {@code LazyMap} instance has been initialised already, the
     *  provided
     *  {@link Consumer}
     *  will be executed; otherwise nothing happens.
     *
     *  @param  consumer    The consumer.
     */
    public void ifPresent( final Consumer<? super Map<K,V>> consumer );

    /**
     *  Forces the initialisation of this {@code LazyMap} instance.
     */
    public void init();

    /**
     *  Checks whether this {@code LazyMap} instance has been initialised
     *  already.
     *
     *  @return {@code true} if the instance was initialised, {@code false}
     *      otherwise.
     */
    public boolean isPresent();

    /**
     *  Creates a new {@code LazyMap} instance that is already initialised.
     *
     *  @param <K> The type of keys maintained by this map.
     *  @param <V> The type of mapped values.
     *  @param  value   The value.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <K,V> LazyMap<K,V> of( final Map<K,V> value )
    {
        return new LazyMapImpl<>( requireNonNullArgument( value, "value" ) );
    }   //  of()

    /**
     *  Creates a new {@code LazyMap} instance that uses the given supplier to
     *  create the internal map, but that supplier does not provide values on
     *  initialisation.
     *
     *  @param <K> The type of keys maintained by this map.
     *  @param <V> The type of mapped values.
     *  @param  supplier    The supplier that initialises for the new instance
     *      of {@code LazyMap} when needed.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <K,V> LazyMap<K,V> use( final Supplier<? extends Map<K, V>> supplier )
    {
        return new LazyMapImpl<>( false, supplier );
    }   //  use()

    /**
     *  Creates a new {@code LazyMap} instance that uses the given supplier to
     *  initialise.
     *
     *  @param <K> The type of keys maintained by this map.
     *  @param <V> The type of mapped values.
     *  @param  doPopulate  {@code true} if the provided supplier will put
     *      entries to the map on initialisation, {@code false} if will create
     *      an empty map.
     *  @param  supplier    The supplier that initialises for the new instance
     *      of {@code LazyMap} when needed.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <K,V> LazyMap<K,V> use( final boolean doPopulate, final Supplier<? extends Map<K, V>> supplier )
    {
        return new LazyMapImpl<>( doPopulate, supplier );
    }   //  use()
}
//  interface LazyMap

/*
 *  End of File
 */