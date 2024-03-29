/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import java.util.HexFormat;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.ValidationException;

/**
 *  <p>{@summary A set of utility methods that are useful for the conversion of
 *  byte arrays to and from strings of hexadecimal digits.}</p>
 *  <p>Parts of the code were adopted from the class
 *  {@code org.apache.catalina.util.HexUtils} (written by Craig R. McClanahan)
 *  out of the Tomcat source and modified to match the requirements of this
 *  project.</p>
 *  <p>Partially, the methods got obsolete with the introduction of
 *  {@link java.util.HexFormat}
 *  in Java&nbsp;17.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Craig R. McClanahan
 *  @version $Id: HexUtils.java 1086 2024-01-05 23:18:33Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: HexUtils.java 1086 2024-01-05 23:18:33Z tquadrat $" )
@UtilityClass
public final class HexUtils
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance of this class is allowed.
     */
    private HexUtils() { throw new PrivateConstructorForStaticClassCalledError( HexUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Converts a String of hexadecimal digits into the
     *  corresponding byte array by encoding each two hexadecimal digits as a
     *  byte.} The method will not distinguish between upper or lower case for
     *  the digit from {@code 0xA} to {@code 0xF}.</p>
     *  <p>If the number of digits in the String is odd, the String will be
     *  <i>prefixed</i> by an additional 0.</p>
     *
     *  @param  digits  Hexadecimal digits representation.
     *  @return The resulting byte array.
     *  @throws IllegalArgumentException The input String is {@code null},
     *      empty, or it contains an invalid character that cannot be
     *      interpreted as a hexadecimal digit.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static byte[] convertFromHexString( final CharSequence digits )
    {
        final var len = requireNotEmptyArgument( digits, "digits" ).length();
        final var buffer = new StringBuilder( len + 1 );
        if( len % 2 > 0 ) buffer.append( '0' );
        buffer.append( digits );
        final var retValue = HexFormat.of().parseHex( buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertFromHexString()

    /**
     *  Converts an array of hexadecimal digits into the corresponding byte
     *  array by encoding each two hexadecimal digits as a byte. The method
     *  will not distinguish between upper or lower case for the digit from
     *  {@code 0xA} to {@code 0xF}.<br>
     *  <br>If the number of digits in the array is odd, an additional 0 will
     *  <i>prepended</i> to it.
     *
     *  @param  digits  Hexadecimal digits representation.
     *  @return The resulting byte array.
     *  @throws IllegalArgumentException The input array is {@code null},
     *      empty, or it contains an invalid character that cannot be
     *      interpreted as a hexadecimal digit.
     */
    @SuppressWarnings( "MethodCanBeVariableArityMethod" )
    @API( status = STABLE, since = "0.0.5" )
    public static final byte[] convertFromHexCharArray( final char [] digits )
    {
        final var retValue = convertFromHexString( new String( requireNotEmptyArgument( digits, "digits" ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertFromHexCharArray()

    /**
     *  Converts an integer in the range form 0 to 15 to a hex digit.
     *
     *  @param  value   The value to convert.
     *  @return The hex digit (uppercase).
     */
    @SuppressWarnings( "MagicNumber" )
    @API( status = STABLE, since = "0.0.5" )
    public static final char convertToHexDigit( final int value )
    {
        if( (value < 0) || (value > 15) )
        {
            throw new ValidationException( "The value %1$d cannot be converted to a single HexDigit".formatted( value ) );
        }

        final var retValue = HexFormat.of().withUpperCase().toLowHexDigit( value );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertToHexDigit()
}
//  class HexUtils

/*
 *  End of File
 */