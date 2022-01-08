/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Comparator;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.Comparators.KeyProvider;

/**
 *  Sometimes a special sort order is required that cannot defined as a rule.
 *  Instead a list defines the sequence.
 *
 *  @param  <T> The type to order.
 *  @param  <K> The key type that is used to determine the order; this may be
 *      the same as the type itself.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ListBasedComparator.java 980 2022-01-06 15:29:19Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ListBasedComparator.java 980 2022-01-06 15:29:19Z tquadrat $" )
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
     *  @version $Id: ListBasedComparator.java 980 2022-01-06 15:29:19Z tquadrat $
     *
     *  @param  <T> The type to order.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself. At least, an instance of type
     *      {@code T} must be assignable to {@code K}.
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: ListBasedComparator.java 980 2022-01-06 15:29:19Z tquadrat $" )
    private static class SimpleKeyProvider<T,K> implements KeyProvider<T,K>
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SimpleKeyProvider} instance.
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

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code ListBasedComparator<T>} objects.
     */
    @SuppressWarnings( "rawtypes" )
    public static final ListBasedComparator [] EMPTY_ListBasedComparator_ARRAY = new ListBasedComparator [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The comparator that is used to determine the sort order for instances
     *  that do not have their keys in the list; may be {@code null}.
     */
    private final Comparator<? super T> m_Comparator;

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
     *  @param  keys    The sort order keys.
     */
    @SuppressWarnings( "MethodCanBeVariableArityMethod" )
    public ListBasedComparator( final K [] keys )
    {
        this( new SimpleKeyProvider<>(), null, null, requireNonNullArgument( keys, "keys" ) );
    }   //  ListBasedComparator()

    /**
     *  Creates a new {@code ListBasedComparator} instance.
     *
     *  @param  keys    The sort order keys.
     */
    public ListBasedComparator( final List<K> keys )
    {
        this( new SimpleKeyProvider<>(), null, requireNonNullArgument( keys, "keys" ), null );
    }   //  ListBasedComparator()

    /**
     *  Creates a new {@code ListBasedComparator} instance.
     *
     *  @param  keyProvider The implementation of
     *      {@link org.tquadrat.foundation.util.Comparators.KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed; if {@code null}, those are ordered randomly in
     *      a non-consistent way if they or their keys do not implement the
     *      {@link Comparable}
     *      interface.
     *  @param  keys    The sort order keys.
     */
    public ListBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<? super T> comparator, final List<K> keys )
    {
        this( keyProvider, comparator, requireNonNullArgument( keys, "keys" ), null );
    }   //  ListBasedComparator()

    /**
     *  Creates a new {@code ListBasedComparator} instance.
     *
     *  @param  keyProvider The implementation of
     *      {@link org.tquadrat.foundation.util.Comparators.KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed; if {@code null}, those are ordered randomly in
     *      a non-consistent way if they or their keys do not implement the
     *      {@link Comparable}
     *      interface.
     *  @param  keys    The sort order keys.
     */
    @SafeVarargs
    public ListBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<? super T> comparator, final K... keys )
    {
        this( keyProvider, comparator, null, requireNonNullArgument( keys, "keys" ) );
    }   //  ListBasedComparator()

    /**
     *  Creates a new {@code ListBasedComparator} instance.
     *
     *  @param  keyProvider The implementation of
     *      {@link org.tquadrat.foundation.util.Comparators.KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed; if {@code null}, those are ordered randomly in
     *      a non-consistent way if they or their keys do not implement the
     *      {@link Comparable}
     *      interface.
     *  @param  keysList    The sort order keys in a list; is {@code null} when
     *      {@code keysArray} is not {@code null}.
     *  @param  keysArray   The sort order keys in an array; is {@code null}
     *      when {@code keysList} is not {@code null}.
     */
    @SuppressWarnings( {"unchecked", "MethodCanBeVariableArityMethod"} )
    private ListBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<? super T> comparator, final List<K> keysList, final K [] keysArray )
    {
        assert isNull( keysList ) ^ isNull( keysArray ) : "Both, keysList and keysArray are either null or not null";

        //noinspection SuspiciousArrayCast
        m_Keys = nonNull( keysList ) ? (K []) keysList.toArray() : keysArray.clone();
        m_KeyProvider = requireNonNullArgument( keyProvider, "keyProvider" );
        m_Comparator = comparator;
    }   //  ListBasedComparator()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( {"rawtypes", "unchecked", "preview"} )
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
                    //noinspection IfStatementWithTooManyBranches
                    if( nonNull( m_Comparator ) )
                    {
                        //---* Use the comparator if we have one *-------------
                        retValue = signum( m_Comparator.compare( o1, o2 ) );
                    }
                    else if( key1 instanceof Comparable comparable )
                    {
                        //---* Use the implicit natural order *----------------
                        retValue = signum( comparable.compareTo( key2 ) );
                    }
                    else if( (o1 != key1) || (o2 != key2) )
                    /*
                     * Yes, we know that we do check for *identity* here! If
                     * no explicit KeyProvider was given, the SimpleKeyProvider
                     * is used, and that will return o# as Key#. This would
                     * mean that we have performed the action below already
                     * above …
                     */
                    {
                        if( o1 instanceof Comparable comparable )
                        {
                            retValue = signum( comparable.compareTo( o2 ) );
                        }
                    }
                    else
                    {
                        /*
                         * We have no idea how to order, so we just compare the
                         * hashcodes of the keys.
                         */
                        retValue = Long.signum( (long) key1.hashCode() - (long) key2.hashCode() );
                    }
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
                     * That one that is closer to the begin of the list is less
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