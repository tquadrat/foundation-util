/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
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

import static java.lang.Character.MIN_CODE_POINT;
import static java.lang.Character.isISOControl;
import static java.lang.Character.isSurrogatePair;
import static java.lang.Character.toChars;
import static java.lang.Character.toCodePoint;
import static java.lang.Integer.min;
import static java.text.Normalizer.isNormalized;
import static java.text.Normalizer.normalize;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.breakString;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isEmpty;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.ValidationException;

/**
 *  This class provides several utilities dealing with Strings in different
 *  character sets/encodings.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version CharSetUtils: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
 *
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@SuppressWarnings( "MagicNumber" )
@ClassVersion( sourceVersion = "$Id: CharSetUtils.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
@UtilityClass
public final class CharSetUtils
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private CharSetUtils() { throw new PrivateConstructorForStaticClassCalledError( CharSetUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Converts the given byte array into to a String that will only contain
     *  printable ASCII characters; all other characters will be 'escaped' to
     *  the format &quot;<code>&#92;uXXXX</code>&quot;. This can be useful to
     *  generate a String in another character set/encoding than ASCII or
     *  UTF-8/Unicode, given that the receiving part can interpret the
     *  format.<br>
     *  <br>But generally, a transfer encoding like BASE64 or quoted-printable
     *  should be preferred.
     *
     *  @param  bytes   The input; may be {@code null}.
     *  @return The output string; {@code null} if the input was already
     *      {@code null}.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String convertBytesToASCII( final byte [] bytes )
    {
        String retValue = null;
        if( nonNull( bytes ) )
        {
            if( bytes.length == 0 )
            {
                retValue = EMPTY_STRING;
            }
            else
            {
                final var buffer = new StringBuilder();
                for( final var b : bytes )
                {
                    @SuppressWarnings( "cast" )
                    final var codePoint = b;
                    //noinspection ImplicitNumericConversion
                    buffer.append( (codePoint < ' ') || (codePoint >= 0x007F)
                        ? escapeCharacter( codePoint )
                        : Character.toString( codePoint ) );
                }
                retValue = buffer.toString();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertBytesToASCII()

    /**
     *  Converts a String that contains only ASCII characters and Unicode
     *  escape sequences like &quot;<code>&#92;uXXXX</code>&quot; to the
     *  equivalent Unicode String.<br>
     *  <br>This method will not touch other escape sequences, like
     *  <code>&quot;&#92;n&quot;</code> or <code>&quot;&#92;t&quot;</code>.
     *  Refer to
     *  {@link String#translateEscapes()}.
     *
     *  @param  input   The input String; may be {@code null}.
     *  @return The output string; {@code null} if the input string was
     *      already {@code null}.
     *  @throws IllegalArgumentException    The given input String contained at
     *      least one non-ASCII character.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String convertEscapedStringToUnicode( final CharSequence input ) throws IllegalArgumentException
    {
        String retValue = null;
        if( nonNull( input ) )
        {
            if( isEmpty( input ) )
            {
                retValue = EMPTY_STRING;
            }
            else
            {
                final var pattern = compile( "\\\\u\\p{XDigit}{4}" );
                var inputPos = 0;
                final var inputLength = input.length();
                final var buffer = new StringBuilder( inputLength );
                ScanLoop: while( inputPos < inputLength )
                {
                    final var currentChar = input.charAt( inputPos );
                    if( currentChar == '\\' )
                    {
                        //---* Is this an escape sequence? *-------------------
                        inputPos += extractEscapeSequence( buffer, pattern, input.subSequence( inputPos, min( inputLength, inputPos + 12 ) ) );
                        continue ScanLoop;
                    }

                    buffer.append( currentChar );
                    ++inputPos;
                }   //  ScanLoop:
                retValue = buffer.toString();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertEscapedStringToUnicode()

    /**
     *  Applies the given normalisation to the given Unicode String and
     *  translates it to a String that will only contain printable ASCII
     *  characters; all other characters will be 'escaped' to the format
     *  &quot;<code>&#92;uXXXX</code>&quot;.
     *
     *  @param  normalization   The normalisation form; in case it is
     *      {@code null}, no normalisation will be performed.
     *  @param  input   The input String; may be {@code null}.
     *  @return The output String; {@code null} if the input String was
     *      already {@code null}.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String convertUnicodeToASCII( final Normalizer.Form normalization, final CharSequence input )
    {
        String retValue = null;
        if( nonNull( input ) )
        {
            if( isEmpty( input ) )
            {
                retValue = EMPTY_STRING;
            }
            else
            {
                //---* Normalise the String *----------------------------------
                @SuppressWarnings( "LocalVariableNamingConvention" )
                final var s = isNull( normalization )
                    ? input
                    : isNormalized( input, normalization )
                        ? input
                        : normalize( input, normalization );

                retValue = s.codePoints()
                    .mapToObj( codePoint ->
                        isPrintableASCIICharacter( codePoint )
                        ? Character.toString( codePoint )
                        : escapeCharacter( codePoint ) )
                    .collect( joining() );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertUnicodeToASCII()

    /**
     *  Translates the given Unicode String without any normalisation to a
     *  String that will only contain printable ASCII characters; all other
     *  characters will be 'escaped' to the format
     *  &quot;<code>&#92;uXXXX</code>&quot;. Calling this method is the same as
     *  calling
     *  {@link #convertUnicodeToASCII(Normalizer.Form, CharSequence)}
     *  with {@code null} as the first argument.
     *
     *  @param  input   The input String; may be {@code null}.
     *  @return The output String; {@code null} if the input String was
     *      already {@code null}.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String convertUnicodeToASCII( final CharSequence input )
    {
        final var retValue = convertUnicodeToASCII( null, input );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertUnicodeToASCII()

    /**
     *  Returns the Unicode escape sequence for the given character. This will
     *  return &quot;{@code &#92;u0075}&quot; for the letter 'u', and
     *  &quot;{@code &#92;u003c}&quot; for the smaller-than sign '&lt;'.<br>
     *  <br>This method should be used only for characters that are not
     *  surrogates; for general use, the implementation that takes a code point
     *  is preferred.
     *
     *  @param  c   The character.
     *  @return The escape sequence.
     *
     *  @see #escapeCharacter(int)
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String escapeCharacter( final char c )
    {
        final var retValue = format( "\\u%04x", Integer.valueOf( c ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escapeCharacter()

    /**
     *  Returns the Unicode escape sequence for the given code point. This will
     *  return &quot;{@code &#92;u0075}&quot; for the letter 'u', and
     *  &quot;{@code &#92;u003c}&quot; for the smaller-than sign '&lt;'.<br>
     *  <br>This method takes only a single code point; to translate a whole
     *  String, this code sequence can be used:<pre><code>  &hellip;
     *  String result = input.codePoints()
     *      .mapToObj( codePoint -&gt; escapeUnicode( codePoint ) )
     *      .collect( Collectors.joining() );
     *  &hellip;</code></pre>
     *  This will escape <i>all</i> characters in the String. If only a subset
     *  needs to be escaped, the mapping function in
     *  {@link java.util.stream.IntStream#mapToObj(java.util.function.IntFunction) mapToObj()}
     *  can be adjusted accordingly. Something like that is implemented with
     *  the method
     *  {@link #convertUnicodeToASCII(CharSequence)}.
     *
     *  @param  codePoint   The character.
     *  @return The escape sequence.
     *  @throws IllegalArgumentException    The given code point is invalid.
     *
     *  @see String#codePoints()
     *  @see java.util.stream.IntStream#mapToObj(java.util.function.IntFunction)
     *  @see java.util.stream.Stream#collect(java.util.stream.Collector)
     *  @see java.util.stream.Collectors#joining()
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String escapeCharacter( final int codePoint ) throws IllegalArgumentException
    {
        final var retValue = new StringBuilder();
        for( final var c : toChars( codePoint ) ) retValue.append( format( "\\u%04x", Integer.valueOf( c ) ) );

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  escapeCharacter()

    /**
     *  Returns {@code true} if the given character is an ASCII character.
     *
     *  @param  c   The character to check.
     *  @return {@code true} if the given character is an ASCII character,
     *      {@code false} otherwise.
     */
    public static final boolean isASCIICharacter( final char c )
    {
        return isASCIICharacter( (int) c );
    }   //  isASCIICharacter()

    /**
     *  Returns {@code true} if the given code point represents an ASCII
     *  character.
     *
     *  @param  codePoint   The code point to check.
     *  @return {@code true} if the given code point represents an ASCII
     *      character, {@code false} otherwise.
     */
    public static final boolean isASCIICharacter( final int codePoint )
    {
        final var retValue = (codePoint >= MIN_CODE_POINT) && (codePoint < 0x80);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isASCIICharacter()

    /**
     *  Returns {@code true} if the given character is a printable ASCII
     *  character. That means, it is an ASCII character, but not a control
     *  character.
     *
     *  @param  c   The character to check.
     *  @return {@code true} if the given character is a printable ASCII
     *      character, {@code false} otherwise.
     */
    public static final boolean isPrintableASCIICharacter( final char c )
    {
        return isPrintableASCIICharacter( (int) c );
    }   //  isPrintableASCIICharacter()

    /**
     *  Returns {@code true} if the given code point represents a printable
     *  ASCII character. That means, it is an ASCII character, but not a
     *  control character.
     *
     *  @param  codePoint   The code point to check.
     *  @return {@code true} if the given code point represents a printable
     *      ASCII character, {@code false} otherwise.
     */
    public static final boolean isPrintableASCIICharacter( final int codePoint )
    {
        final var retValue = !isISOControl(codePoint) && (codePoint >= MIN_CODE_POINT) && (codePoint < 0x80);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isPrintableASCIICharacter()

    /**
     *  Extracts the escape sequence from the given chunk, write the result to
     *  the buffer and returns the offset.
     *
     *  @param  buffer  The target buffer.
     *  @param  pattern The regex pattern for the check.
     *  @param chunk    The chunk to check.
     *  @return The offset; one of 1, 6, or 12.
     */
    private static final int extractEscapeSequence( final StringBuilder buffer, final Pattern pattern, final CharSequence chunk )
    {
        var retValue = 1;
        if( chunk.length() >= 6 )
        {
            final var c1 = chunk.subSequence( 0, 6 );
            if( pattern.matcher( c1 ).matches() )
            {
                if( (chunk.length() == 12) && pattern.matcher( chunk.subSequence( 6, 12 ) ).matches() )
                {
                    try
                    {
                        buffer.append( unescapeUnicode( chunk ) );
                        retValue = 12;
                    }
                    catch( final ValidationException ignored ) { /* Deliberately ignored */ }
                }

                if( retValue == 1 )
                {
                    try
                    {
                        buffer.append( unescapeUnicode( c1 ) );
                        retValue = 6;
                    }
                    catch( final ValidationException ignored ) { /* Deliberately ignored */ }
                }
            }
        }

        if( retValue == 1 ) buffer.append( chunk.charAt( 0 ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  extractEscapeSequence()

    /**
     *  Parses Strings in the format &quot;<code>&#92;uXXXX</code>&quot;,
     *  containing the textual representation of a single Unicode character, to
     *  the respective Unicode character. Some Unicode characters will be
     *  represented as <i>surrogate pairs</i> in Java, so the String that is
     *  returned by this method may contain more than one {@code char}.<br>
     *  <br>The input format for this method is used in Java source code
     *  Strings, in Java {@code .properties} files, in C/C++ source code, in
     *  JavaScript source, &hellip;
     *
     *  @param  s   The input String with the Unicode escape sequence.
     *  @return The Unicode character.
     *  @throws ValidationException The input is {@code null}, empty, or cannot
     *      be parsed as a unicode escape sequence.
     *
     *  @since 0.1.5
     */
    @API( status = STABLE, since = "0.1.5" )
    public static final String unescapeUnicode( final CharSequence s )
    {
        final var len = requireNotEmptyArgument( s, "s" ).length();
        //noinspection MagicNumber
        if( (len != 6) && (len != 12) ) throw new ValidationException( "The length of a Unicode String must be 6 or 12 characters" );
        if( !s.subSequence( 0, 2 ).equals( "\\u" ) ) throw new ValidationException( "Unicode String must start with '\\u'" );

        final var msgCannotparse = "Cannot parse '%s' as a Unicode Escape String";
        @SuppressWarnings( "NumericCastThatLosesPrecision" )
        final var characters = breakString( s, 6 )
            .mapToInt( chunk ->
            {
                try
                {
                    return Integer.parseInt( chunk.subSequence( 2, 6 ).toString(), 0x10 );
                }
                catch( final NumberFormatException e )
                {
                    throw new ValidationException( format( msgCannotparse, s ), e );
                }
            } )
            .mapToObj( i -> Character.valueOf( (char) i ) )
            .toArray( Character []::new );

        final var codePoint = switch( characters.length )
            {
                case 1 -> characters [0];
                case 2 ->
                    {
                        if( !isSurrogatePair( characters [0], characters [1] ) )
                        {
                            throw new ValidationException( format( msgCannotparse, s ) );
                        }
                        yield toCodePoint( characters [0], characters [1] );
                    }
                default -> throw new ValidationException( format( msgCannotparse, s ) );
            };
        if( !Character.isValidCodePoint( codePoint ) ) throw new ValidationException( format( msgCannotparse, s ) );
        final var retValue = new String( toChars( codePoint ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  unescapeUnicode()
}
//  class CharSetUtils

/*
 *  End of File
 */