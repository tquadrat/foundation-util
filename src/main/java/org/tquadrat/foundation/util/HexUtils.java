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

import static java.lang.System.arraycopy;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.ValidationException;

/**
 *  Library of utility methods useful in dealing with converting byte arrays
 *  to and from strings of hexadecimal digits. <br>
 *  <br>Parts of the code were adopted from the class
 *  <code>org.apache.catalina.util.HexUtils</code> (written by Craig R.
 *  McClanahan) out of the Tomcat source and modified to match the requirements
 *  of this project.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Craig R. McClanahan
 *  @version $Id: HexUtils.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: HexUtils.java 820 2020-12-29 20:34:22Z tquadrat $" )
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
     *  Converts a String of hexadecimal digits into the corresponding byte
     *  array by encoding each two hexadecimal digits as a byte. The method
     *  will not distinguish between upper or lower case for the digit from
     *  {@code 0xA} to {@code 0xF}.<br>
     *  <br>If the number of digits in the String is odd, the String will be
     *  <i>prefixed</i> by an additional 0.
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
        final var digitsString = digits.toString();
        final var chars = ((len % 2) == 0) ? digitsString.toCharArray() : ("0" + digitsString).toCharArray();
        final var retValue = convertFromHexCharArray( chars );

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
    @SuppressWarnings( {"MethodCanBeVariableArityMethod", "MagicNumber"} )
    @API( status = STABLE, since = "0.0.5" )
    public static final byte[] convertFromHexCharArray( final char [] digits )
    {
        var len = requireNotEmptyArgument( digits, "digits" ).length;
        final var f = new byte [] {16, 1};

        char [] chars = null;
        if( (len % 2) == 0 )
        {
            chars = digits;
        }
        else
        {
            chars = new char [len + 1];
            chars [0] = '0';
            arraycopy( digits, 0, chars, 1, len );
            ++len;
        }
        final var retValue = new byte [len / 2];
        byte resultByte;
        char currentChar;
        var rIndex = 0;  //  Index for retValue array.
        int v;

        ScanLoop:for( var i = 0; i < len; i += 2 )
        {
            resultByte = 0;

            //---* Convert the digits *----------------------------------------
            ConvertLoop: for( var j = 0; j < 2; ++j )
            {
                currentChar = chars [i + j];
                v = Character.digit( currentChar, 0x10 );
                if( v >= 0 )
                {
                    resultByte += v * f [j];
                }
                else
                {
                    throw new IllegalArgumentException( format( "The HexString '%1$S' contains an invalid character: %2$S", new String( digits ), Character.toString( currentChar ) ) );
                }
            }   //  ConvertLoop:

            //---* Write the resulting byte *----------------------------------
            retValue [rIndex++] = resultByte;
        }   //  ScanLoop:

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
            throw new ValidationException( format( "The value %1$d cannot be converted to a single HexDigit", value ) );
        }

        final var temp = value & 15;
        final var retValue = (char) (temp >= 10 ? ((temp - 10) + 'A') : (temp + '0'));

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertToHexDigit()

    /**
     *  Converts an array of bytes to a hex string where the digits from
     *  {@code 0xA} to {@code 0xF} are all uppercase.
     *
     *  @param  bytes   The byte array.
     *  @return The converted string.
     *  @throws IllegalArgumentException    The given byte array is
     *      {@code null} or empty.
     */
    @SuppressWarnings( "MagicNumber" )
    @API( status = STABLE, since = "0.0.5" )
    public static final String convertToHexString( final byte[] bytes ) throws IllegalArgumentException
    {
        final var buffer = new StringBuilder( requireNotEmptyArgument( bytes, "bytes" ).length * 2 );
        int i;
        for( final var b : bytes )
        {
            //---* Make sure that the value is in the range 0 < value < 256 *--
            i = ((b + 256) % 256);

            //---* Convert *---------------------------------------------------
            buffer.append( convertToHexDigit( i >> 4 ) );
            buffer.append( convertToHexDigit( i & 15 ) );
        }
        final var retValue = buffer.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertToHexString()
}
//  class HexUtils

/*
 *  End of File
 */