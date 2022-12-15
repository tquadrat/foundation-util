/*
 * ============================================================================
 *  Copyright © 2002-2022 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util;

import static java.math.BigInteger.ZERO;
import static java.util.Arrays.fill;
import static java.util.Locale.ROOT;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.math.BigInteger;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.ValidationException;

/**
 *  <p>{@summary This class provides an Encoder and a Decoder for
 *  {@href https://www.crockford.com/base32.html Crockfords's Base&nbsp;32}
 *  format.}</p>
 *  <p>Base&nbsp;32 allows to represent large numbers as strings with less
 *  characters than Base&nbsp;10 (the regular decimal system) or Base&nbsp;16
 *  (the hexadecimal system). It has the advantage over Base&nbsp;64 that it
 *  uses less symbols, and no special characters.</p>
 *  <p>While Base&nbsp; is mainly for (large) numbers, it can be used for
 *  strings, to, in the same way as Base&nbsp;64. But different from that,
 *  Base&nbsp;32 is not really standardized; this version, introduced by
 *  Douglas Crockford, is just one among various others.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Douglas Crockford - douglas@crockford.com
 *  @version $Id: Base32.java 1037 2022-12-15 00:35:17Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"JavadocLinkAsPlainText", "MagicNumber"} )
@ClassVersion( sourceVersion = "$Id: Base32.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
@UtilityClass
public final class Base32
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary The Decoder for
     *  {@href https://www.crockford.com/base32.html Crockfords's Base&nbsp;32}
     *  format.}</p>
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @thanks Douglas Crockford - douglas@crockford.com
     *  @version $Id: Base32.java 1037 2022-12-15 00:35:17Z tquadrat $
     *  @since 0.1.0
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: Base32.java 1037 2022-12-15 00:35:17Z tquadrat $" )
    @API( status = STABLE, since = "0.1.0" )
    public static final class Decoder
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new instance of {@code Base32.Decoder}.
         */
        private Decoder() { /* Just exists */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Checks whether the given argument is a valid Basic&nbsp;32
         *  sequence and converts it into an array with the symbol values.
         *
         *  @param  src The data to check.
         *  @return The symbol values.
         *  @throws IllegalArgumentException    The input data is invalid.
         */
        private final long [] checkValid( final byte [] src )
        {
            requireNonNullArgument( src, "src" );
            final var retValue = new long [src.length];
            for( var i = 0; i < src.length; ++i )
            {
                final var value = ALPHABET_VALUES [(char) src [i]];
                if( value == -1 ) throw new ValidationException( "No valid Base32 sequence" );
                retValue [i] = value;
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  checkValid()

        /**
         *  <p>{@summary Decodes an array of Base&nbsp;32 symbols.}</p>
         *  <p>This is the converse operation to
         *  {@link Encoder#encode(byte[])}</p>
         *
         *  @param  src The input data.
         *  @return The decoded value.
         *  @throws ValidationException The input data is not a valid
         *      Base&nbsp;32 sequence
         */
        public final byte [] decode( final byte [] src ) throws ValidationException
        {
            var retValue = EMPTY_byte_ARRAY;
            if( requireNonNullArgument( src, "src" ).length > 0 )
            {
                final var buffer = decodeToNumber( src );
                retValue = buffer.toByteArray();
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  decode()

        /**
         *  <p>{@summary Decodes a Base&nbsp;32 string.}</p>
         *  <p>This is the converse operation to
         *  {@link Encoder#encodeToString(byte[])}.</p>
         *
         *  @param  src The input data.
         *  @return The decoded value.
         *  @throws ValidationException The input data is not a valid
         *      Base&nbsp;32 sequence
         */
        public final byte [] decode( final String src ) throws ValidationException
        {
            final var buffer = decodeToNumber( requireNonNullArgument( src, "src" ).getBytes( UTF8) );
            final var retValue = buffer.toByteArray();

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  decode()

        /**
         *  Decodes an array of Base&nbsp;32 symbols to a number.
         *
         *  @param  src The input data.
         *  @return The decoded value.
         *  @throws ValidationException The input data is not a valid
         *      Base&nbsp;32 sequence
         */
        public final BigInteger decodeToNumber( final byte [] src ) throws ValidationException
        {
            final var sourceValues = checkValid( src );
            var shift = (sourceValues.length - 1) * 5;
            var retValue = ZERO;
            for( final var value : sourceValues )
            {
                retValue = retValue.or( BigInteger.valueOf( value ).shiftLeft( shift ) );
                shift -= 5;
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  decodeToNumber()

        /**
         *  <p>{@summary Decodes a Base&nbsp; string to a number.}</p>
         *  <p>This is the converse operation to
         *  {@link Encoder#encodeToString(BigInteger)}.</p>
         *
         *  @param  src The input data.
         *  @return The decoded value.
         *  @throws ValidationException The input data is not a valid
         *      Base&nbsp;32 sequence
         */
        public final BigInteger decodeToNumber( final String src ) throws ValidationException
        {
            final var retValue = decodeToNumber( requireNonNullArgument( src, "src" ).getBytes( UTF8 ) );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  decodeToNumber()

        /**
         *  <p>{@summary Decodes an array of Base&nbsp;32 symbols to a
         *  String.}</p>
         *  <p>This is the converse operation to
         *  {@link Encoder#encode(String)}.</p>
         *
         *  @param  src The input data.
         *  @return The decoded value.
         *  @throws ValidationException The input data is not a valid
         *      Base&nbsp;32 sequence
         */
        public final String decodeToString( final byte [] src ) throws ValidationException
        {
            final var buffer = decode( src );
            final var retValue = new String( buffer, UTF8 );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  decodeToString()

        /**
         *  <p>{@summary Decodes a Base&nbsp; string to a String.}</p>
         *  <p>This is the converse operation to
         *  {@link Encoder#encodeToString(String)}.</p>
         *
         *  @param  src The input data.
         *  @return The decoded value.
         *  @throws ValidationException The input data is not a valid
         *      Base&nbsp;32 sequence
         */
        public final String decodeToString( final String src ) throws ValidationException
        {
            var retValue = EMPTY_STRING;
            if( !requireNonNullArgument( src, "src" ).isEmpty() ) retValue = decodeToString( src.getBytes( UTF8 ) );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  decodeToString()
    }
    //   class Decoder

    /**
     *  <p>{@summary The Encoder for
     *  {@href https://www.crockford.com/base32.html Crockfords's Base&nbsp;32}
     *  format.}</p>
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @thanks Douglas Crockford - douglas@crockford.com
     *  @version $Id: Base32.java 1037 2022-12-15 00:35:17Z tquadrat $
     *  @since 0.1.0
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: Base32.java 1037 2022-12-15 00:35:17Z tquadrat $" )
    @API( status = STABLE, since = "0.1.0" )
    public static final class Encoder
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new instance of {@code Base32.Encoder}.
         */
        private Encoder() { /* Just exists */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  <p>{@summary Encodes the given value to Crockford's
         *  Base&nbsp;32.}</p>
         *  <p>This is the converse operation to
         *  {@link Decoder#decode(byte[])}.</p>
         *
         *  @param  value   The value to encode. It must be a positive number.
         *  @return The result.
         */
        public final byte [] encode( final BigInteger value )
        {
            if( requireNonNullArgument( value, "value" ).signum() < 0 )
            {
                throw new ValidationException( format( "%d is negative", value ) );
            }
            final var buffer = value.toString( RADIX )
                .toUpperCase( ROOT );
            final var len = buffer.length();
            final var retValue = new byte[ len ];
            for( var i = 0; i < len; ++i )
            {
                final var pos = Character.digit( buffer.charAt( i ), RADIX );
                retValue [i] = (byte) ALPHABET_UPPERCASE [pos];
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encode()

        /**
         *  <p>{@summary Encodes the given value to Crockford's
         *  Base&nbsp;32.}</p>
         *  <p>This is the converse operation to
         *  {@link Decoder#decodeToString(byte[])}.</p>
         *
         *  @param  value   The value to encode.
         *  @return The result.
         */
        public final byte [] encode( final String value )
        {
            final byte [] retValue;
            if( requireNonNullArgument( value, "value" ).isEmpty() )
            {
                retValue = EMPTY_byte_ARRAY;
            }
            else
            {
                retValue = encode( value.getBytes( UTF8 ) );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encode()

        /**
         *  <p>{@summary Encodes the given value to Crockford's
         *  Base&nbsp;32.}</p>
         *  <p>This is the converse operation to
         *  {@link Decoder#decode(byte[])}.</p>
         *
         *  @param  value   The value to encode.
         *  @return The result.
         */
        public final byte [] encode( final byte [] value )
        {
            final byte [] retValue;
            if( requireNonNullArgument( value, "value" ).length == 0 )
            {
                retValue = EMPTY_byte_ARRAY;
            }
            else
            {
                retValue = encode( new BigInteger( value ) );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encode()

        /**
         *  Encodes the given value to Crockford's Base&nbsp;32.
         *
         *  @param  value   The value to encode.
         *  @return The result.
         */
        public final byte [] encode( final long value )
        {
            final var retValue = encode( BigInteger.valueOf( value ) );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encode()

        /**
         *  <p>{@summary Encodes the given value to a Crockford's Base&nbsp;32
         *  String.}</p>
         *  <p>This is the converse operation to
         *  {@link Decoder#decodeToNumber(String)}.</p>
         *
         *  @param  value   The value to encode.
         *  @return The result.
         */
        public final String encodeToString( final BigInteger value )
        {
            final var retValue = new String( encode( value ), UTF8 );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encodeToString()

        /**
         *  <p>{@summary Encodes the given value to a Crockford's Base&nbsp;32
         *  String.}</p>
         *  <p>This is the converse operation to
         *  {@link Decoder#decodeToString(String)}.</p>
         *
         *  @param  value   The value to encode.
         *  @return The result.
         */
        public final String encodeToString( final String value )
        {
            final var retValue = new String( encode( value ), UTF8 );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encodeToString()

        /**
         *  <p>{@summary Encodes the given value to a Crockford's Base&nbsp;32
         *  String.}</p>
         *  <p>This is the converse operation to
         *  {@link Encoder#encodeToString(byte[])}.</p>
         *
         *  @param  value   The value to encode.
         *  @return The result.
         */
        public final String encodeToString( final byte [] value )
        {
            final var retValue = new String( encode( value ), UTF8 );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encodeToString()

        /**
         *  Encodes the given value to a Crockford's Base&nbsp;32 String.
         *
         *  @param  value   The value to encode.
         *  @return The result.
         */
        public final String encodeToString( final long value )
        {
            final var retValue = new String( encode( value ), UTF8 );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  encodeToString()
    }   //  class Encoder

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The lowercase symbols for the Base&nbsp;32 alphabet.
     */
    private static final char[] ALPHABET_LOWERCASE =  { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' };

    /**
     *  The uppercase symbols for the Base&nbsp;32 alphabet.
     */
    private static final char[] ALPHABET_UPPERCASE =  { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z' };

    /**
     *  An empty byte array.
     */
    public static final byte[] EMPTY_byte_ARRAY = new byte [0];

    /**
     *  The radix for Base&nbsp;32 is, as one would expect: {@value}.
     */
    private static final int RADIX = 32;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The symbol values.
     */
    private static final long[] ALPHABET_VALUES;

    /**
     *  The Base&nbsp;32 decoder instance.
     */
    private static final Decoder m_Decoder;

    /**
     *  The Base&nbsp;32 encoder instance.
     */
    private static final Encoder m_Encoder;

    static
    {
        //---* Initialise the symbol values *----------------------------------
        ALPHABET_VALUES = new long [128];
        fill( ALPHABET_VALUES, -1 ); // -1 indicates an invalid index

        //---* The Numbers *---------------------------------------------------
        ALPHABET_VALUES['0'] = 0x00;
        ALPHABET_VALUES['1'] = 0x01;
        ALPHABET_VALUES['2'] = 0x02;
        ALPHABET_VALUES['3'] = 0x03;
        ALPHABET_VALUES['4'] = 0x04;
        ALPHABET_VALUES['5'] = 0x05;
        ALPHABET_VALUES['6'] = 0x06;
        ALPHABET_VALUES['7'] = 0x07;
        ALPHABET_VALUES['8'] = 0x08;
        ALPHABET_VALUES['9'] = 0x09;

        //---* The lower case letters *----------------------------------------
        ALPHABET_VALUES['a'] = 0x0a;
        ALPHABET_VALUES['b'] = 0x0b;
        ALPHABET_VALUES['c'] = 0x0c;
        ALPHABET_VALUES['d'] = 0x0d;
        ALPHABET_VALUES['e'] = 0x0e;
        ALPHABET_VALUES['f'] = 0x0f;
        ALPHABET_VALUES['g'] = 0x10;
        ALPHABET_VALUES['h'] = 0x11;
        ALPHABET_VALUES['j'] = 0x12;
        ALPHABET_VALUES['k'] = 0x13;
        ALPHABET_VALUES['m'] = 0x14;
        ALPHABET_VALUES['n'] = 0x15;
        ALPHABET_VALUES['p'] = 0x16;
        ALPHABET_VALUES['q'] = 0x17;
        ALPHABET_VALUES['r'] = 0x18;
        ALPHABET_VALUES['s'] = 0x19;
        ALPHABET_VALUES['t'] = 0x1a;
        ALPHABET_VALUES['v'] = 0x1b;
        ALPHABET_VALUES['w'] = 0x1c;
        ALPHABET_VALUES['x'] = 0x1d;
        ALPHABET_VALUES['y'] = 0x1e;
        ALPHABET_VALUES['z'] = 0x1f;

        //---* The lower case OIL *--------------------------------------------
        ALPHABET_VALUES['o'] = 0x00;
        ALPHABET_VALUES['i'] = 0x01;
        ALPHABET_VALUES['l'] = 0x01;

        //---* The upper case letters *----------------------------------------
        ALPHABET_VALUES['A'] = 0x0a;
        ALPHABET_VALUES['B'] = 0x0b;
        ALPHABET_VALUES['C'] = 0x0c;
        ALPHABET_VALUES['D'] = 0x0d;
        ALPHABET_VALUES['E'] = 0x0e;
        ALPHABET_VALUES['F'] = 0x0f;
        ALPHABET_VALUES['G'] = 0x10;
        ALPHABET_VALUES['H'] = 0x11;
        ALPHABET_VALUES['J'] = 0x12;
        ALPHABET_VALUES['K'] = 0x13;
        ALPHABET_VALUES['M'] = 0x14;
        ALPHABET_VALUES['N'] = 0x15;
        ALPHABET_VALUES['P'] = 0x16;
        ALPHABET_VALUES['Q'] = 0x17;
        ALPHABET_VALUES['R'] = 0x18;
        ALPHABET_VALUES['S'] = 0x19;
        ALPHABET_VALUES['T'] = 0x1a;
        ALPHABET_VALUES['V'] = 0x1b;
        ALPHABET_VALUES['W'] = 0x1c;
        ALPHABET_VALUES['X'] = 0x1d;
        ALPHABET_VALUES['Y'] = 0x1e;
        ALPHABET_VALUES['Z'] = 0x1f;

        //---* The upper case OIL *--------------------------------------------
        ALPHABET_VALUES['O'] = 0x00;
        ALPHABET_VALUES['I'] = 0x01;
        ALPHABET_VALUES['L'] = 0x01;

        //---* The instances *-------------------------------------------------
        m_Decoder = new Decoder();
        m_Encoder = new Encoder();
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private Base32() { throw new PrivateConstructorForStaticClassCalledError( Base32.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns a decoder for
     *  {@href https://www.crockford.com/base32.html Crockfords's Base&nbsp;32}
     *  format.
     *
     *  @return The decoder.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final Decoder getDecoder() { return m_Decoder; }

    /**
     *  Returns an encoder for
     *  {@href https://www.crockford.com/base32.html Crockfords's Base&nbsp;32}
     *  format.
     *
     *  @return The encoder.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final Encoder getEncoder() { return m_Encoder; }
}
//  class Base32

/*
 *  End of File
 */