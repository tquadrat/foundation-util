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

import static java.lang.Integer.signum;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.Comparators.KeyProvider;

/**
 *  Sometimes a special sort key is used to order elements, and instead of
 *  hiding the generation of that key inside the method
 *  {@link Comparator#compare(Object, Object) compare()}
 *  of an implementation of
 *  {@link Comparator},
 *  this implementation expects an instance of
 *  {@link KeyProvider}
 *  as an argument to its
 *  {@linkplain #KeyBasedComparator(KeyProvider,Comparator,boolean) constructor}.
 *
 *  @param  <T> The type to order.
 *  @param  <K> The key type that is used to determine the order; this may be
 *      the same as the type itself.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: KeyBasedComparator.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: KeyBasedComparator.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public class KeyBasedComparator<T,K> implements Comparator<T>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  A flag that determines whether the keys are to be cached internally;
     *  {@code true} means they are kept, {@code false} means that the keys are
     *  generated newly for each comparison.
     */
    private final boolean m_CacheKeys;

    /**
     *  The cache for the keys.
     */
    private final Map<T,K> m_KeyCache = new IdentityHashMap<>();

    /**
     *  The comparator that determines the sort order for the keys.
     */
    private final Comparator<K> m_KeyComparator;

    /**
     *  The method that generates the key for the given element.
     */
    private final KeyProvider<T,K> m_KeyProvider;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code KeyBasedComparator} instance.
     *
     *  @param  keyProvider The method that generates the key for a given
     *      element.
     *  @param  keyComparator   The comparator that determines the sort order
     *      for the keys.
     *  @param cacheKeys    A flag that determines whether the keys are to be
     *      cached internally; {@code true} means they are kept, {@code false}
     *      means that the keys are generated newly for each comparison.
     */
    public KeyBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<K> keyComparator, final boolean cacheKeys )
    {
        m_CacheKeys = cacheKeys;
        m_KeyComparator = requireNonNullArgument( keyComparator, "keyComparator" );
        m_KeyProvider = requireNonNullArgument( keyProvider, "keyProvider" );
    }   //  KeyBasedComparator<T,K>()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final int compare( final T o1, final T o2 )
    {
        final var k1 = retrieveKey( o1 );
        final var k2 = retrieveKey( o2 );

        final var retValue = signum( m_KeyComparator.compare( k1, k2 ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  compare()

    /**
     *  Provides the key for the given element, either from the cache or
     *  freshly generated.
     *
     *  @param  o   The element.
     *  @return The sort key for the element.
     */
    private final K retrieveKey( final T o )
    {
        final var retValue = m_CacheKeys
            ? m_KeyCache.computeIfAbsent( o, m_KeyProvider::getKey )
            : m_KeyProvider.getKey( o );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveKey()
}
//  class KeyBasedComparator

/*
 *  End of File
 */