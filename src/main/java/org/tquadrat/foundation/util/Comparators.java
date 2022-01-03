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

import static java.lang.Integer.signum;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Comparator.naturalOrder;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Comparator;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.util.internal.KeyBasedComparator;
import org.tquadrat.foundation.util.internal.ListBasedComparator;

/**
 *  This class provides factory methods for a bunch of different
 *  implementations of
 *  {@link Comparator}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Comparators.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@UtilityClass
@ClassVersion( sourceVersion = "$Id: Comparators.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class Comparators
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  Implementations of this interface provides the sort order key from the
     *  given instance of the type. <br>
     *  <br>Usually the implementation of#
     *  {@link #getKey(Object) getKey()}
     *  must ensure that the returned keys are distinct for distinct arguments.
     *  This means for the arguments {@code a1} and {@code a2} and the
     *  generated keys <code>k<sub>a1</sub></code> and
     *  <code>k<sub>a2</sub></code> that the following must be both
     *  {@code true}:
     *  <ul>
     *      <li><code>a1&nbsp;=&nbsp;a2&nbsp;&#x21d4;&nbsp;k<sub>a1</sub>&nbsp;=&nbsp;k<sub>a2</sub></code></li>
     *      <li><code>a1&nbsp;&#x2260;&nbsp;a2&nbsp;&#x21d4;&nbsp;k<sub>a1</sub>&nbsp;&#x2260;&nbsp;k<sub>a2</sub></code></li>
     *  </ul>
     *  This also follows that {@code getKey()} for the same argument must
     *  provide always the same key.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: Comparators.java 820 2020-12-29 20:34:22Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     *
     *  @param  <T> The type to order.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *
     *  @UMLGraph.link
     */
    @API( status = STABLE, since = "0.0.5" )
    @ClassVersion( sourceVersion = "$Id: Comparators.java 820 2020-12-29 20:34:22Z tquadrat $" )
    @FunctionalInterface
    public interface KeyProvider<T,K>
    {
            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Returns the sort order key for the given instance.
         *
         *  @param  instance    The instance; may be {@code null}.
         *  @return The respective sort order key; will be {@code null} if
         *      the {@code instance} was {@code null}.
         */
        public K getKey( T instance );
    }
    //  interface KeyProvider

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private Comparators() { throw new PrivateConstructorForStaticClassCalledError( Comparators.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns a comparator that compares Strings ignoring the case.<br>
     *  <br>Internally this is
     *  {@link String#CASE_INSENSITIVE_ORDER},
     *  with the limitations described there.
     *
     *  @return The comparator.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Comparator<String> caseInsensitiveComparator() { return CASE_INSENSITIVE_ORDER; }

    /**
     *  Returns a dummy comparator for elements that implements
     *  {@link Comparable}.
     *  Use this comparator if a
     *  {@link Comparator}
     *  is required for some reason, but your objects can be compared already
     *  directly by their inherent
     *  {@link Comparable#compareTo(Object)}
     *  method.<br>
     *  <br>A
     *  {@link NullPointerException}
     *  (and not a
     *  {@link org.tquadrat.foundation.exception.NullArgumentException})
     *  is thrown, if the first argument is {@code null}. Whether an exception
     *  is thrown when the second argument is {@code null} depends on the
     *  implementation of
     *  {@link Comparable#compareTo(Object) T.compareTo(Object)}.
     *
     *  @param  <T> The type to compare.
     *  @return The comparator.
     *
     *  @see Comparator#naturalOrder()
     *
     *  @deprecated Use
     *      {@link Comparator#naturalOrder()}
     *      instead.
     */
    @API( status = STABLE, since = "0.0.5" )
    @Deprecated( since = "0.1.0", forRemoval = true )
    public static final <T extends Comparable<? super T>> Comparator<T> comparableComparator()
    {
        final Comparator<T> retValue = naturalOrder();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  comparableComparator()

    /**
     *  Returns a comparator that generates a sort key from the elements before
     *  sorting them.<br>
     *  <br>The implementation of this comparator generates the sort keys for
     *  each and every comparison. This is obviously not very efficient. A more
     *  efficient implementation is provided by
     *  {@link #keyBasedComparator(KeyProvider, Comparator, boolean)}
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keyProvider The method that generates the sort key.
     *  @return The comparator.
     *
     *  @note The key provider must return {@code null} for a {@code null}
     *      value, as described for
     *      {@link KeyProvider#getKey(Object)}.
     *
     *  @see KeyProvider
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T, K extends Comparable<K>> Comparator<T> keyBasedComparator( final KeyProvider<? super T, K> keyProvider )
    {
        requireNonNullArgument( keyProvider, "keyProvider" );
        final Comparator<T> retValue = (o1,o2) -> signum( keyProvider.getKey( o1 ).compareTo( keyProvider.getKey( o2 ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  keyBasedComparator()

    /**
     *  A comparator that generates a sort key from the elements before sorting
     *  them.<br>
     *  <br>As it is not very efficient to generate the sort keys for each and
     *  every comparison, it is possible to cache them.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keyProvider The method that generates the sort key.
     *  @param  keyComparator   The comparator that determines the order for
     *      the keys.
     *  @param  cacheKeys   A flag that determines whether the keys are to be
     *      cached internally; {@code true} means they are kept, {@code false}
     *      means that the keys are generated newly for each comparison.
     *  @return The comparator.
     *
     *  @note The key provider must return {@code null} for a {@code null}
     *      value, as described for
     *      {@link KeyProvider#getKey(Object)}.
     *  @note The internally used cache will be kept for later uses of the
     *      comparator; this means that the key provider may not change its
     *      behaviour meanwhile.
     *  @note The internal cache is based on an
     *      {@link java.util.IdentityHashMap IdentityHashMap}
     *      with instances of the argument type <code>&lt;T&gt;</code> as the
     *      key. If the key generation through the key provider will provide
     *      equal keys for non-identical, but otherwise equal arguments, the
     *      cache may not be efficient.
     *
     *  @see KeyProvider
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T, K> Comparator<T> keyBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<K> keyComparator, final boolean cacheKeys )
    {
        return new KeyBasedComparator<>( keyProvider, keyComparator, cacheKeys );
    }   //  keyBasedComparator()

    /**
     *  Sometimes a special sort order is required that cannot be defined as a
     *  rule. Instead a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys. Values that are not on
     *  that list will be placed to the end, ordered according to their natural
     *  order if they or their keys implement the
     *  {@link Comparable}
     *  interface, or without any specific order.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final K... keys ) { return new ListBasedComparator<>( keys ); }

    /**
     *  Sometimes a special sort order is required that cannot defined as a
     *  rule. Instead a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys. Values that are not on
     *  that list will be placed to the end, ordered according to their natural
     *  order if they or their keys implement the
     *  {@link Comparable}
     *  interface, or without any specific order.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final List<K> keys ) { return new ListBasedComparator<>( keys ); }

    /**
     *  Sometimes a special sort order is required that cannot defined as a
     *  rule. Instead a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keyProvider The implementation of
     *      {@link Comparators.KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed; if {@code null}, those are ordered randomly in
     *      a non-consistent way if they or their keys do not implement the
     *      {@link Comparable}
     *      interface.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final KeyProvider<T,K> keyProvider, final Comparator<? super T> comparator, final List<K> keys )
    {
        return new ListBasedComparator<>( keyProvider, comparator, keys );
    }   //  listBasedComparator()

    /**
     *  Sometimes a special sort order is required that cannot defined as a
     *  rule. Instead a list defines the sequence. This method creates a new
     *  {@link Comparator}
     *  instance that works on the given list of keys.
     *
     *  @param  <T> The type to compare.
     *  @param  <K> The key type that is used to determine the order; this may
     *      be the same as the type itself.
     *  @param  keyProvider The implementation of
     *      {@link org.tquadrat.foundation.util.Comparators.KeyProvider}
     *      that returns the sort keys for the instances to compare.
     *  @param  comparator  The comparator that is used to order the instances
     *      that are not listed; if {@code null}, those are ordered randomly in
     *      a non-consistent way if they or their keys do not implement the
     *      {@link Comparable}
     *      interface.
     *  @param  keys    The sort order keys.
     *  @return The comparator.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static final <T,K> Comparator<T> listBasedComparator( final KeyProvider<T, ? super K> keyProvider, final Comparator<? super T> comparator, final K... keys )
    {
        return new ListBasedComparator<>( keyProvider, comparator, keys );
    }   //  ListBasedComparator()

    /**
     *  A comparator for numeric values.
     *
     *  @param  <T> The type to compare.
     *  @return The comparator.
     *
     *  @since 0.1.0
     */
    @SuppressWarnings( {"preview", "rawtypes"} )
    @API( status = STABLE, since = "0.1.0" )
    public static final <T extends Number> Comparator<T> numberComparator()
    {
        final Comparator<T> retValue = (v1,v2) ->
        {
            //noinspection rawtypes
            if( v1 instanceof Comparable comparable )
            {
                @SuppressWarnings( "unchecked" )
                final var result = signum( comparable.compareTo( v2 ) );
                return result;
            }
            return (int) Math.signum( v1.doubleValue() - v2.doubleValue() );
        };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  numberComparator()
}
//  class Comparators

/*
 *  End of File
 */