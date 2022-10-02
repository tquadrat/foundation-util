/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

import static java.lang.System.arraycopy;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.stream.IntStream;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.lang.Objects;

/**
 *  This class provides some utility functions that extends
 *  {@link java.util.Arrays}
 *  to some extent. All methods are static, no objects of this class are
 *  allowed.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ArrayUtils.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@UtilityClass
@ClassVersion( sourceVersion = "$Id: ArrayUtils.java 1032 2022-04-10 17:27:44Z tquadrat $" )
public final class ArrayUtils
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The illegal index message.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String MSG_IllegalIndex = "The starting index '%1$d' is invalid";

    /**
     *  "The message indicates that the given value is not an array."
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String MSG_NoArray = "The argument is not an Array: %1$s";

    /**
     *  The message indicates that the given value set is empty.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String MSG_NoValues = "No values were provided";

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Prevent the creation of any object of this class.
     */
    private ArrayUtils() { throw new PrivateConstructorForStaticClassCalledError( ArrayUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Translates the given array to an array of element type
     *  {@link Object}.
     *  Primitive types will be automatically converted into the respective
     *  Wrapper types.<br>
     *  <br>The array elements will not be copied.
     *
     *  @param  array   The array to translate.
     *  @return The translated array.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static Object [] toObjectArray( final Object array )
    {
        if( !requireNonNullArgument( array, "array" ).getClass().isArray() )
        {
            throw new IllegalArgumentException( format( MSG_NoArray, array.getClass().getName() ) );
        }

        final var length = Array.getLength( array );
        final var retValue = IntStream.range( 0, length ).mapToObj( i -> Array.get( array, i ) ).toArray();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toObjectArray()

    /**
     *  This method checks if the array of objects that is provided as the
     *  second parameter contains an object that is equal to that one provided
     *  as the first parameter. The test is made using
     *  {@link Comparable#compareTo(Object) compareTo()}.
     *  For a test on object identity, using {@code ==}, use
     *  {@link #isIn(Object,Object...) isIn()}.
     *
     *  @param <T>  The class for the objects.
     *  @param  o   The object to look for.
     *  @param  list    The list of object to look into.
     *  @return {@code true} if the object is in the list,
     *      {@code false} otherwise.
     *  @throws NullPointerException At least one entry of {@code list} is
     *      {@code null}.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T> boolean isComparableIn( final Comparable<? super T> o, final T... list )
    {
        requireNonNullArgument( o, "o" );
        final var retValue = stream( requireNonNullArgument( list, "list" ) ).anyMatch( e -> o.compareTo( e ) == 0 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isComparableIn()

    /**
     *  This method checks if the array of objects that is provided as the
     *  second parameter contains an object that is equal to that one provided
     *  as the first parameter. The test is made using
     *  {@link Object#equals(Object) equals()}.
     *  For a test on object identity, using {@code ==}, use
     *  {@link #isIn(Object, Object...) isIn()}.
     *
     *  @param <T>  The class for the objects.
     *  @param  o   The object to look for.
     *  @param  list    The list of object to look into.
     *  @return {@code true} if the object is in the list,
     *      {@code false} otherwise.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T> boolean isEqualIn( final T o, final T... list )
    {
        requireNonNullArgument( o, "o" );
        final var retValue = asList( requireNonNullArgument( list, "list" ) ).contains( o );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isEqualIn()

    /**
     *  This method checks if the given object is in the list of objects that
     *  is provided as the second parameter. The test is made on object
     *  identity, using {@code ==}. For a test with
     *  {@link Object#equals(Object) equals()},
     *  use
     *  {@link #isEqualIn(Object, Object...) isEqualIn()}
     *
     *  @param <T>  The class for the objects.
     *  @param  o   The object to look for; may be {@code null}.
     *  @param  list    The list of object to look into.
     *  @return {@code true} if the object is in the list,
     *      {@code false} otherwise.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T> boolean isIn( final T o, final T... list )
    {
        final var retValue = stream( requireNonNullArgument( list, "list" ) ).anyMatch( e -> o == e );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  This method checks if the given value is at least once in the list that
     *  is provided as the second parameter.
     *
     *  @param  v   The value to look for.
     *  @param  list    The list of values to look into.
     *  @return {@code true} if the value is in the list,
     *      {@code false} otherwise.
     */
    @SuppressWarnings( "ImplicitNumericConversion" )
    @API( status = STABLE, since = "0.0.5" )
    public static boolean isIn( final byte v, final byte... list )
    {
        requireNonNullArgument( list, "list" );
        var retValue = false;
        for( var i = 0; (i < list.length) && !retValue; retValue = v == list [i++] )
        { /* Does nothing! */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  This method checks if the given value is at least once in the list that
     *  is provided as the second parameter.
     *
     *  @param  v   The value to look for.
     *  @param  list    The list of values to look into.
     *  @return {@code true} if the value is in the list,
     *      {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static boolean isIn( final char v, final char... list )
    {
        requireNonNullArgument( list, "list" );
        var retValue = false;
        for( var i = 0; (i < list.length) && !retValue; retValue = v == list [i++] )
        { /* Does nothing! */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  This method checks if the given value is at least once in the list that
     *  is provided as the second parameter. The test is made on identity,
     *  using {@code ==}; this means, due to the nature of {@code double}, that
     *  values may not be found, although they <i>appear</i> to be equal.
     *
     *  @param  v   The value to look for.
     *  @param  list    The list of values to look into.
     *  @return {@code true} if the value is in the list,
     *      {@code false} otherwise.
     */
    @SuppressWarnings( "FloatingPointEquality" )
    @API( status = STABLE, since = "0.0.5" )
    public static boolean isIn( final double v, final double... list )
    {
        requireNonNullArgument( list, "list" );
        var retValue = false;
        for( var i = 0; (i < list.length) && !retValue; retValue = v == list [i++] )
        { /* Does nothing! */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  This method checks if the given value is at least once in the list that
     *  is provided as the second parameter. The test is made on identity,
     *  using {@code ==}; this means, due to the nature of {@code float}, that
     *  values may not be found, although they <i>appear</i> to be equal.
     *
     *  @param  v   The value to look for.
     *  @param  list    The list of values to look into.
     *  @return {@code true} if the value is in the list,
     *      {@code false} otherwise.
     */
    @SuppressWarnings( "FloatingPointEquality" )
    @API( status = STABLE, since = "0.0.5" )
    public static boolean isIn( final float v, final float... list )
    {
        requireNonNullArgument( list, "list" );
        var retValue = false;
        for( var i = 0; (i < list.length) && !retValue; retValue = v == list [i++] )
        { /* Does nothing! */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  This method checks if the given value is at least once in the list that
     *  is provided as the second parameter.
     *
     *  @param  v   The value to look for.
     *  @param  list    The list of values to look into.
     *  @return {@code true} if the value is in the list,
     *      {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static boolean isIn( final int v, final int... list )
    {
        requireNonNullArgument( list, "list" );
        var retValue = false;
        for( var i = 0; (i < list.length) && !retValue; retValue = v == list [i++] )
        { /* Does nothing! */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  This method checks if the given value is at least once in the list that
     *  is provided as the second parameter.
     *
     *  @param  v   The value to look for.
     *  @param  list    The list of values to look into.
     *  @return {@code true} if the value is in the list,
     *      {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static boolean isIn( final long v, final long... list )
    {
        requireNonNullArgument( list, "list" );
        var retValue = false;
        for( var i = 0; (i < list.length) && !retValue; retValue = v == list [i++] )
        { /* Does nothing! */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  This method checks if the given value is at least once in the list that
     *  is provided as the second parameter.
     *
     *  @param  v   The value to look for.
     *  @param  list    The list of values to look into.
     *  @return {@code true} if the value is in the list,
     *      {@code false} otherwise.
     */
    @SuppressWarnings( "ImplicitNumericConversion" )
    @API( status = STABLE, since = "0.0.5" )
    public static boolean isIn( final short v, final short... list )
    {
        requireNonNullArgument( list, "list" );
        var retValue = false;
        for( var i = 0; (i < list.length) && !retValue; retValue = v == list [i++] )
        { /* Does nothing! */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isIn()

    /**
     *  Determines the greatest value in the given array, based on the given
     *  {@link Comparator}.
     *
     *  @param <T>  The class for the objects.
     *  @param  comparator  The comparator to use.
     *  @param  list  The values.
     *  @return The greatest value in the list.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.1.0" )
    public static <T> T max( final Comparator<? super T> comparator, final T... list )
    {
        requireNonNullArgument( comparator, "comparator" );
        final var retValue = stream( requireNonNullArgument( list, "list" ) ).max( comparator ).orElse( null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  max()

    /**
     *  Determines the greatest value in the given array.
     *
     *  @param <T>  The class for the objects.
     *  @param  list  The values.
     *  @return The greatest value in the list.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T extends Comparable<T>> T max( final T... list )
    {
        final var retValue = stream( requireNonNullArgument( list, "list" ) ).max( naturalOrder() ).orElse( null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  max()

    /**
     *  Determines the greatest value in the given array.
     *
     *  @param  list  The values.
     *  @return The greatest value in the list.
     */
    @SuppressWarnings( "CharacterComparison" )
    @API( status = STABLE, since = "0.0.5" )
    public static char max( final char... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] > retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  max()

    /**
     *  Determines the greatest value in the given array.
     *
     *  @param  list  The values.
     *  @return The greatest value in the list.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static double max( final double... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] > retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  max()

    /**
     *  Determines the greatest value in the given array.
     *
     *  @param  list  The values.
     *  @return The greatest value in the list.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static int max( final int... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] > retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  max()

    /**
     *  Determines the greatest value in the given array.
     *
     *  @param  list  The values.
     *  @return The greatest value in the list.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static long max( final long... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] > retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  max()

    /**
     *  Determines the smallest value in the given array, based on the given
     *  {@link Comparator}.
     *
     *  @param <T>  The class for the objects.
     *  @param  comparator  The comparator.
     *  @param  list  The values.
     *  @return The smallest value in the list.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T> T min( final Comparator<? super T> comparator,  final T... list )
    {
        requireNonNullArgument( comparator, "comparator" );
        final var retValue = stream( requireNonNullArgument( list, "list" ) ).min( comparator ).orElse( null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  min()

    /**
     *  Determines the smallest value in the given array.
     *
     *  @param <T>  The class for the objects.
     *  @param  list  The values.
     *  @return The smallest value in the list.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T extends Comparable<T>> T min( final T... list )
    {
        final var retValue = stream( requireNonNullArgument( list, "list" ) ).min( naturalOrder() ).orElse( null );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  min()

    /**
     *  Determines the smallest value in the given array.
     *
     *  @param  list  The values.
     *  @return The smallest value in the list.
     */
    @SuppressWarnings( "CharacterComparison" )
    @API( status = STABLE, since = "0.0.5" )
    public static char min( final char... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] < retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  min()

    /**
     *  Determines the smallest value in the given array.
     *
     *  @param  list  The values.
     *  @return The smallest value in the list.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static double min( final double... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] < retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  min()

    /**
     *  Determines the smallest value in the given array.
     *
     *  @param  list  The values.
     *  @return The smallest value in the list.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static int min( final int... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] < retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  min()

    /**
     *  Determines the smallest value in the given array.
     *
     *  @param  list  The values.
     *  @return The smallest value in the list.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static long min( final long... list )
    {
        final var len = requireNonNullArgument( list, "list" ).length;
        if( 0 == len )
        {
            throw new IllegalArgumentException( MSG_NoValues );
        }
        var retValue = list [0];
        for( var i = 1; i < len; ++i )
        {
            if( list [i] < retValue )
            {
                retValue = list [i];
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  min()

    /**
     *  Modifies the given array to contain the elements in reverted order.
     *
     *  @param  <T> The type of the array elements.
     *  @param  a The array to revert.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T> void revert( final T... a )
    {
        final var len = requireNonNullArgument( a, "a" ).length;
        if( len > 1 )
        {
            int pos;
            for( var i = 0; i < (len / 2); ++i )
            {
                pos = len - 1 - i;
                final var temp = a [i];
                a [i] = a [pos];
                a [pos] = temp;
            }
        }
    }   //  revert()

    /**
     *  Returns a copy of the given array with the elements in reverted order.
     *
     *  @param  <T> The type of the array elements.
     *  @param  a The array to revert.
     *  @return The copy in reverted order.
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.0.5" )
    public static <T> T [] revertCopy( final T... a )
    {
        final var retValue = requireNonNullArgument( a, "a" ).clone();
        revert( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  revertCopy()

    /**
     *  Returns a part of the given array as a copy.<br>
     *  <br>The semantics of this method are slightly different from that in
     *  {@link java.util.Arrays}.
     *
     *  @param  <T> The type of the array elements.
     *  @param  source  The source array.
     *  @param  start   The start index.
     *  @param  len The number of elements for the partial array.
     *  @return The new array.
     *
     *  @see java.util.Arrays#copyOfRange(Object[], int, int)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <T> T [] subArray( final T [] source, final int start, final int len )
    {
        if( start < 0 ) throw new IndexOutOfBoundsException( format( MSG_IllegalIndex, start ) );

        final Class<?> sourceClass = requireNonNullArgument( source, "source" ).getClass();
        if( !sourceClass.isArray() ) throw new IllegalArgumentException( format( MSG_NoArray, sourceClass.getName() ) );
        final var size = Math.min( len, source.length - start );
        if( size < 0 ) throw new IndexOutOfBoundsException( format( MSG_IllegalIndex, size ) );
        @SuppressWarnings( "unchecked" )
        final var retValue = (T []) Array.newInstance( sourceClass.getComponentType(), size );
        arraycopy( source, start, retValue, 0, size );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  subArray()

    /**
     *  Returns a part of the given array as a copy, beginning with the given
     *  start position to the end of the array.
     *
     *  @param  <T> The type of the array elements.
     *  @param  source  The source array.
     *  @param  start   The start index.
     *  @return The new array.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <T> T [] subArray( final T [] source, final int start )
    {
        final var retValue = subArray( source, start, Integer.MAX_VALUE );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  subArray()

    /**
     *  Renders the given array to a single string where the elements are
     *  separated by the given character sequence.<br>
     *  <br>This method allows to control the output a bit better than
     *  {@link java.util.Arrays#toString(Object[])},
     *  {@link java.util.Objects#toString(Object)},
     *  or
     *  {@link org.tquadrat.foundation.lang.Objects#toString(Object)}.
     *  Especially it does not frame the output with those square brackets ...
     *
     *  @param  array   The input array.
     *  @param  separator   The separator character sequence; if
     *      {@code null}, the empty string is used.
     *  @return The concatenated string.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static String toString( final Object [] array, final CharSequence separator )
    {
        var retValue = EMPTY_STRING;
        if( array.length > 0 )
        {
            @SuppressWarnings( "LocalVariableNamingConvention" )
            final var s = array.length > 1
                ? nonNull( separator )
                    ? separator.toString()
                    : EMPTY_STRING
                : null;
            retValue = IntStream.range( 1, array.length )
                .mapToObj( i -> s + Objects.toString( array [i] ) )
                .collect( joining( "", Objects.toString( array [0] ), "" ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ArrayUtils

/*
 *    End of File
 */