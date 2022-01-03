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

import java.util.SortedMap;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.internal.LazySortedMapImpl;

/**
 *  The interface for a
 *  {@link SortedMap}
 *  that will be initialised only when required.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LazySortedMap.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @param <K> The type of keys maintained by this map.
 *  @param <V> The type of mapped values.
 *
 *  @see org.tquadrat.foundation.lang.Lazy
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "preview" )
@ClassVersion( sourceVersion = "$Id: LazySortedMap.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed interface LazySortedMap<K,V> extends LazyMap<K,V>, SortedMap<K,V>
    permits LazySortedMapImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates a new {@code LazySortedMap} instance that is already
     *  initialised.
     *
     *  @param <K> The type of keys maintained by this map.
     *  @param <V> The type of mapped values.
     *  @param  value   The value.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <K,V> LazySortedMap<K,V> of( final SortedMap<K,V> value )
    {
        return new LazySortedMapImpl<>( requireNonNullArgument( value, "value" ) );
    }   //  of()

    /**
     *  Creates a new {@code LazySortedMap} instance that uses the given
     *  supplier to create the internal map, but that supplier will not
     *  populate the map with entries.
     *
     *  @param <K> The type of keys maintained by this map.
     *  @param <V> The type of mapped values.
     *  @param  supplier    The supplier that initialises the new instance of
     *      {@code LazySortedMap} when needed.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <K,V> LazySortedMap<K,V> use( final Supplier<? extends SortedMap<K, V>> supplier )
    {
        return new LazySortedMapImpl<>( false, supplier );
    }   //  use()

    /**
     *  Creates a new {@code LazySortedMap} instance that uses the given
     *  supplier to initialise.
     *
     *  @param <K> The type of keys maintained by this map.
     *  @param <V> The type of mapped values.
     *  @param  doPopulate  {@code true} if the provided supplier will put
     *      values to the map on initialisation, {@code false} if it will
     *      create an empty map.
     *  @param  supplier    The supplier that initialises the new instance of
     *      {@code LazySortedMap} when needed.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <K,V> LazySortedMap<K,V> use( final boolean doPopulate, final Supplier<? extends SortedMap<K, V>> supplier )
    {
        return new LazySortedMapImpl<>( doPopulate, supplier );
    }   //  use()
}
//  interface LazySortedMap

/*
 *  End of File
 */