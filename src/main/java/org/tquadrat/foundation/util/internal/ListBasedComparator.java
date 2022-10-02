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

package org.tquadrat.foundation.util.internal;

import static java.lang.Integer.signum;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.NOT_FOUND;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Comparator;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.Comparators.KeyProvider;

/**
 *  <p>{@summary A comparator that works on a list of sort keys.}</p>
 *  <p>Sometimes, a special sort order is required that cannot be defined as a
 *  rule based on the values themselves. Instead an ordered list of value
 *  defines their sequence.</p>
 *  <p>The implementation first determines the key for a given value, then it
 *  looks up that key in the key list to determine the sort order. Values whose
 *  keys are not in the key list are placed on the list, will be ordered based
 *  on the order implied by the given comparator according their keys.</p>
 *
 *  @param  <T> The type to order.
 *  @param  <K> The key type that is used to determine the order; this may be
 *      the same as the type itself.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ListBasedComparator.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ListBasedComparator.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public class ListBasedComparator<T,K> implements Comparator<T>
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  A simple implementation of
     *  {@link org.tquadrat.foundation.util.Comparators.KeyProvider}
     *  that returns the instance itself as the sort order key.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: ListBasedComparator.java 1032 2022-04-10 17:27:44Z tquadrat $
     *
     *  @param  <T> The type to order.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself. At least, an instance of type
     *      {@code T} must be assignable to {@code K}.
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: ListBasedComparator.java 1032 2022-04-10 17:27:44Z tquadrat $" )
    public static class SimpleKeyProvider<T,K extends T> implements KeyProvider<T,K>
    {
            /*--------------*\
        ====** Constructors **=====================================================
            \*--------------*/
        /**
         *  Creates a new instance of {@code SimpleKeyProvider}.
         */
        public SimpleKeyProvider() { /* Just exists */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         *
         *  @return The instance itself as the sort order key; {@code null} if
         *      the instance is {@code null} itself.
         */
        @SuppressWarnings( "unchecked" )
        @Override
        public final K getKey( final T instance ) { return (K) instance; }
    }
    //  class SimpleKeyProvider

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The comparator that is used to determine the sort order for instances
     *  that do not have their keys in the list; may be {@code null}.
     */
    private final Comparator<? super K> m_Comparator;

    /**
     *  The key provider.
     */
    private final KeyProvider<T,K> m_KeyProvider;

    /**
     *  The list with the sort order keys.
     */
    private final K [] m_Keys;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ListBasedComparator} instance.
     *
     *  @param  keyProvider The implementation of
     *      {@link org.tquadrat.foundation.util.Comparators.KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed.
     *  @param  keys    The sort order keys.
     */
    @SuppressWarnings( "MethodCanBeVariableArityMethod" )
    public ListBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<? super K> comparator, final K [] keys )
    {
        m_Keys = requireNonNullArgument( keys, "keys" );
        m_KeyProvider = requireNonNullArgument( keyProvider, "keyProvider" );
        m_Comparator = requireNonNullArgument( comparator, "comparator" );
    }   //  ListBasedComparator()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final int compare( final T o1, final T o2 )
    {
        var retValue = 0; // Assuming that the values are equal …
        if( o1 != o2 ) // Not identical …
        {
            /*
             *  Retrieve the keys and get the index for the respective key. A
             *  value of -1 (NOT_FOUND) indicates that the key was not found in
             *  the list of keys and that the natural order should be used for
             *  the respective entry.
             */
            final var key1 = m_KeyProvider.getKey( o1 );
            final var index1 = searchKey( key1 );
            assert index1 == NOT_FOUND || index1 >= 0;
            final var key2 = m_KeyProvider.getKey( o2 );
            final var index2 = searchKey( key2 );
            assert index2 == NOT_FOUND || index2 >= 0;

            if( index1 == index2 )
            {
                /*
                 *  If both index values are not -1, this means that the keys
                 *  are equals and therefore the instances are to be treated
                 *  as equals, too.
                 */
                if( index1 == NOT_FOUND )
                {
                    //---* Both are not in the list *--------------------------
                    retValue = signum( m_Comparator.compare( key1, key2 ) );
                }
            }
            else
            {
                /*
                 * That one that is in the list is less than the other one.
                 */
                if( index1 == NOT_FOUND )
                {
                    retValue = 1;
                }
                else if( index2 == NOT_FOUND )
                {
                    retValue = -1;
                }
                else
                {
                    /*
                     * That one that is closer to the start of the list is less
                     * than the other one.
                     */
                    retValue = signum( index1 - index2 );
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  compare()

    /**
     *  Searches the given key in the list.
     *
     *  @param  key The key.
     *  @return The index in the list or
     *      {@link org.tquadrat.foundation.lang.CommonConstants#NOT_FOUND}
     *      ({@value org.tquadrat.foundation.lang.CommonConstants#NOT_FOUND})
     *      if the key is not in the list.
     */
    private int searchKey( final K key )
    {
        /*
         * Although using Arrays.binarySearch() looked a good idea at first
         * place, it does not work as we are not allowed to sort the key's
         * array, for obvious reasons.
         */
        var found = false;
        var retValue = m_Keys.length;
        while( !found && (--retValue >= 0) )
        {
            found = (key == m_Keys [retValue]) || (nonNull( key ) && key.equals( m_Keys [retValue] ));
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  searchKey()
}
//  class ListBasedComparator

/*
 *  End of File
 */