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

import static org.apiguardian.api.API.Status.INTERNAL;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.util.LazyMap;
import org.tquadrat.foundation.util.LazySortedMap;

/**
 *  The implementation for
 *  {@link LazyMap}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LazySortedMapImpl.java 966 2022-01-04 22:28:49Z tquadrat $
 *  @since 0.0.5
 *
 *  @param <K> The type of keys maintained by this map
 *  @param <V> The type of mapped values
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: LazySortedMapImpl.java 966 2022-01-04 22:28:49Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class LazySortedMapImpl<K,V> extends LazyMapImpl<K,V> implements LazySortedMap<K,V>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LazySortedMapImpl<K,V>} instance.
     *
     *  @param  doPopulate  {@code true} if the provided supplier will put
     *      values to the map on initialisation, {@code false} if it will
     *      create an empty map.
     *  @param  supplier    The supplier that initialises the internal map for
     *      this instance on the first it is needed.
     */
    public LazySortedMapImpl( final boolean doPopulate, final Supplier<? extends SortedMap<K, V>> supplier )
    {
        super( doPopulate, supplier );
    }   //  LazySortedMapImpl<K,V>()

    /**
     *  Creates a new {@code LazyMapImpl} instance that is initialised with the
     *  given value.
     *
     *  @param  value   The initialisation value.
     */
    @SuppressWarnings( "TypeMayBeWeakened" )
    public LazySortedMapImpl( final SortedMap<K,V> value )
    {
        super( value );
    }   //  LazySortedMapImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}<br>
     *  <br>A call to this method initialises the lazy map.
     */
    @Override
    public final Comparator<? super K> comparator() { return holder().get().comparator(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final K firstKey()
    {
        if( isEmpty() )
        {
            //noinspection NewExceptionWithoutArguments
            throw new NoSuchElementException();
        }

        final var retValue = holder().get().firstKey();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  firstKey()

    /**
     *  {@inheritDoc}<br>
     *  <br>A call to this method initialises the lazy map.
     */
    @Override
    public final SortedMap<K,V> headMap( final K toKey )
    {
        final var retValue = holder().get().headMap( toKey );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  headMap

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    protected final Lazy<? extends SortedMap<K,V>> holder() { return (Lazy<SortedMap<K,V>>) super.holder(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final K lastKey()
    {
        if( isEmpty() )
        {
            //noinspection NewExceptionWithoutArguments
            throw new NoSuchElementException();
        }

        final var retValue = holder().get().lastKey();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  lastKey()

    /**
     *  {@inheritDoc}<br>
     *  <br>A call to this method initialises the lazy map.
     */
    @Override
    public final SortedMap<K,V> subMap( final K fromKey, final K toKey )
    {
        final var retValue = holder().get().subMap( fromKey, toKey );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  subMap

    /**
     *  {@inheritDoc}<br>
     *  <br>A call to this method initialises the lazy map.
     */
    @Override
    public final SortedMap<K,V> tailMap( final K fromKey )
    {
        final var retValue = holder().get().tailMap( fromKey );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  tailMap
}
//  class LazySortedMapImpl

/*
 *  End of File
 */