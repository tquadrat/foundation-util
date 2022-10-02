/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
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

import java.util.Comparator;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  <p>{@summary Several range checking functions.}</p>
 *  <p>The functions in this class support range checking for various types.
 *  This can be especially useful for scripting, but is also usable in other
 *  contexts.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Ben Gidley
 *  @version $Id: RangeFunctions.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.7
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@UtilityClass
@ClassVersion( sourceVersion = "$Id: RangeFunctions.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.0.7" )
public final class RangeFunctions
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance is allowed for this class.
     */
    private RangeFunctions() { throw new PrivateConstructorForStaticClassCalledError( RangeFunctions.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns {@code true} if {@code value} is above {@code floor}.
     *
     *  @param  <T> The type of the values to check.
     *  @param  value   The value to compare.
     *  @param  floor   The border value.
     *  @param  include {@code true} if the border is included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is greater than the given
     *      border value, {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;greater or equal&quot;,
     *      respectively.
     */
    public static <T> boolean isAbove( final Comparable<T> value, final T floor, final boolean include )
    {
        requireNonNullArgument( value, "value" );
        requireNonNullArgument( floor, "floor" );

        final var retValue = include ?
            value.compareTo( floor ) >= 0 :
            value.compareTo( floor ) > 0;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isAbove()

    /**
     *  Returns {@code true} if {@code value} is above {@code floor}.
     *
     *  @param  <T> The type of the values to check.
     *  @param  comparator  The comparator that is used for the comparison.
     *  @param  value   The value to compare.
     *  @param  floor   The border value.
     *  @param  include {@code true} if the border is included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is greater than the given
     *      border value, {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;greater or equal&quot;,
     *      respectively.
     */
    public static <T> boolean isAbove( final Comparator<T> comparator, final T value, final T floor, final boolean include )
    {
        requireNonNullArgument( comparator, "comparator" );
        requireNonNullArgument( value, "value" );
        requireNonNullArgument( floor, "floor" );

        final var retValue = include ?
            comparator.compare( value, floor ) >= 0 :
            comparator.compare( value, floor ) > 0;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isAbove()

    /**
     *  Returns {@code true} if {@code value} is above {@code floor}. If
     *  {@code include} is {@code true}, {@code floor} is included,
     *  otherwise it is not part of the range.
     *
     *  @param  <T> The type of the values to check.
     *  @param  value   The value to compare.
     *  @param  floor   The border value.
     *  @return {@code true} if the given value is greater than the given
     *      border value, {@code false} otherwise.
     */
    public static <T> boolean isAbove( final Comparable<T> value, final T floor )
    {
        final var retValue = isAbove( value, floor, false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isAbove()

    /**
     *  Returns {@code true} if {@code value} is above {@code floor}. If
     *  {@code include} is {@code true}, {@code floor} is included,
     *  otherwise it is not part of the range.
     *
     *  @param  <T> The type of the values to check.
     *  @param  comparator  The comparator that is used for the comparison.
     *  @param  value   The value to compare.
     *  @param  floor   The border value.
     *  @return {@code true} if the given value is greater than the given
     *      border value, {@code false} otherwise.
     */
    public static <T> boolean isAbove( final Comparator<T> comparator, final T value, final T floor )
    {
        final var retValue = isAbove( comparator, value, floor, false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isAbove()

    /**
     *  Returns {@code true} if {@code value} is above {@code floor}.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The border value.
     *  @return {@code true} if the given value is greater than the given
     *      border value, {@code false} otherwise.
     */
    @SuppressWarnings( "CharacterComparison" )
    public static boolean isAbove( final char value, final char floor ) { return value > floor; }

    /**
     *  Returns {@code true} if {@code value} is above {@code floor}.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The border value.
     *  @return {@code true} if the given value is greater than the given
     *      border value, {@code false} otherwise.
     */
    public static boolean isAbove( final double value, final double floor ) { return value > floor; }

    /**
     *  Returns {@code true} if {@code value} is above {@code floor}.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The border value.
     *  @return {@code true} if the given value is greater than the given
     *      border value, {@code false} otherwise.
     */
    public static boolean isAbove( final long value, final long floor ) { return value > floor; }

    /**
     *  Returns {@code true} if {@code value} is below {@code ceiling}.
     *  If {@code include} is {@code true}, {@code ceiling} is included,
     *  otherwise it is not part of the range.
     *
     *  @param  <T> The type of the values to check.
     *  @param  value   The value to compare.
     *  @param  ceiling The border value.
     *  @param  include {@code true} if the border is included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is less than the given
     *      border value, {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;less or equal&quot;,
     *      respectively.
     */
    public static <T> boolean isBelow( final Comparable<T> value, final T ceiling, final boolean include )
    {
        requireNonNullArgument( value, "value" );
        requireNonNullArgument( ceiling, "ceiling" );

        final var retValue = include ?
            value.compareTo( ceiling ) <= 0 :
            value.compareTo( ceiling ) < 0;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBelow()

    /**
     *  Returns {@code true} if {@code value} is below {@code ceiling}.
     *  If {@code include} is {@code true}, {@code ceiling} is included,
     *  otherwise it is not part of the range.
     *
     *  @param  <T> The type of the values to check.
     *  @param  comparator  The comparator that is used for the comparison.
     *  @param  value   The value to compare.
     *  @param  ceiling The border value.
     *  @param  include {@code true} if the border is included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is less than the given
     *      border value, {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;less or equal&quot;,
     *      respectively.
     */
    public static <T> boolean isBelow( final Comparator<T> comparator, final T value, final T ceiling, final boolean include )
    {
        requireNonNullArgument( comparator, "comparator" );
        requireNonNullArgument( value, "value" );
        requireNonNullArgument( ceiling, "ceiling" );

        final var retValue = include ?
            comparator.compare( value, ceiling ) <= 0 :
            comparator.compare( value, ceiling ) < 0;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBelow()

    /**
     *  Returns {@code true} if {@code value} is below {@code ceiling}.
     *
     *  @param  <T> The type of the values to check.
     *  @param  value   The value to compare.
     *  @param  ceiling The border value.
     *  @return {@code true} if the given value is less than the given
     *      border value, {@code false} otherwise.
     */
    public static <T> boolean isBelow( final Comparable<T> value, final T ceiling )
    {
        final var retValue = isBelow( value, ceiling, false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBelow()

    /**
     *  Returns {@code true} if {@code value} is below {@code ceiling}.
     *
     *  @param  <T> The type of the values to check.
     *  @param  comparator  The comparator that is used for the comparison.
     *  @param  value   The value to compare.
     *  @param  ceiling The border value.
     *  @return {@code true} if the given value is less than the given
     *      border value, {@code false} otherwise.
     */
    public static <T> boolean isBelow( final Comparator<T> comparator, final T value, final T ceiling )
    {
        final var retValue = isBelow( comparator, value, ceiling, false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBelow()

    /**
     *  Returns {@code true} if {@code value} is below {@code ceiling}.
     *
     *  @param  value   The value to compare.
     *  @param  ceiling The border value.
     *  @return {@code true} if the given value is less than the given
     *      border value, {@code false} otherwise.
     */
    @SuppressWarnings( "CharacterComparison" )
    public static boolean isBelow( final char value, final char ceiling ) { return value < ceiling; }

    /**
     *  Returns {@code true} if {@code value} is below {@code ceiling}.
     *
     *  @param  value   The value to compare.
     *  @param  ceiling The border value.
     *  @return {@code true} if the given value is less than the given
     *      border value, {@code false} otherwise.
     */
    public static boolean isBelow( final double value, final double ceiling ) { return value < ceiling; }

    /**
     *  Returns {@code true} if {@code value} is below {@code ceiling}.
     *
     *  @param  value   The value to compare.
     *  @param  ceiling The border value.
     *  @return {@code true} if the given value is less than the given
     *      border value, {@code false} otherwise.
     */
    public static boolean isBelow( final long value, final long ceiling ) { return value < ceiling; }

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}. If {@code include} is {@code true}, {@code floor}
     *  and {@code ceiling} are included, otherwise they are not in the range.
     *
     *  @param  <T> The type of the values to check.
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @param  include {@code true} if the borders are included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;greater or equal&quot; and
     *      &quot;less or equal&quot;, respectively.
     */
    public static <T> boolean isBetween( final Comparable<T> value, final T floor, final T ceiling, final boolean include )
    {
        final var retValue = isAbove( value, floor, include ) && isBelow( value, ceiling, include );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBetween()

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}. If {@code include} is {@code true}, {@code floor}
     *  and {@code ceiling} are included, otherwise they are not in the range.
     *
     *  @param  <T> The type of the values to check.
     *  @param  comparator  The comparator that is used for the comparison.
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @param  include {@code true} if the borders are included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;greater or equal&quot; and
     *      &quot;less or equal&quot;, respectively.
     */
    public static <T> boolean isBetween( final Comparator<T> comparator, final T value, final T floor, final T ceiling, final boolean include )
    {
        final var retValue = isAbove( comparator, value, floor, include ) && isBelow( comparator, value, ceiling, include );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBetween()

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}. If {@code include} is {@code true}, {@code floor}
     *  and {@code ceiling} are included, otherwise they are not in the range.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @param  include {@code true} if the borders are included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;greater or equal&quot; and
     *      &quot;less or equal&quot;, respectively.
     */
    @SuppressWarnings( "CharacterComparison" )
    public static boolean isBetween( final char value, final char floor, final char ceiling, final boolean include )
    {
        final var retValue = include ?
            (value <= ceiling) && (value >= floor) :
            (value < ceiling) && (value > floor);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBetween()

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}. If {@code include} is {@code true}, {@code floor}
     *  and {@code ceiling} are included, otherwise they are not in the range.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @param  include {@code true} if the borders are included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;greater or equal&quot; and
     *      &quot;less or equal&quot;, respectively.
     */
    public static boolean isBetween( final double value, final double floor, final double ceiling, final boolean include )
    {
        final var retValue = include ?
            (value <= ceiling) && (value >= floor) :
            (value < ceiling) && (value > floor);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBetween()

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}. If {@code include} is {@code true}, {@code floor}
     *  and {@code ceiling} are included, otherwise they are not in the range.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @param  include {@code true} if the borders are included,
     *      {@code false} if not.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise. If {@code include} is
     *      {@code true}, it will be &quot;greater or equal&quot; and
     *      &quot;less or equal&quot;, respectively.
     */
    public static boolean isBetween( final long value, final long floor, final long ceiling, final boolean include )
    {
        final var retValue = include ?
            (value <= ceiling) && (value >= floor) :
            (value < ceiling) && (value > floor);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBetween()

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}.
     *
     *  @param  <T> The type of the values to check.
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise.
     */
    public static <T> boolean isBetween( final Comparable<T> value, final T floor, final T ceiling )
    {
        final var retValue = isBetween( value, floor, ceiling, false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBetween()

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}.
     *
     *  @param  <T> The type of the values to check.
     *  @param  comparator  The comparator that is used for the comparison.
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise.
     */
    public static <T> boolean isBetween( final Comparator<T> comparator, final T value, final T floor, final T ceiling )
    {
        final var retValue = isBetween( comparator, value, floor, ceiling, false );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isBetween()

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise.
     */
    public static boolean isBetween( final char value, final char floor, final char ceiling ) { return isBetween( value, floor, ceiling, false ); }

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise.
     */
    public static boolean isBetween( final double value, final double floor, final double ceiling ) { return isBetween( value, floor, ceiling, false ); }

    /**
     *  Returns {@code true} if {@code value} is between {@code floor} and
     *  {@code ceiling}.
     *
     *  @param  value   The value to compare.
     *  @param  floor   The lower border value.
     *  @param  ceiling The upper border value.
     *  @return {@code true} if the given value is greater than the given
     *      lower border value and less than the given upper border value,
     *      {@code false} otherwise.
     */
    public static boolean isBetween( final long value, final long floor, final long ceiling ) { return isBetween( value, floor, ceiling, false ); }
}
//  class RangeFunctions

/*
 *  End of File
 */