/*
 * ============================================================================
 * Copyright © 2002-2019 by Thomas Thrien.
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

import java.io.Serializable;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Pair;
import org.tquadrat.foundation.util.internal.RangeMapImpl;

/**
 *  <p>{@code A range map is used to map a value to a given numerical range.}
 *  The lower border of the lowest range is always
 *  {@link Double#MAX_VALUE -Double.MAX_VALUE}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: RangeMap.java 1017 2022-02-10 19:39:09Z tquadrat $
 *  @since 0.0.7
 *
 *  @param <T>  The type of the mapped value.
 *
 *  @see RangeFunctions
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: RangeMap.java 1017 2022-02-10 19:39:09Z tquadrat $" )
@API( status = STABLE, since = "0.0.7" )
public sealed interface RangeMap<T> extends Serializable
    permits RangeMapImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Adds a range.} If there is already a range with the given
     *  {@code key}, it will <i>not</i> replaced by the new one!</p>
     *  <p>The method will return a reference to the map itself; this allows
     *  to chain it with other
     *  {@code #addRange(double, Object)}
     *  method calls.</p>
     *
     *  @return A reference to this range map instance.
     *
     *  @param  key The upper border of the range.
     *  @param  value   The mapped value.
     */
    public RangeMap<T> addRange( final double key, final T value );

    /**
     *  Clears the range map.
     */
    public void clear();

    /**
     *  Returns a copy of this range map.
     *
     *  @param  modifiable  {@code true} if the copy can be modified,
     *      {@code false} otherwise.
     *  @return The copy.
     */
    public RangeMap<T> copy( final boolean modifiable );

    /**
     *  Returns a modifiable copy of this range map.
     *
     *  @return The copy.
     */
    public default RangeMap<T> copy() { return copy( true ); }

    /**
     *  Returns the entries in their order.
     *
     *  @return The entries; for an empty range map, an empty array will be
     *      returned.
     */
    public Pair<Double,T>[] entries();

    /**
     *  Returns the value for the range the given key is in.
     *
     *  @param  key The key.
     *  @return The value that is mapped to the range.
     *  @throws IllegalStateException   No entry was added to the range map; it
     *      is empty.
     */
    public T get( final double key ) throws IllegalStateException;

    /**
     *  <p>{@summary Returns {@code true} if the range map is empty.}</p>
     *  <p>Usually, a range map is empty only after a call to
     *  {@link #clear()}
     *  or when the last entry was removed by a call to
     *  {@link #removeRange(double)},
     *  but special implementations of this interface can handle this
     *  differently.</p>
     *
     *  @return {@code true} if the range map is empty, {@code false}
     *      if there were already some entries added to it.
     */
    public boolean isEmpty();

    /**
     *  The factory method for a new instance of {@code RangeMap}.
     *
     *  @param  <V> The value type for the range map.
     *  @param  defaultValue    The default value; this is the that is returned
     *      if the key is above all range limits.
     *  @param  includes    {@code true} if the limit belongs to the
     *      range, {@code false} otherwise.
     *  @return The new range map instance.
     */
    public static <V> RangeMap<V> of( final V defaultValue, final boolean includes )
    {
        final RangeMap<V> retValue = new RangeMapImpl<>( includes );
        retValue.setDefault( defaultValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  of()

    /**
     *  <p>{@summary Removes a range.} Nothing happens if there is no range for
     *  the given key value.</p>
     *  <p>After this operation, the range map can be empty.</p>
     *  <p>The method will return a reference to the map itself; this allows
     *  to chain it with
     *  {@link #addRange(double, Object)} method calls.</p>
     *
     *  @param  key The key for the range to remove.
     *  @return A reference to this range map instance.
     */
    public RangeMap<T> removeRange( double key );

    /**
     *  <p>{@summary Replaces an already existing range.} If there is no range
     *  for the given key value, the method will just add a new range.</p>
     *  <p>The method will return a reference to the map itself; this allows to
     *  chain it with
     *  {@link #addRange(double, Object)} method calls.</p>
     *
     *  @param  key The upper border of the range.
     *  @param  value   The mapped value.
     *  @return A reference to this range map instance.
     */
    public default RangeMap<T> replaceRange( final double key, final T value )
    {
        removeRange( key );
        addRange( key, value );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  replaceRange()

    /**
     *  <p>{@summary Sets the default value and overwrites that one that was
     *  set on creation of the range map.}</p>
     *  <p>The default value is that one that is returned if the key is above
     *  all range limits.</p>
     *  <p>The method will return a reference to the map itself; this allows
     *  to chain it with
     *  {@link #addRange(double, Object)} method calls.</p>
     *
     *  @param  value   The mapped value.
     *  @return A reference to this range map instance.
     *
     *  @see #of(Object,boolean)
     */
    public RangeMap<T> setDefault( final T value );
}
//  interface RangeMap

/*
 *  End of File
 */