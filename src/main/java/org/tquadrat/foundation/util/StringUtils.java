/*
 * ============================================================================
 * Copyright © 20002-2021 by Thomas Thrien.
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

import static java.lang.Character.isISOControl;
import static java.lang.Character.isValidCodePoint;
import static java.lang.Character.isWhitespace;
import static java.lang.Character.toChars;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;
import static java.lang.Integer.min;
import static java.lang.String.join;
import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.text.Normalizer.Form.NFKC;
import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.DEPRECATED;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.CHAR_ELLIPSIS;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.NULL_CHAR;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.CharSetUtils.escapeCharacter;
import static org.tquadrat.foundation.util.StringUtils.Clipping.CLIPPING_ABBREVIATE;
import static org.tquadrat.foundation.util.StringUtils.Clipping.CLIPPING_ABBREVIATE_MIDDLE;
import static org.tquadrat.foundation.util.StringUtils.Clipping.CLIPPING_CUT;
import static org.tquadrat.foundation.util.StringUtils.Clipping.CLIPPING_NONE;
import static org.tquadrat.foundation.util.StringUtils.Padding.PADDING_CENTER;
import static org.tquadrat.foundation.util.StringUtils.Padding.PADDING_LEFT;
import static org.tquadrat.foundation.util.StringUtils.Padding.PADDING_RIGHT;
import static org.tquadrat.foundation.util.internal.Entities.HTML50;
import static org.tquadrat.foundation.util.internal.Entities.XML;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.CharSequenceTooLongException;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.ImpossibleExceptionError;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.lang.internal.SharedFormatter;

/**
 *  Library of utility methods that are useful when dealing with Strings. <br>
 *  <br>Parts of the code were adopted from the class
 *  <code>org.apache.commons.lang.StringUtils</code> and modified to match the
 *  requirements of this project. In particular, these are the methods
 *  <ul>
 *  <li>{@link #abbreviate(CharSequence, int) abbreviate()}</li>
 *  <li>{@link #capitalize(CharSequence) capitalize()}</li>
 *  <li>{@link #escapeHTML(CharSequence) escapeHTML()} in both versions</li>
 *  <li>{@link #isEmpty(CharSequence) isEmpty()}</li>
 *  <li>{@link #isNotEmpty(CharSequence) isNotEmpty()}</li>
 *  <li>{@link #repeat(CharSequence, int) repeat()}</li>
 *  <li>{@link #uncapitalize(CharSequence) uncapitalize()}</li>
 *  <li>{@link #unescapeHTML(CharSequence) unescapeHTML()} in both versions</li>
 *  </ul>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: StringUtils.java 1001 2022-01-29 16:42:15Z tquadrat $
 *  @since 0.0.3
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "OverlyComplexClass"} )
@ClassVersion( sourceVersion = "$Id: StringUtils.java 1001 2022-01-29 16:42:15Z tquadrat $" )
@UtilityClass
public final class StringUtils
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The clipping mode that is used for the method
     *  {@link StringUtils#pad(CharSequence,int,char,Padding,Clipping)}
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: StringUtils.java 1001 2022-01-29 16:42:15Z tquadrat $
     *  @since 0.0.3
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "InnerClassTooDeeplyNested" )
    @ClassVersion( sourceVersion = "$Id: StringUtils.java 1001 2022-01-29 16:42:15Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static enum Clipping
    {
            /*------------------*\
        ====** Enum Declaration **=============================================
            \*------------------*/
        /**
         *  If an input String is already longer than the target length, it
         *  will be returned unchanged.
         */
        CLIPPING_NONE
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            protected final String clip( final CharSequence s, final int length ) { return s.toString(); }
        },

        /**
         *  If an input String is longer than the target length, it will be
         *  just shortened to that length.
         */
        CLIPPING_CUT
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            protected final String clip( final CharSequence s, final int length )
            {
                final var retValue = (s.length() > length ? s.subSequence( 0, length ) : s).toString();

                //---* Done *--------------------------------------------------
                return retValue;
            }   //  clip()
        },

        /**
         *  If an input String is longer than the target length, it will be
         *  abbreviated to that length, by calling
         *  {@link StringUtils#abbreviate(CharSequence, int)}
         *  with that String. The minimum length for the padded String is 4.
         */
        CLIPPING_ABBREVIATE
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            protected final String clip( final CharSequence s, final int length ) { return abbreviate( s, length ); }
        },

        /**
         *  If an input String is longer than the target length, it will be
         *  abbreviated to that length, by calling
         *  {@link StringUtils#abbreviateMiddle(CharSequence, int)}
         *  with that String. The minimum length for the padded String is 5.
         */
        CLIPPING_ABBREVIATE_MIDDLE
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            protected final String clip( final CharSequence s, final int length ) { return abbreviateMiddle( s, length ); }
        };

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Clips the given input String.
         *
         *  @param  s   The input String.
         *  @param  length  The target length.
         *  @return The result String.
         */
        protected abstract String clip( final CharSequence s, final int length );
    }
    //  enum Clipping

    /**
     *  The padding mode that is used for the methods
     *  {@link StringUtils#pad(CharSequence,int,char,Padding,boolean)}
     *  and
     *  {@link StringUtils#pad(CharSequence,int,char,Padding,Clipping)}
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: StringUtils.java 1001 2022-01-29 16:42:15Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "InnerClassTooDeeplyNested" )
    @ClassVersion( sourceVersion = "$Id: StringUtils.java 1001 2022-01-29 16:42:15Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static enum Padding
    {
            /*------------------*\
        ====** Enum Declaration **=============================================
            \*------------------*/
        /**
         *  The pad characters are distributed evenly at begin and end of the
         *  string.
         */
        PADDING_CENTER
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            protected final String pad( final CharSequence s, final int padSize, final char c )
            {
                final var rightSize = padSize / 2;
                final var leftSize = padSize - rightSize;
                final var retValue = padding( leftSize, c ) + s.toString() + padding( rightSize, c );

                //---* Done *--------------------------------------------------
                return retValue;
            }   //  pad()
        },

        /**
         *  The pad characters are added at the beginning of the string
         *  (prefixing it).
         */
        PADDING_LEFT
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            protected final String pad( final CharSequence s, final int padSize, final char c )
            {
                return padding( padSize, c ) + s.toString();
            }   //  pad()
        },

        /**
         *  The pad characters are added the end of the string (as a suffix).
         */
        PADDING_RIGHT
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            protected final String pad( final CharSequence s, final int padSize, final char c )
            {
                return s.toString() + padding( padSize, c );
            }   //  pad()
        };

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Pads the given input String.
         *
         *  @param  s   The input String.
         *  @param  padSize The pad size.
         *  @param  c   The pad character.
         *  @return The result String.
         */
        protected abstract String pad( final CharSequence s, final int padSize, final char c );

        /**
         *  Returns padding using the specified pad character repeated to the
         *  given length.<br>
         *  <br><code>
         *  Padding.padding(&nbsp;0,&nbsp;'e'&nbsp;)&nbsp;&rArr;&nbsp;""<br>
         *  Padding.padding(&nbsp;3,&nbsp;'e'&nbsp;)&nbsp;&rArr;&nbsp;"eee"<br>
         *  Padding.padding(&nbsp;-2,&nbsp;'e'&nbsp;)&nbsp;&rArr;&nbsp;IndexOutOfBoundsException<br>
         *  </code>
         *
         *  @param  repeat  Number of times to repeat {@code padChar}; must be
         *      0 or greater.
         *  @param  padChar Character to repeat.
         *  @return String with repeated {@code padChar} character, or the
         *      empty String if {@code repeat} is 0.
         *  @throws IndexOutOfBoundsException {@code repeat} is less than 0.
         *
         *  @see StringUtils#repeat(int,int)
         */
        private static String padding( final int repeat, final char padChar ) throws IndexOutOfBoundsException
        {
            if( repeat < 0 ) throw new IndexOutOfBoundsException( format( MSG_PadNegative, repeat ) );

            final var retValue = Character.toString( padChar ).repeat( repeat ).intern();

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  padding()
    }
    //  enum Padding

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  <p>{@summary The regular expression for an HTML or XML comment:
     *  {@value}.}</p>
     *  <p>This pattern is used by the
     *  {@link #stripXMLComments(CharSequence)}
     *  method.</p>
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String COMMENTREMOVAL_PATTERN = "<!--.+?-->";

    /**
     *  The initial buffer size that is used by
     *  {@link #format(String, Object...)}
     *  and
     *  {@link #format(Locale, String, Object...)}
     *  (per thread): {@value}.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int FORMAT_INITIAL_BUFFERSIZE = 2048;

    /**
     *  The message text indicating that the given value for the abbreviation
     *  target length is too short.
     */
    private static final String MSG_AbbrTooShort = "The minimum abbreviation width is %d";

    /**
     *  The message indicating that the give size for padding is negative.
     */
    private static final String MSG_PadNegative = "Cannot pad a negative amount: %d";

    /**
     *  The maximum size to which the padding constant(s) can expand: {@value}.
     *
     *  @see #repeat(CharSequence,int) repeat() for String
     *  @see #repeat(char,int) repeat() for char
     */
    @SuppressWarnings( "unused" )
    private static final int PAD_LIMIT = 8192;

    /**
     *  The regular expression for an HTML or XML tag: {@value}.<br>
     *  <br>This pattern is used by the
     *  {@link #stripTags(CharSequence)}
     *  method.<br>
     *  <br>As HTML/XML comments may contain a &quot;greater than&quot; sign
     *  ('&gt;' or '&amp;gt;'), it is necessary to treat comments
     *  separately.<br>
     *  <br>Just as a reminder: several sources recommend using the following
     *  idiom for embedded JavaScript:<pre><code>  &lt;script&gt;
     *  &lt;!--
     *  <i>JavaScript code </i>
     *  --&gt;
     *  &lt;/script&gt;</code></pre>
     *
     *  @since 0.0.5
     */
    @SuppressWarnings( "RegExpUnnecessaryNonCapturingGroup" )
    @API( status = STABLE, since = "0.0.5" )
    public static final String TAGREMOVAL_PATTERN = "(?:<!--.+?-->)|(?:<[^>]+?>)";

    /**
     *  The regular expression to identify a variable in a char sequence:
     *  {@value}.
     *
     *  @see #findVariables(CharSequence)
     *  @see #isValidVariableName(CharSequence)
     *  @see #replaceVariable(CharSequence,Map...)
     *  @see #replaceVariable(CharSequence, Function)
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link Template#VARIABLE_PATTERN}
     *      instead.
     */
    @API( status = DEPRECATED, since = "0.0.5" )
    @Deprecated( since ="0.1.0", forRemoval = true )
    public static final String VARIABLE_PATTERN = Template.VARIABLE_PATTERN;

    /**
     *  The template for variables: {@value}. The argument is the name of the
     *  variable itself; after an optional prefix character, it may not contain
     *  other characters than the letters from 'a' to 'z' (upper case and lower
     *  case), the digits from '0' to '9' and the special characters underscore
     *  ('_') and dot ('.').<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link Template#VARIABLE_TEMPLATE}
     *      instead.
     */
    @API( status = DEPRECATED, since = "0.0.5" )
    @Deprecated( since ="0.1.0", forRemoval = true )
    public static final String VARIABLE_TEMPLATE = Template.VARIABLE_TEMPLATE;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The pattern that is used to identify an HTML or XML comment.
     *
     *  @see #stripXMLComments(CharSequence)
     *  @see #COMMENTREMOVAL_PATTERN
     */
    private static final Pattern m_CommentRemovalPattern;

    /**
     *  The pattern that is used to identify an HTML or XML tag.
     *
     *  @see #stripTags(CharSequence)
     *  @see #TAGREMOVAL_PATTERN
     */
    private static final Pattern m_TagRemovalPattern;

    static
    {
        //---* The regex patterns *--------------------------------------------
        try
        {
            m_CommentRemovalPattern = compile( COMMENTREMOVAL_PATTERN, DOTALL );
            m_TagRemovalPattern = compile( TAGREMOVAL_PATTERN, DOTALL );
        }
        catch( final PatternSyntaxException e )
        {
            throw new ImpossibleExceptionError( "The patterns are constant values that have been tested", e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance of this class is allowed.
     */
    private StringUtils() { throw new PrivateConstructorForStaticClassCalledError( StringUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Abbreviates a String using ellipses (Unicode HORIZONTAL ELLIPSIS,
     *  0x2026). This will turn &quot;<i>Now is the time for all good
     *  men</i>&quot; into &quot;<i>Now is the time for&hellip;</i>&quot;.<br>
     *  <br>Specifically:
     *  <ul>
     *  <li>If {@code text} is less than {@code maxWidth} characters long,
     *  return it unchanged.</li>
     *  <li>Else abbreviate it to <code>(substring( text, 0, max - 1 ) +
     *  &quot;\u2026&quot; )</code>.</li>
     *  <li>If {@code maxWidth} is less than 4, throw an
     *  {@link ValidationException}.</li>
     *  <li>In no case it will return a String of length greater than
     *  {@code maxWidth}.</li>
     *  </ul>
     *  Some samples:<br>
     *  <pre><code>
     *  StringUtils.abbreviate( null, * )      = null
     *  StringUtils.abbreviate( "", 4 )        = ""
     *  StringUtils.abbreviate( "abc", 4 ) = "abc"
     *  StringUtils.abbreviate( "abcd", 4 ) = "abcd;"
     *  StringUtils.abbreviate( "abcdefg", 4 ) = "abc&hellip;"
     *  StringUtils.abbreviate( "abcdefg", 7 ) = "abcdefg"
     *  StringUtils.abbreviate( "abcdefg", 8 ) = "abcdefg"
     *  StringUtils.abbreviate( "abcdefg", 3 ) = IllegalArgumentException
     *  </code></pre>
     *
     *  @param  text    The String to abbreviate, can be {@code null}.
     *  @param  maxWidth    The maximum length of result String, must be at
     *      least 4.
     *  @return The abbreviated String, or {@code null} if the input was
     *      already {@code null}.
     *  @throws ValidationException The value for {@code maxWidth} was less
     *      than 4.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String abbreviate( final CharSequence text, final int maxWidth ) throws ValidationException
    {
        return abbreviate( text, 0, maxWidth );
    }   //  abbreviate()

    /**
     *  Abbreviates a String using ellipses (Unicode HORIZONTAL ELLIPSIS,
     *  0x2026). This will turn &quot;<i>Now is the time for all good
     *  men</i>&quot; into &quot;<i>&hellip;is the time
     *  for&hellip;</i>&quot;.<br>
     *  <br>Works like
     *  {@link #abbreviate(CharSequence, int)},
     *  but allows to specify a &quot;left edge&quot; offset. Note that this
     *  left edge is not necessarily going to be the leftmost character in the
     *  result, or the first character following the ellipses, but it will
     *  appear somewhere in the result. An offset less than 0 will be treated
     *  as 0, a value greater than {@code maxWidth} will be ignored.<br>
     *  <br>In no case will it return a String of length greater than
     *  {@code maxWidth}.<br>
     *  <br>Some samples:<br>
     *  <pre>
     *  StringUtils.abbreviate( null, *, * )                = null
     *  StringUtils.abbreviate( "", 0, 4 )                  = ""
     *  StringUtils.abbreviate( "abcdefghijklmno", -1, 10 ) = "abcdefghi&hellip;"
     *  StringUtils.abbreviate( "abcdefghijklmno", 0, 10 )  = "abcdefghi&hellip;"
     *  StringUtils.abbreviate( "abcdefghijklmno", 1, 10 )  = "abcdefghi&hellip;"
     *  StringUtils.abbreviate( "abcdefghijklmno", 4, 10 )  = "&hellip;efghijkl&hellip;"
     *  StringUtils.abbreviate( "abcdefghijklmno", 5, 10 )  = "&hellip;fghijklm&hellip;"
     *  StringUtils.abbreviate( "abcdefghijklmno", 6, 10 )  = "&hellip;ghijklmno"
     *  StringUtils.abbreviate( "abcdefghijklmno", 8, 10 )  = "&hellip;ghijklmno"
     *  StringUtils.abbreviate( "abcdefghijklmno", 10, 10 ) = "&hellip;ghijklmno"
     *  StringUtils.abbreviate( "abcdefghijklmno", 12, 10 ) = "&hellip;ghijklmno"
     *  StringUtils.abbreviate( "abcdefghij", 0, 3 )        = IllegalArgumentException
     *  StringUtils.abbreviate( "abcdefghij", 5, 6 )        = IllegalArgumentException
     *  </pre>
     *
     *  @param  text    The String to process, can be {@code null}.
     *  @param  offset  The left edge of the source String; this value will not
     *      be checked.
     *  @param  maxWidth    The maximum length of result String, must be at
     *      least 4.
     *  @return The abbreviated String, or {@code null} if the input was
     *      already {@code null}.
     *  @throws ValidationException The value for {@code maxWidth} was less
     *      than 4.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String abbreviate( final CharSequence text, final int offset, final int maxWidth ) throws ValidationException
    {
        final var ellipsis = Character.toString( CHAR_ELLIPSIS ).intern();

        String retValue = null;
        if( nonNull( text ) )
        {
            if( maxWidth < 4 ) throw new ValidationException( format( MSG_AbbrTooShort, 4 ) );

            final var len = text.length();
            if( len > maxWidth )
            {
                var effectiveOffset = min( offset, len);
                if( (len - effectiveOffset) < (maxWidth - 1))
                {
                    effectiveOffset = len - (maxWidth - 1);
                }
                if( effectiveOffset <= 1 )
                {
                    retValue = text.subSequence( 0, maxWidth - 1 ) + ellipsis;
                }
                else
                {
                    if( ((effectiveOffset + maxWidth) - 1) < len )
                    {
                        retValue = ellipsis + abbreviate( text.subSequence( effectiveOffset, len ), maxWidth - 1 );
                    }
                    else
                    {
                        retValue = ellipsis + text.subSequence( len - (maxWidth - 1), len );
                    }
                }
            }
            else
            {
                retValue = text.toString();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  abbreviate()

    /**
     *  Abbreviates a String using ellipses (Unicode HORIZONTAL ELLIPSIS,
     *  0x2026) in the middle of the returned text. This will turn &quot;<i>Now
     *  is the time for all good men</i>&quot; into &quot;<i>Now is &hellip;
     *  good men</i>&quot;<br>
     *  <br>Works like
     *  {@link #abbreviate(CharSequence, int)}.<br>
     *  <br>In no case will it return a String of length greater than
     *  {@code maxWidth}.<br>
     *  <br>Some samples:<br>
     *  <pre>
     *  StringUtils.abbreviateMiddle(null, *)      = null
     *  StringUtils.abbreviateMiddle("", 5)        = ""
     *  StringUtils.abbreviateMiddle("abcdefgh", 5) = "ab&hellip;gh"
     *  StringUtils.abbreviateMiddle("abcdefgh", 7) = "ab&hellip;gh"
     *  StringUtils.abbreviateMiddle("abcdefgh", 8) = "abcdefgh"
     *  StringUtils.abbreviateMiddle("abcdefgh", 4) = IllegalArgumentException
     *  </pre>
     *
     *  @param  str The String to check, may be {@code null}.
     *  @param  maxWidth    The maximum length of result String, must be at
     *      least 5.
     *  @return The abbreviated String, or {@code null} if the input was
     *      already {@code null}.
     *  @throws ValidationException The value for {@code maxWidth} was less
     *      than 5.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String abbreviateMiddle( final CharSequence str, final int maxWidth )
    {
        final var ellipsis = Character.toString( CHAR_ELLIPSIS ).intern();

        String retValue = null;
        if( nonNull( str ) )
        {
            if( maxWidth < 5 ) throw new ValidationException( format( MSG_AbbrTooShort, 5 ) );

            final var len = str.length();
            if( len > maxWidth )
            {
                final var suffixLength = (maxWidth - 1) / 2;
                final var prefixLength = maxWidth - 1 - suffixLength;
                final var suffixStart = len - suffixLength;
                retValue = str.subSequence( 0, prefixLength ) + ellipsis + str.subSequence( suffixStart, suffixStart + suffixLength );
            }
            else
            {
                retValue = str.toString();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  abbreviateMiddle()

    /**
     *  <p>{@summary Breaks a long string into chunks of the given length.}</p>
     *  <p>This method returns an instance of
     *  {@link Stream} that can be easily converted into an array or a
     *  collection.</p>
     *  <p>To array:</p>
     *  <pre><code>breakString( &lt;<i>string</i>&gt;, &lt;<i>chunk</i>&gt; ).toArray( String []::new )</code></pre>
     *  <p>To collection (here: a
     *  {@link List}):</p>
     *  <pre><code>breakString( &lt;<i>string</i>&gt;, &lt;<i>chunk</i>&gt; ).collect( Collectors.toList() )</code></pre>
     *
     *  @param  s   The string.
     *  @param  chunk   The chunk size.
     *  @return The chunks from the string; the last chunk could be shorter
     *      than the others.
     *
     *  @see Stream#toArray(java.util.function.IntFunction)
     *  @see Stream#collect(java.util.stream.Collector)
     *  @see java.util.stream.Collectors#toList()
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Stream<String> breakString( final CharSequence s, final int chunk )
    {
        if( chunk < 1 ) throw new ValidationException( format( "Chunk size must not be zero or a negative number: %d", chunk ) );

        final Builder<String> builder = Stream.builder();
        final var len = requireNonNullArgument( s, "s" ).length();
        var pos = 0;
        while( (pos + chunk) < len )
        {
            builder.add( s.subSequence( pos, pos + chunk ).toString() );
            pos += chunk;
        }
        if( pos < len ) builder.add( s.subSequence( pos, len ).toString() );

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  breakString()

    /**
     *  <p>{@summary Breaks a text into lines of the given length, but
     *  different from
     *  {@link #breakString(CharSequence, int)},
     *  it will honour whitespace.}</p>
     *  <p>This method returns an instance of
     *  {@link Stream} that can be easily converted into an array, a String, or
     *  a collection.</p>
     *  <p>To array:</p>
     *  <pre><code>breakText( &lt;<i>text</i>&gt;, &lt;<i>len</i>&gt; ).toArray( String []::new )</code></pre>
     *  <p>To String:</p>
     *  <pre><code>breakText( &lt;<i>text</i>&gt;, &lt;<i>len</i>&gt; ).collect( Collectors.joining() )</code></pre>
     *  <p>To collection (here: a
     *  {@link List}):</p>
     *  <pre><code>breakText( &lt;<i>text</i>&gt;, &lt;<i>len</i>&gt; ).collect( Collectors.toList() )</code></pre>
     *
     *  @param  text    The text.
     *  @param  lineLength  The length of a line.
     *  @return The lines; if a word is longer than the given line length, a
     *      line containing only that word can be longer that the given line
     *      length.
     *
     *  @see Stream#toArray(java.util.function.IntFunction)
     *  @see Stream#collect(java.util.stream.Collector)
     *  @see java.util.stream.Collectors#joining()
     *  @see java.util.stream.Collectors#joining(CharSequence)
     *  @see java.util.stream.Collectors#joining(CharSequence, CharSequence, CharSequence)
     *  @see java.util.stream.Collectors#toList()
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Stream<String> breakText( final CharSequence text, final int lineLength )
    {
        if( lineLength < 1 ) throw new ValidationException( format( "Line length size must not be zero or a negative number: %d", lineLength ) );

        final Builder<String> builder = Stream.builder();

        for( final var line : splitString( requireNonNullArgument( text, "text" ), '\n' ) )
        {
            if( isEmptyOrBlank( line ) )
            {
                builder.add( EMPTY_STRING );
            }
            else
            {
                final var buffer = new StringBuilder();
                final var chunks = line.split( "\\s" );
                SplitLoop: for( final var c : chunks )
                {
                    if( c.isEmpty() ) continue SplitLoop;
                    if( (buffer.length() + 1 + c.length()) < lineLength )
                    {
                        if( isNotEmpty( buffer) ) buffer.append( ' ' );
                    }
                    else
                    {
                        if( isNotEmpty( buffer ) )
                        {
                            builder.add( buffer.toString() );
                            buffer.setLength( 0 );
                        }
                    }
                    buffer.append( c );
                }   //  SplitLoop:
                if( isNotEmpty( buffer ) ) builder.add( buffer.toString() );
            }
        }

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  breakText()

    /**
     *  <p>{@summary <i>Capitalises</i> a String, meaning changing the first
     *  letter to upper case as per
     *  {@link Character#toUpperCase(char)}.} No other letters are changed.</p>
     *  <p>A {@code null} input String returns {@code null}.</p>
     *  <p>Samples:</p>
     *  <pre><code>  StringUtils.capitalize( null )  == null;
     *  StringUtils.capitalize( &quot;&quot; )    == &quot;&quot;;
     *  StringUtils.capitalize( &quot;cat&quot; ) == &quot;Cat&quot;;
     *  StringUtils.capitalize( &quot;cAt&quot; ) == &quot;CAt&quot;;</code></pre>
     *  <p>Use this function to create a getter or setter name from the name of
     *  the attribute.</p>
     *  <p>This method does not recognise the
     *  {@linkplain java.util.Locale#getDefault() default locale}.
     *  This means that &quot;istanbul&quot; will become &quot;Istanbul&quot;
     *  even for the locale {@code tr_TR} (although &quot;&#x130;stanbul&quot;
     *  would be correct).</p>
     *
     *  @param c    The String to capitalise, can be {@code null}.
     *  @return The capitalised String, or {@code null} if the argument
     *      was already {@code null}.
     *
     *  @see #decapitalize(CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String capitalize( final CharSequence c )
    {
        String retValue = null;
        if( nonNull( c ) )
        {
            final var s = c.toString();
            if( s.isEmpty() )
            {
                retValue = s;
            }
            else
            {
                retValue = (toUpperCase( s.charAt( 0 ) ) + s.substring( 1 )).intern();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  capitalize()

    /**
     *  Tests if the given text is not {@code null}, not empty and not
     *  longer than the given maximum length.
     *
     *  @param  name    The name that should appear in the exception if one
     *      will be thrown.
     *  @param  text    The text to check.
     *  @param  maxLength   The maximum length.
     *  @return Always the contents of <code>text</code> as a String; if the
     *      argument fails any of the tests, an
     *      {@link IllegalArgumentException}
     *      or an exception derived from that will be thrown.
     *  @throws CharSequenceTooLongException    {@code text} is longer than
     *      {@code maxLength}.
     *  @throws EmptyArgumentException {@code text} is the empty String.
     *  @throws NullArgumentException   Either {@code name} or {@code text} is
     *      {@code null}.
     *
     *  @deprecated Replaced by
     *      {@link #checkTextLen(String, CharSequence, int)}
     */
    @API( status = DEPRECATED )
    @Deprecated( forRemoval = true )
    public static final String checkText( final String name, final CharSequence text, final int maxLength ) throws CharSequenceTooLongException, EmptyArgumentException, NullArgumentException
    {
        return checkTextLen( name, text, maxLength );
    }   //  checkText()

    /**
     *  Tests if the given text is not {@code null}, not empty and not
     *  longer than the given maximum length. Use this to check whether a
     *  String that is provided as an argument to a method is longer than
     *  expected.
     *
     *  @param  name    The name that should appear in the exception if one
     *      will be thrown. Usually this is the name of the argument to
     *      validate.
     *  @param  text    The text to check.
     *  @param  maxLength   The maximum length.
     *  @return Always the contents of <code>text</code> as a String; if the
     *      argument fails any of the tests, an
     *      {@link IllegalArgumentException}
     *      or an exception derived from that will be thrown.
     *  @throws CharSequenceTooLongException    {@code text} is longer than
     *      {@code maxLength}.
     *  @throws EmptyArgumentException Either {@code name} or {@code text} is
     *      the empty String.
     *  @throws NullArgumentException   Either {@code name} or {@code text} is
     *      {@code null}.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String checkTextLen( final String name, final CharSequence text, final int maxLength ) throws CharSequenceTooLongException, EmptyArgumentException, NullArgumentException
    {
        if( requireNotEmptyArgument( text, requireNotEmptyArgument( name, "name" ) ).length() > maxLength )
        {
            throw new CharSequenceTooLongException( name, maxLength );
        }

        //---* Done *----------------------------------------------------------
        return text.toString();
    }   //  checkTextLen()

    /**
     *  Tests if the given text is not longer than the given maximum length;
     *  different from
     *  {@link #checkTextLen(String, CharSequence, int)},
     *  it may be {@code null} or empty.
     *
     *  @param  name    The name that should appear in the exception if one
     *      will be thrown.
     *  @param  text    The text to check; may be {@code null}.
     *  @param  maxLength   The maximum length.
     *  @return Always the contents of {@code text} as a String, {@code null}
     *      if {@code text} was {@code null}; if the argument fails any of the
     *      tests, an
     *      {@link IllegalArgumentException}
     *      or an exception derived from that will be thrown.
     *  @throws CharSequenceTooLongException    {@code text} is longer than
     *      {@code maxLength}.
     *  @throws EmptyArgumentException  {@code name} is empty.
     *  @throws NullArgumentException   {@code name} is {@code null}.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String checkTextLenNull( final String name, final CharSequence text, final int maxLength ) throws CharSequenceTooLongException
    {
        requireNotEmptyArgument( name, "name" );

        String retValue = null;
        if( nonNull( text ) )
        {
            if( text.length() > maxLength )
            {
                throw new CharSequenceTooLongException( name, maxLength );
            }
            retValue = text.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  checkTextLenNull()

    /**
     *  Tests if the given text is not longer than the given maximum length;
     *  it may be {@code null}.
     *
     *  @param  name    The name that should appear in the exception if one
     *      will be thrown.
     *  @param  text    The text to check; may be {@code null}.
     *  @param  maxLength   The maximum length.
     *  @return Always the contents of {@code text} as a String,
     *      {@code null} if {@code text} was {@code null}; if the
     *      argument fails any of the tests, an
     *      {@link IllegalArgumentException}
     *      or an exception derived from that will be thrown.
     *  @throws CharSequenceTooLongException    {@code text} is longer than
     *      {@code maxLength}.
     *  @throws NullArgumentException   {@code name} is {@code null}.
     *
     *  @deprecated Replaced by
     *      {@link #checkTextLenNull(String, CharSequence, int)}.
     */
    @API( status = DEPRECATED )
    @Deprecated( forRemoval = true )
    public static final String checkTextNull( final String name, final CharSequence text, final int maxLength )
    {
        return checkTextLenNull( name, text, maxLength );
    }   //  checkTextNull()

    /**
     *  Changes the first letter of the given String tolower case as per
     *  {@link Character#toLowerCase(char)}.
     *  No other letters are changed. A {@code null} input String returns
     *  {@code null}.<br>
     *  <br>Samples:<pre><code>  StringUtils.decapitalize( null ) = null;
     *  StringUtils.decapitalize(&quot;&quot;)     = &quot;&quot;;
     *  StringUtils.decapitalize(&quot;Cat&quot;)  = &quot;cat&quot;;
     *  StringUtils.decapitalize(&quot;CAT&quot;)  = &quot;cAT&quot;;</code></pre>
     *  <br>Basically, this is the complementary method to
     *  {@link #capitalize(CharSequence)}.
     *  Use this method to normalise the name of bean attributes.
     *
     *  @param  s   The String to <i>decapitalise</i>, may be {@code null}.
     *  @return The <i>decapitalised</i> String, {@code null} if the argument
     *      was {@code null}.
     *  @see #capitalize(CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String decapitalize( final CharSequence s )
    {
        String retValue = null;

        if( nonNull( s ) )
        {
            retValue = EMPTY_STRING;
            if( isNotEmpty( s ) )
            {
                final var str = s.toString();
                retValue = (toLowerCase( str.charAt( 0 ) ) + str.substring( 1 )).intern();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  decapitalize()

    /**
     *  <p>{@summary Escapes the non-ASCII and special characters in a
     *  {@code String} so that the result can be used in the context of HTML.}
     *  Wherever possible, the method will return the respective HTML&nbsp;5
     *  entity; only when there is no matching entity, it will use the Unicode
     *  escape.</p>
     *  <p>So if you call the method with the argument
     *  &quot;<i>S&uuml;&szlig;e</i>&quot;, it will return
     *  &quot;<code>S&amp;uuml;&amp;szlig;e</code>&quot;.</p>
     *  <p>If the input will be, for example, a Chinese text like this:
     *  &quot;<i>球体</i>&quot; (means "Ball"), you may get back something like
     *  this: &quot;<code>&amp;#x7403;&amp;#x4F53;</code>&quot;, as there are
     *  no entities defined for (any) Chinese letters.</p>
     *  <p>The method supports all known HTML&nbsp;5.0 entities, including
     *  funky accents. But it will not escape several commonly used characters
     *  like the full stop ('.'), the comma (','), the colon (':'), or the
     *  semicolon (';'), although they will be handled properly by
     *  {@link #unescapeHTML(CharSequence)}.</p>
     *  <p>Note that the commonly used apostrophe escape character
     *  (&amp;apos;) that was not a legal entity for HTML before HTML&nbsp;5 is
     *  now supported.</p>
     *
     *  @param  str The {@code String} to escape, may be {@code null}.
     *  @return A new escaped {@code String}, or {@code null} if the
     *      argument was already {@code null}.
     *
     *  @see #unescapeHTML(CharSequence)
     *  @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
     *  @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
     *  @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
     *  @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
     *  @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
     *
     *  @since 0.0.5
     */
    /*
     *  For some unknown reasons, JavaDoc will not accept the entities &#x7403;
     *  and &#x4F53; (for '球' and '体'), therefore it was required to add the
     *  Chinese character directly into the comment above.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String escapeHTML( final CharSequence str )
    {
        final var retValue = nonNull( str ) ? HTML50.escape( str ) : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escapeHTML()

    /**
     *  Escapes the characters in a {@code String} using HTML entities and
     *  writes them to an
     *  {@link Appendable}.
     *  For details, refer to
     *  {@link #escapeHTML(CharSequence)}.
     *
     *  @param  appendable  The appendable object receiving the escaped string.
     *  @param  str  The {@code String} to escape, may be {@code null}.
     *  @throws NullArgumentException   The appendable is {@code null}.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(char)}
     *      method.
     *
     *  @see #escapeHTML(CharSequence)
     *  @see #unescapeHTML(CharSequence)
     *  @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
     *  @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
     *  @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
     *  @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
     *  @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void escapeHTML( final Appendable appendable, final CharSequence str ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" );

        if( nonNull( str ) ) HTML50.escape( appendable, str );
    }   //  escapeHTML()

    /**
     *  Formats the given {@code String} for the output into JSONText. This
     *  means that the input sequence will be surrounded by double quotes, and
     *  backslash sequences are put into all the right places.<br>
     *  <br>&lt; and &gt; will be inserted as their Unicode values, allowing
     *  JSON text to be delivered in HTML.<br>
     *  <br>In JSON text, a string cannot contain a control character or an
     *  unescaped quote or backslash, so these are translated to Unicode
     *  escapes also.
     *
     *  @param  str The string to escape to the JSON format; it may be empty,
     *      but not {@code null}.
     *  @return A string correctly formatted for insertion in a JSON text.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String escapeJSON( final CharSequence str )
    {
        var retValue = "\"\""; // The JSON empty string.
        final var len = requireNonNullArgument( str, "str" ).length();
        if( len > 0 )
        {
            final var buffer = new StringBuilder( len * 2 ).append( '"' );
            char c;
            for( var i = 0; i < len; ++i )
            {
                c = str.charAt( i );
                switch( c )
                {
                    case '\\':
                    case '"':
                    case '<':
                    case '>':
                    case '&':
                        buffer.append( escapeCharacter( c ) );
                        break;

                    case '\b':
                        buffer.append( "\\b" );
                        break;

                    case '\t':
                        buffer.append( "\\t" );
                        break;

                    case '\n':
                        buffer.append( "\\n" );
                        break;

                    case '\f':
                        buffer.append( "\\f" );
                        break;

                    case '\r':
                        buffer.append( "\\r" );
                        break;

                    default:
                        //noinspection OverlyComplexBooleanExpression
                        if( (c < ' ')
                            || ((c >= '\u0080') && (c < '\u00a0'))
                            || ((c >= '\u2000') && (c < '\u2100')) )
                        {
                            buffer.append( escapeCharacter( c ) );
                        }
                        else
                        {
                            buffer.append( c );
                        }
                        break;
                }
            }
            buffer.append( '"' );
            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escapeJSON()

    /**
     *  Escapes the given character using Regex escapes and writes them to a
     *  {@link Appendable}.
     *
     *  @param  appendable  The appendable receiving the escaped string.
     *  @param  c   The character to escape.
     *  @throws NullArgumentException   The appendable is {@code null}.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(CharSequence)}
     *      method.
     *
     *  @since 0.0.5
     */
    @SuppressWarnings( "SwitchStatementWithTooManyBranches" )
    @API( status = STABLE, since = "0.0.5" )
    public static final void escapeRegex( final Appendable appendable, final char c ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" );

        TestSwitch: switch( c )
        {
            case '\\' -> appendable.append( "\\" );
            case '[', ']', '{', '}', '(', ')', '^', '$', '&', '*', '.', '+', '|', '?' -> appendable.append( "\\" ).append( c );
            case '\t' -> appendable.append( "\\t" );
            case '\n' -> appendable.append( "\\n" );
            case '\r' -> appendable.append( "\\r" );
            case '\f' -> appendable.append( "\\f" );
            case '\u0007' -> appendable.append( "\\a" );
            case '\u001B' -> appendable.append( "\\e" ); // ESC
            default -> appendable.append( c );
        }   //  TestSwitch:
    }   //  escapeRegex()

    /**
     *  Escapes the given character using Regex escapes.
     *
     *  @param  c   The character to escape.
     *  @return A {@code String} with the escaped character.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String escapeRegex( final char c )
    {
        final var retValue = new StringBuilder();
        try
        {
            escapeRegex( retValue, c );
        }
        catch( final IOException e )
        {
            /*
             * We append to a StringBuilder, and StringBuilder.append() does
             * not define an IOException.
             */
            throw new ImpossibleExceptionError( e );
        }

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  escapeRegex()

    /**
     *  Escapes the characters in a {@code String} using Regex escapes.
     *
     *  @param  str The {@code String} to escape, may be {@code null}.
     *  @return A new escaped {@code String}, or {@code null} if the argument
     *      was already {@code null}.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String escapeRegex( final CharSequence str )
    {
        String retValue = null;
        if( nonNull( str ) )
        {
            final var len = str.length();
            if( len > 0 )
            {
                final var buffer = new StringBuilder( (len * 12) / 10 );
                try
                {
                    escapeRegex( buffer, str );
                }
                catch( final IOException e )
                {
                    /*
                     * We append to a StringBuilder, and StringBuilder.append() does
                     * not define an IOException.
                     */
                    throw new ImpossibleExceptionError( e );
                }
                retValue = buffer.toString();
            }
            else
            {
                retValue = EMPTY_STRING;
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escapeRegex()

    /**
     *  Escapes the characters in a {@code String} using Regex escapes and
     *  writes them to a
     *  {@link Appendable}.
     *
     *  @param  appendable  The appendable receiving the escaped string.
     *  @param  str  The {@code String} to escape. If {@code null} or the empty
     *      String, nothing will be put to the appendable.
     *  @throws NullArgumentException   The appendable is {@code null}.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(CharSequence)}
     *      method.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void escapeRegex( final Appendable appendable, final CharSequence str ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" );

        if( isNotEmpty( str ) )
        {
            ScanLoop: for( var i = 0; i < str.length(); ++i )
            {
                escapeRegex( appendable, str.charAt( i ) );
            }   //  ScanLoop:
        }
    }   //  escapeRegex()

    /**
     *  Returns the Unicode escape sequence for the given character. This will
     *  return &quot;&#92;u0075&quot; for the letter 'u', and
     *  &quot;&#92;u003c&quot; for the smaller-than sign '&lt;'.<br>
     *  <br>This method should be used only for characters that are not
     *  surrogates; for general use, the implementation that takes a code point
     *  is preferred.
     *
     *  @param  c   The character.
     *  @return The escape sequence.
     *
     *  @see #escapeUnicode(int)
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link CharSetUtils#escapeCharacter(char)}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String escapeUnicode( final char c ) { return escapeCharacter( c ); }

    /**
     *  Returns the Unicode escape sequence for the given code point. This will
     *  return &quot;&#92;u0075&quot; for the letter 'u', and
     *  &quot;&#92;u003c&quot; for the smaller-than sign '&lt;'.<br>
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
     *  {@link #toUnicode(CharSequence)}.
     *
     *  @param  codePoint   The character.
     *  @return The escape sequence.
     *
     *  @see String#codePoints()
     *  @see java.util.stream.IntStream#mapToObj(java.util.function.IntFunction)
     *  @see java.util.stream.Stream#collect(java.util.stream.Collector)
     *  @see java.util.stream.Collectors#joining()
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link CharSetUtils#escapeCharacter(int)}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String escapeUnicode( final int codePoint ) { return escapeCharacter( codePoint ); }

    /**
     *  <p>{@summary Escapes the characters in a {@code String} using XML
     *  entities.}</p>
     *  <p>For example:</p>
     *  <p>{@code "bread" & "butter"}</p>
     *  <p>becomes:</p>
     *  <p><code>&amp;quot;bread&amp;quot; &amp;amp;
     *  &amp;quot;butter&amp;quot;</code>.</p>
     *
     *  @param  str The {@code String} to escape, may be null.
     *  @return A new escaped {@code String}, or {@code null} if the
     *      argument was already {@code null}.
     *
     *  @see #unescapeXML(CharSequence)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String escapeXML( final CharSequence str )
    {
        final var retValue = nonNull( str ) ? XML.escape( str ) : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escapeXML()

    /**
     *  <p>{@summary Escapes the characters in a {@code String} using XML
     *  entities and writes them to an
     *  {@link Appendable}.}</p>
     *  <p>For example:</p>
     *  <p>{@code "bread" & "butter"}</p>
     *  <p>becomes:</p>
     *  <p><code>&amp;quot;bread&amp;quot; &amp;amp;
     *  &amp;quot;butter&amp;quot;</code>.</p>
     *
     *  @param  appendable  The appendable object receiving the escaped string.
     *  @param  str  The {@code String} to escape, may be {@code null}.
     *  @throws NullArgumentException   The appendable is {@code null}.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(char)}
     *      method.
     *
     *  @see #escapeXML(CharSequence)
     *  @see #unescapeXML(CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void escapeXML( final Appendable appendable, final CharSequence str ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" );

        if( nonNull( str ) ) XML.escape( appendable, str );
    }   //  escapeXML()

    /**
     *  Collects all the variables of the form
     *  <code>${<i>&lt;name&gt;</i>}</code> in the given String.<br>
     *  <br>If there are not any variables in the given String, an empty
     *  {@link Collection}
     *  will be returned.<br>
     *  <br>A valid variable name may not contain any other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @return A {@code Collection} with the variable (names).
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link Template#findVariables(CharSequence)}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final Collection<String> findVariables( final CharSequence text )
    {
        return Template.findVariables( text );
    }   //  findVariables()

    /**
     *  Returns a formatted String using the specified format String and
     *  arguments.<br>
     *  <br>The implementation uses internally the
     *  {@link Locale}
     *  that is returned by
     *  {@link java.util.Locale#getDefault() Locale.getDefault()}.<br>
     *  <br>This method is meant as a replacement for the method
     *  {@link java.lang.String#format(String, Object...)};
     *  that implementation always uses a new instance of
     *  {@link Formatter}
     *  for each invocation. The implementation here uses a cached instance
     *  instead, and as {@code Formatter} is not inherently thread-safe, this
     *  cached instance is created and stored per thread, therefore no
     *  synchronisation is needed.<br>
     *  <br>This implementation is about as twice as fast as
     *  {@code java.lang.String.format()}.
     *
     *  @param  format  A format String with the syntax as described for the
     *      {@link Formatter}
     *      class.
     *  @param  args    The arguments referenced by the format specifiers in
     *      the {@code format} String. If there are more arguments than format
     *      specifiers, the extra arguments are ignored. The number of
     *      arguments is variable and may be zero. The maximum number of
     *      arguments is limited by the maximum dimension of a Java array as
     *      defined by <cite>The Java&trade; Virtual Machine
     *      Specification</cite>. The behaviour on a {@code null} argument
     *      depends on the conversion.
     *  @throws IllegalFormatException A format string contains an illegal
     *      syntax, a format specifier that is incompatible with the given
     *      arguments, insufficient arguments given the format string, or other
     *      illegal conditions occurred. For specification of all possible
     *      formatting errors, see the &quot;Details&quot; section of the
     *      {@code Formatter} class specification.
     *  @return A formatted String.
     *
     *  @see  java.util.Formatter
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String format( final String format, final Object... args ) throws IllegalFormatException
    {
        return SharedFormatter.format( format, args );
    }   //  format()

    /**
     *  Returns a formatted String using the specified
     *  {@link Locale},
     *  format String, and arguments.<br>
     *  <br>This method is meant as a replacement for the method
     *  {@link java.lang.String#format(String, Object...)};
     *  that implementation always uses a new instance of
     *  {@link Formatter}
     *  for each invocation. The implementation here uses a cached instance
     *  instead, and as {@code Formatter} is not inherently thread-safe, this
     *  cached instance is created and stored per thread, therefore no
     *  synchronisation is needed.<br>
     *  <br>The performance gain for this implementation is not that impressive
     *  when compared with that of
     *  {@link #format(String, Object...)},
     *  but still in the 10% range.
     *  {@code java.lang.String.format()}.
     *
     *  @param  locale  The
     *      {@link Locale}
     *      that will be applied during formatting. If {@code locale} is
     *      {@code null} then no localisation is applied.
     *  @param  format  A format String with the syntax as described for the
     *      {@link Formatter}
     *      class.
     *  @param  args    The arguments referenced by the format specifiers in
     *      the {@code format} String. If there are more arguments than format
     *      specifiers, the extra arguments are ignored. The number of
     *      arguments is variable and may be zero. The maximum number of
     *      arguments is limited by the maximum dimension of a Java array as
     *      defined by <cite>The Java&trade; Virtual Machine
     *      Specification</cite>. The behaviour on a {@code null} argument
     *      depends on the conversion.
     *  @throws IllegalFormatException A format string contains an illegal
     *      syntax, a format specifier that is incompatible with the given
     *      arguments, insufficient arguments given the format string, or other
     *      illegal conditions occurred. For specification of all possible
     *      formatting errors, see the &quot;Details&quot; section of the
     *      {@code Formatter} class specification.
     *  @return A formatted String.
     *
     *  @see  java.util.Formatter
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String format( final Locale locale, final String format, final Object... args ) throws IllegalFormatException
    {
        return SharedFormatter.format( locale, format, args );
    }   //  format()

    /**
     *  Checks whether the given String contains at least one variable of the
     *  form <code>${<i>&lt;name&gt;</i>}</code> (matching the pattern given in
     *  {@link #VARIABLE_PATTERN}).
     *
     *  @param  s   The String to test; can be {@code null}.
     *  @return {@code true} if the String contains at least one variable,
     *      {@code false} otherwise.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.0.4
     *  @deprecated Use
     *      {@link Template#hasVariables(CharSequence)}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = STABLE, since = "0.0.4" )
    public static final boolean hasVariable( final CharSequence s )
    {
        return Template.hasVariables( s );
    }   //  hasVariable()

    /**
     *  Tests if the given String is {@code null} or the empty String.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is
     *      {@code null} or the empty String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isEmpty( final CharSequence s ) { return isNull( s ) || s.isEmpty(); }

    /**
     *  Tests if the given String is {@code null}, the empty String, or just
     *  containing whitespace.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is not
     *      {@code null} and not the empty String.
     *
     *  @see String#isBlank()
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isEmptyOrBlank( final CharSequence s )
    {
        final var retValue = isNull( s ) || s.toString().isBlank();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isEmptyOrBlank()

    /**
     *  Tests if the given String is not {@code null} and not the empty
     *  String.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is not
     *      {@code null} and not the empty String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isNotEmpty( final CharSequence s ) { return nonNull( s ) && !s.isEmpty(); }

    /**
     *  Tests if the given String is not {@code null}, not the empty String,
     *  and that it contains other characters than just whitespace.
     *
     *  @param  s   The String to test.
     *  @return {@code true} if the given String reference is not
     *      {@code null} and not the empty String, and it contains other
     *      characters than just whitespace.
     *
     *  @see String#isBlank()
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isNotEmptyOrBlank( final CharSequence s )
    {
        final var retValue = nonNull( s ) && !s.toString().isBlank();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isNotEmptyOrBlank()

    /**
     *  Test whether the given String is a valid variable name.
     *
     *  @param  name    The bare variable name, without the surrounding
     *      &quot;${&hellip;}&quot;.
     *  @return {@code true} if the given name is valid for a variable name,
     *      {@code false} otherwise.
     *
     *  @see #VARIABLE_PATTERN
     *  @see #findVariables(CharSequence)
     *  @see #replaceVariable(CharSequence, Map...)
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link Template#isValidVariableName(CharSequence)}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final boolean isValidVariableName( final CharSequence name )
    {
        return Template.isValidVariableName( name );
    }   //  isValidVariableName()

    /**
     *  Checks whether the given String is a variable in the form
     *  <code>${<i>&lt;name&gt;</i>}</code>, according to the pattern provided
     *  in
     *  {@link #VARIABLE_PATTERN}.
     *
     *  @param  s   The String to test; can be {@code null}.
     *  @return {@code true} if the given String is not {@code null}, not the
     *      empty String, and it matches the given pattern, {@code false}
     *      otherwise.
     *
     *  @since 0.0.4
     *  @deprecated Use
     *      {@link Template#isVariable(CharSequence)}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.4" )
    public static final boolean isVariable( final CharSequence s )
    {
        return Template.isVariable( s );
    }   //  isVariable()

    /**
     *  Combines the given String array to a single string, with the given
     *  separator.
     *
     *  @param  s   The input sequence.
     *  @param  separator   The separator; may be empty, but not {@code null}.
     *  @return The combined String. If the input sequence array is empty, the
     *      empty String will be returned.
     *
     *  @deprecated Use
     *      {@link java.lang.String#join(CharSequence, CharSequence...)}
     *      instead.
     */
    @API( status = DEPRECATED, since = "0.0.4" )
    @Deprecated( since = "Java 9", forRemoval = true )
    public static final String joinString( final CharSequence [] s, final CharSequence separator )
    {
        final var retValue = join( requireNonNullArgument( separator, "separator" ), requireNonNullArgument( s, "s" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  joinString()

    /**
     *  Combines the given
     *  {@link Collection}
     *  of String to a single string, with the given separator.
     *
     *  @param  s   The input sequence.
     *  @param  separator   The separator; may be empty, but not {@code null}.
     *  @return The combined String. If the input sequence collection is empty,
     *      the empty String will be returned.
     *
     *  @deprecated Use
     *      {@link java.lang.String#join(CharSequence,Iterable)}
     *      instead.
     */
    @API( status = DEPRECATED, since = "0.0.4" )
    @Deprecated( since = "Java 9", forRemoval = true )
    public static final String joinString( final Collection<? extends CharSequence> s, final CharSequence separator )
    {
        final var retValue = join( requireNonNullArgument( separator, "separator" ), requireNonNullArgument( s, "s" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  joinString()

    /**
     *  Combines the given
     *  {@link Stream}
     *  of Strings to a single string, with the given separator.
     *
     *  @param  s   The input sequence.
     *  @param  separator   The separator; may be empty, but not {@code null}.
     *  @return The combined String. If the input sequence stream is empty,
     *      the empty String will be returned.
     *
     *  @deprecated This method is obsolete; the same effect can be achieved
     *      easily by calling
     *      {@link Stream#collect(java.util.stream.Collector)}
     *      on the stream, with the collector
     *      {@link java.util.stream.Collectors#joining(CharSequence) joining()}
     *      as its argument.
     */
    @API( status = DEPRECATED, since = "0.0.4" )
    @Deprecated( forRemoval = true, since = "Java 8" )
    public static final String joinString( final Stream<? extends CharSequence> s, final CharSequence separator )
    {
        final var retValue = requireNonNullArgument( s, "s" ).collect( joining( separator ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  joinString()

    /**
     *  Combines the given String array to a single string, with the given
     *  separator.
     *
     *  @param  s   The input sequence.
     *  @param  separator   The separator.
     *  @return The combined String. If the input sequence array is empty, the
     *      empty String will be returned.
     *
     *  @deprecated This method is obsolete; the same effect can be achieved
     *      easily by calling
     *      {@link String#join(CharSequence, CharSequence...)}.
     */
    @Deprecated( forRemoval = true, since = "Java 8" )
    @API( status = DEPRECATED, since = "0.0.4" )
    public static final String joinString( final CharSequence [] s, final char separator )
    {
        final var retValue = join( Character.toString( separator), requireNonNullArgument( s, "s" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  joinString()

    /**
     *  Combines the given String array to a single string, with the given
     *  separator.
     *
     *  @param  s   The input sequence.
     *  @param  separator   The separator.
     *  @return The combined String. If the input sequence collection is empty, the
     *      empty String will be returned.
     *
     *  @deprecated This method is obsolete; the same effect can be achieved
     *      easily by calling
     *      {@link String#join(CharSequence, Iterable)}
     */
    @Deprecated( forRemoval = true, since = "Java 8" )
    @API( status = DEPRECATED, since = "0.0.4" )
    public static final String joinString( final Iterable<? extends CharSequence> s, final char separator )
    {
        final var retValue = join( Character.toString( separator), requireNonNullArgument( s, "s" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  joinString()

    /**
     *  Determines the maximum length over all strings provided in the given
     *  collection.<br>
     *  <br>For an array, this method can be called like this:<pre><code>  String [] a = {"1", "2", "3"};
     *  maxContentLength( Arrays.asList( a ) );</code></pre>
     *
     *  @param  list    The strings.
     *  @return The length of the longest string in the list; -1 if all values
     *      in the list are {@code null}.
     *
     *  @deprecated Use
     *      {@link #maxContentLength(CharSequence[])},
     *      {@link #maxContentLength(Collection)},
     *      or
     *      {@link #maxContentLength(Stream)}
     *      instead.
     */
    @Deprecated( forRemoval = true )
    @API( status = DEPRECATED )
    public static final int maxContentLength( final Iterable<? extends CharSequence> list )
    {
        var retValue = Integer.MIN_VALUE;
        int len;
        for( final var cs : requireNonNullArgument( list, "list" ) )
        {
            len = nonNull( cs ) ? cs.length() : -1;
            if( len > retValue )
            {
                retValue = len;
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  maxContentLength()

    /**
     *  Determines the maximum length over all Strings provided in the given
     *  {@link Stream}.
     *
     *  @param  stream  The strings.
     *  @return The length of the longest string in the list; -1 if all values
     *      in the given {@code stream} are {@code null}, and
     *      {@link Integer#MIN_VALUE}
     *      if the given {@code stream} is empty.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int maxContentLength( final Stream<? extends CharSequence> stream )
    {
        final var retValue = requireNonNullArgument( stream, "stream" )
            .mapToInt( s -> nonNull( s ) ? s.length() : -1 )
            .max()
            .orElse( Integer.MIN_VALUE );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  maxContentLength()

    /**
     *  Determines the maximum length over all strings provided in the given
     *  {@link Collection}.
     *
     *  @param  list    The strings.
     *  @return The length of the longest string in the list; -1 if all values
     *      in the given {@code list} are {@code null}, and
     *      {@link Integer#MIN_VALUE}
     *      if the given {@code list} is empty.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int maxContentLength( final Collection<? extends CharSequence> list )
    {
        final var retValue = maxContentLength( requireNonNullArgument( list, "list" ).stream() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  maxContentLength()

    /**
     *  Determines the maximum length over all strings provided in the given
     *  array.
     *
     *  @param  a   The strings.
     *  @return The length of the longest string in the list; -1 if all values
     *      in the array are {@code null}, and
     *      {@link Integer#MIN_VALUE}
     *      if the given array has zero length.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int maxContentLength( final CharSequence... a )
    {
        final var retValue = maxContentLength( Arrays.stream( requireNonNullArgument( a, "a" ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  maxContentLength()

    /**
     *  Brings the given string to the given length and uses the provided
     *  padding character to fill up the string.
     *
     *  @param  s   The string to format.
     *  @param  length  The desired length; if 0 or less, the given string is
     *      returned, regardless of {@code clip}.
     *  @param  c   The pad character.
     *  @param  mode    The
     *      {@linkplain StringUtils.Padding pad mode}.
     *  @param  clip    {@code true} if the input string {@code s} should
     *      be cut in case it is longer than {@code length}, {@code false}
     *      if it has to be returned unchanged .
     *  @return The re-formatted string.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String pad( final CharSequence s, final int length, final char c, final Padding mode, final boolean clip )
    {
        return pad( s, length, c, mode, clip ? CLIPPING_CUT : CLIPPING_NONE );
    }   //  pad()

    /**
     *  Brings the given string to the given length and uses the provided
     *  padding character to fill up the string.
     *
     *  @param  s   The string to format.
     *  @param  length  The desired length; if 0 or less, the given string is
     *      returned, regardless of {@code clip}.
     *  @param  c   The pad character.
     *  @param  mode    The
     *      {@linkplain StringUtils.Padding pad mode}.
     *  @param  clip    The
     *      {@linkplain StringUtils.Clipping clipping mode}.
     *  @return The re-formatted string.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String pad( final CharSequence s, final int length, final char c, final Padding mode, final Clipping clip )
    {
        //noinspection OverlyComplexBooleanExpression
        if( ((requireNonNullArgument( clip, "clip" ) == CLIPPING_ABBREVIATE) && (length < 4)) || ((clip == CLIPPING_ABBREVIATE_MIDDLE) && (length < 5)) )
        {
            throw new ValidationException( format( "Length %d is too short for clipping mode %s", length, clip.toString() ) );
        }
        requireNonNullArgument( mode, "mode" );

        final String retValue;
        final var currentLength = requireNonNullArgument( s, "s" ).length();

        if( (length > 0) && (length != currentLength) )
        {
            if( currentLength > length )
            {
                retValue = clip.clip( s, length );
            }
            else
            {
                final var padSize = length - currentLength;
                retValue = mode.pad( s, padSize, c );
            }
        }
        else
        {
            retValue = s.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  pad()

    /**
     *  Fills up the given string to the given length by adding blanks on both
     *  sides; will abbreviate the string if it is longer than the given
     *  length. The minimum length is 5.<br>
     *  <br>This is a shortcut to a call to
     *  {@link #pad(CharSequence,int,char,Padding,Clipping) pad( s, length, ' ', PADDING_CENTER, CLIPPING_ABBREVIATE_MIDDLE ) }.
     *
     *  @param  s   The string to format.
     *  @param  length  The desired length; minimum value is 5.
     *  @return The re-formatted string.
     *
     *  @see Padding#PADDING_CENTER
     *  @see Clipping#CLIPPING_ABBREVIATE_MIDDLE
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String padCenter( final CharSequence s, final int length ) { return pad( s, length, ' ', PADDING_CENTER, CLIPPING_ABBREVIATE_MIDDLE ); }

    /**
     *  Fills up the given string to the given length by adding blanks on the
     *  left side;  will abbreviate the string if it is longer than the given
     *  length. The minimum length is 4.<br>
     *  <br>This is a shortcut to a call to
     *  {@link #pad(CharSequence,int,char,Padding,Clipping) pad( s, length, ' ', PADDING_LEFT, CLIPPING_ABBREVIATE ) }.
     *
     *  @param  s   The string to format.
     *  @param  length  The desired length; the minimum value is 4.
     *  @return The re-formatted string.
     *
     *  @see Padding#PADDING_LEFT
     *  @see Clipping#CLIPPING_ABBREVIATE
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String padLeft( final CharSequence s, final int length ) { return pad( s, length, ' ', PADDING_LEFT, CLIPPING_ABBREVIATE ); }

    /**
     *  Fills up the given string to the given length by adding blanks on the
     *  right side;   will abbreviate the string if it is longer than the given
     *  length. The minimum length is 4.<br>
     *  <br>This is a shortcut to a call to
     *  {@link #pad(CharSequence,int,char,Padding,Clipping) pad( s, length, ' ', PADDING_RIGHT, CLIPPING_ABBREVIATE ) }.
     *
     *  @param  s   The string to format.
     *  @param  length  The desired length; the minimum value is 4.
     *  @return The re-formatted string.
     *
     *  @see Padding#PADDING_RIGHT
     *  @see Clipping#CLIPPING_ABBREVIATE
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String padRight( final CharSequence s, final int length ) { return pad( s, length, ' ', PADDING_RIGHT, CLIPPING_ABBREVIATE ); }

    /**
     *  <p>{@summary Surrounds the given String with double-quotes
     *  (&quot;, &amp;#34;).}</p>
     *  <p>When the double-quote is needed in a String constant, it has to be
     *  escaped with a backslash:</p>
     *  <pre><code>&quot;\&quot;…\&quot;&quot;</code></pre>
     *  <p>Sometimes, this is just ugly, and there this method comes into
     *  play.</p>
     *
     *  @param  s   The String to surround; can be {@code null}.
     *  @return The quoted String; will be {@code null} if the argument was
     *      {@code null} already.
     */
    public static final String quote( final CharSequence s )
    {
        final var retValue = isNull( s ) ? null : format( "\"%s\"u\"", s );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  quote()

    /**
     *  Repeats the given char {@code repeat} to form a new String. The table
     *  below shows the various  result for some argument combinations.<br>
     *  <br><code>
     *  StringUtils.repeat(&nbsp;'a',&nbsp;0&nbsp;)&nbsp;&rArr;&nbsp;""<br>
     *  StringUtils.repeat(&nbsp;'a',&nbsp;3&nbsp;)&nbsp;&rArr;&nbsp;"aaa"<br>
     *  StringUtils.repeat(&nbsp;'a',&nbsp;-2&nbsp;)&nbsp;&rArr;&nbsp;""<br>
     *  </code>
     *
     *  @param  c   The character to repeat.
     *  @param  repeat  The number of times to repeat {@code c}; a negative
     *      value will be treated as zero.
     *  @return A new String consisting of the given character repeated
     *      {@code repeat} times, or the empty String if {@code repeat} was 0
     *      or negative.
     *
     *  @see String#repeat(int)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String repeat( final char c, final int repeat )
    {
        final var retValue = (repeat > 0 ? Character.toString( c ).repeat( repeat ) : EMPTY_STRING).intern();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  repeat()

    /**
     *  Repeats the given char {@code repeat}, identified by its code point, to
     *  form a new String. The
     *  table below shows the various  result for some argument
     *  combinations.<br>
     *  <br><code>
     *  StringUtils.repeat(&nbsp;'a',&nbsp;0&nbsp;)&nbsp;&rArr;&nbsp;""<br>
     *  StringUtils.repeat(&nbsp;'a',&nbsp;3&nbsp;)&nbsp;&rArr;&nbsp;"aaa"<br>
     *  StringUtils.repeat(&nbsp;'a',&nbsp;-2&nbsp;)&nbsp;&rArr;&nbsp;""<br>
     *  </code>
     *
     *  @param  codePoint   The character to repeat.
     *  @param  repeat  The number of times to repeat {@code c}; a negative
     *      value will be treated as zero.
     *  @return A new String consisting of the given character repeated
     *      {@code repeat} times, or the empty String if {@code repeat} was 0
     *      or negative, or {@code null} if the code point is invalid.
     *
     *  @see Character#isValidCodePoint(int)
     *  @see String#repeat(int)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String repeat( final int codePoint, final int repeat )
    {
        final var retValue = (repeat > 0)
            ? isValidCodePoint( codePoint )
                ? Character.toString( codePoint ).repeat( repeat ).intern()
                : null
            : EMPTY_STRING;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  repeat()

    /**
     *  Repeats the given String {@code repeat} times to form a new String. The
     *  table below shows the various  result for some argument
     *  combinations.<br>
     *  <br><code>
     *  StringUtils.repeat(&nbsp;null,&nbsp;2&nbsp;)&nbsp;&rArr;&nbsp;null<br>
     *  StringUtils.repeat(&nbsp;"",&nbsp;0&nbsp;)&nbsp;&rArr;&nbsp;""<br>
     *  StringUtils.repeat(&nbsp;"",&nbsp;2&nbsp;)&nbsp;&rArr;&nbsp;""<br>
     *  StringUtils.repeat(&nbsp;"a",&nbsp;3&nbsp;)&nbsp;&rArr;&nbsp;"aaa"<br>
     *  StringUtils.repeat(&nbsp;"ab",&nbsp;2&nbsp;)&nbsp;&rArr;&nbsp;"abab"<br>
     *  StringUtils.repeat(&nbsp;"a",&nbsp;-2&nbsp;)&nbsp;&rArr;&nbsp;""<br>
     *  </code>
     *
     *  @param  s The String to repeat, may be {@code null}.
     *  @param repeat   The number of times to repeat {@code str}; a negative
     *      value will be treated as zero.
     *  @return A new String consisting of the original String repeated,
     *      {@code repeat} times, the empty String if {@code repeat} was 0
     *      or negative, or {@code null} if the input String was
     *      {@code null}, too.
     *
     *  @see String#repeat(int)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String repeat( final CharSequence s, final int repeat )
    {
        final var retValue =
            nonNull( s )
                ? (repeat > 0) && !s.isEmpty()
                    ? s.toString().repeat( repeat )
                    : EMPTY_STRING
                : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  repeat()

    /**
     *  Replaces the variables of the form <code>${&lt;<i>name</i>&gt;}</code>
     *  in the given String with values from the given maps. The method will
     *  try the maps in the given sequence.<br>
     *  <br>If no replacement value could be found, the variable will not be
     *  replaced at all.<br>
     *  <br>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.<br>
     *  <br>The variables names are case-sensitive.<br>
     *  <br>Valid variable name may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @param  sources The maps with the replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link Template#replaceVariable(CharSequence, Map[])}
     *      instead.
     */
    @SafeVarargs
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String replaceVariable( final CharSequence text, final Map<String,? extends Object>... sources )
    {
        return Template.replaceVariable( text, sources );
    }   //  replaceVariable()

    /**
     *  Replaces the variables of the form <code>${&lt;<i>name</i>&gt;}</code>
     *  in the given String with values returned by the given retriever
     *  function for the variable name.<br>
     *  <br>If no replacement value could be found, the variable will not be
     *  replaced at all.<br>
     *  <br>If the retriever function returns a value that contains a variable
     *  itself, this will not be replaced.<br>
     *  <br>The retriever function will be called only once for each variable
     *  name; if the text contains the same variable multiple times, it will
     *  always be replaced with the same value.<br>
     *  <br>The variables names are case-sensitive.<br>
     *  <br>Valid variable name may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.<br>
     *  <br>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').<br>
     *  <br>The prefix character is part of the name.<br>
     *  <br>Finally, there is the single underscore that is allowed as a
     *  special variable.
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @param  retriever   The function that will retrieve the replacement
     *      values for the given variable names.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link Template#replaceVariable(CharSequence, Function)}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String replaceVariable( final CharSequence text, final Function<? super String, Optional<String>> retriever )
    {
        return Template.replaceVariable( text, retriever );
    }   //  replaceVariable()

    /**
     *  Splits a String by the given separator character and returns an array
     *  of all parts. In case a separator character is immediately followed by
     *  another separator char, an empty String will be place to the array.
     *  Beginning and end of the String are treated as separators, so if the
     *  first character of the String is a separator, the returned array will
     *  start with an empty string, as it will end with an empty String if the
     *  last character is a separator.<br>
     *  <br>In case the String is empty, the return value will be an array
     *  containing just the empty String.
     *
     *  @param  string  The String to split.
     *  @param  separator   The separator character.
     *  @return The parts of the String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] splitString( final CharSequence string, final char separator )
    {
        return splitString( string, (int) separator );
    }   //  splitString()

    /**
     *  Splits a String by the given separator character, identified by its
     *  Unicode code point, and returns an array of all parts. In case a
     *  separator character is immediately followed by another separator char,
     *  an empty String will be placed to the array. Beginning and end of the
     *  String are treated as separators, so if the first character of the
     *  String is a separator, the returned array will start with an empty
     *  String, as it will end with an empty String if the last character is a
     *  separator.<br>
     *  <br>In case the String is empty, the return value will be an array
     *  containing just the empty String.
     *
     *  @param  string  The String to split.
     *  @param  separator   The code point for the separator character.
     *  @return The parts of the String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] splitString( final CharSequence string, final int separator )
    {
        final var retValue = stream( string, separator ).toArray( String []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  splitString()

    /**
     *  Splits a String by the given separator sequence and returns an array
     *  of all parts. In case a separator sequence is immediately followed by
     *  another separator sequence, an empty String will be placed to the
     *  array. Beginning and end of the String are treated as separators, so if
     *  the first part of the String equals the separator sequence, the
     *  returned array will start with an empty String, as it will end with an
     *  empty string if the last part would equal the separator sequence..<br>
     *  <br>In case the String is empty, the return value will be an array
     *  containing just the empty String.
     *
     *  @param  string  The String to split.
     *  @param  separator   The separator sequence.
     *  @return The parts of the String.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] splitString( final CharSequence string, final CharSequence separator )
    {
        final var retValue = stream( string, separator).toArray( String []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  splitString()

    /**
     *  Splits a String by the given separator character and returns a
     *  {@link Stream}
     *  of all parts. In case a separator character is immediately followed by
     *  another separator char, an empty String will be put to the
     *  {@code Stream}. Beginning and end of the String are treated as
     *  separators, so if the first character of the String is a separator, the
     *  returned {@code Stream} will start with an empty String, as it will end
     *  with an empty String if the last character is a separator.<br>
     *  <br>In case the String is empty, the return value will be a
     *  {@code Stream} containing just the empty String.
     *
     *  @param  string  The String to split.
     *  @param  separator   The separator character.
     *  @return A {@code Stream} instance with the parts of the String.
     *
     *  @since 0.0.7
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final Stream<String> stream( final CharSequence string, final char separator )
    {
        return stream( string, (int) separator );
    }   //  stream()

    /**
     *  <p>{@summary Splits a String by the given separator character, identified by its
     *  Unicode code point, and returns a
     *  {@link Stream}
     *  of all parts.} In case a separator character is immediately followed by
     *  another separator char, an empty String will be put to the
     *  {@code Stream}. Beginning and end of the String are treated as
     *  separators, so if the first character of the String is a separator, the
     *  returned {@code Stream} will start with an empty String, as it will end
     *  with an empty String if the last character is a separator.</p>
     *  <p>In case the String is empty, the return value will be a
     *  {@code Stream} containing just the empty String.</p>
     *
     *  @param  string  The String to split.
     *  @param  separator   The code point for the separator character.
     *  @return A {@code Stream} instance with the parts of the String.
     *
     *  @since 0.0.7
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final Stream<String> stream( final CharSequence string, final int separator )
    {
        //---* Process the string *--------------------------------------------
        final var s = requireNonNullArgument( string, "string" ).codePoints().toArray();
        final var builder = Stream.<String>builder();
        var begin = -1;
        for( var i = 0 ; i < s.length; ++i )
        {
            if( begin == -1 )
            {
                begin = i;
            }
            if( s [ i ] == separator )
            {
                builder.add( new String( s, begin, i - begin ).intern() );
                begin = -1;
            }
        }

        //---* Add the rest *--------------------------------------------------
        if( (begin >= 0) && (begin < s.length) )
        {
            builder.add( new String( s, begin, s.length - begin ).intern() );
        }
        if( (s.length == 0) || (s [s.length - 1] == separator) )
        {
            builder.add( EMPTY_STRING );
        }

        //---* Create the return value *---------------------------------------
        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  stream()

    /**
     *  Splits a String by the given separator sequence and returns a
     *  {@link Stream}
     *  of all parts. In case a separator sequence is immediately followed by
     *  another separator sequence, an empty String will be put to the
     *  {@code Stream}. Beginning and end of the String are treated as
     *  separators, so if the first part of the String equals the separator
     *  sequence, the returned {@code Stream} will start with an empty string,
     *  as it will end with an empty String if the last part would equal the
     *  separator sequence.<br>
     *  <br>In case the String is empty, the return value will be a
     *  {@code Stream} containing just the empty String.
     *
     *  @param  string  The String to split.
     *  @param  separator   The separator sequence.
     *  @return The parts of the String.
     *
     *  @since 0.0.7
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final Stream<String> stream( final CharSequence string, final CharSequence separator )
    {
        //---* Process the string *--------------------------------------------
        var s = requireNonNullArgument( string, "string" ).toString();
        final var t = requireNotEmptyArgument( separator, "separator" ).toString();

        final var builder = Stream.<String>builder();
        var pos = Integer.MAX_VALUE;
        while( isNotEmpty( s ) && (pos >= 0) )
        {
            pos = s.indexOf( t );
            switch( Integer.signum( pos ) )
            {
                case 0 -> /* String starts with separator */
                    {
                        builder.add( EMPTY_STRING );
                        s = s.substring( t.length() );
                    }
                case 1 -> /* String contains a separator somewhere */
                    {
                        builder.add( s.substring( 0, pos ) );
                        s = s.substring( pos + t.length() );
                    }
                case -1 -> { /* Just leave the loop */ }
            }   //  ResultHandlerSwitch:
        }

        //---* Add the rest *--------------------------------------------------
        builder.add( s );

        //---* Create the return value *---------------------------------------
        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  stream()

    /**
     *  Splits a String using the given regular expression and returns a
     *  {@link Stream}
     *  of all parts. In case a separator sequence is immediately followed by
     *  another separator sequence, an empty String will be put to the
     *  {@code Stream}. Beginning and end of the String are treated as
     *  separators, so if the first part of the String equals the separator
     *  sequence, the returned {@code Stream} will start with an empty string,
     *  as it will end with an empty String if the last part would equal the
     *  separator sequence.<br>
     *  <br>In case the String is empty, the return value will be a
     *  {@code Stream} containing just the empty String.
     *
     *  @note This method behaves different from
     *      {@link String#split(String)}
     *      as it will return trailing empty Strings.
     *
     *  @param  string  The String to split.
     *  @param  pattern The separator sequence.
     *  @return The parts of the String.
     *
     *  @see String#split(String)
     *  @see Pattern#split(CharSequence)
     *
     *  @since 0.0.7
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final Stream<String> stream( final CharSequence string, final Pattern pattern )
    {
        requireNonNullArgument( pattern, "pattern" );

        //---* Process the string *--------------------------------------------
        final var builder = Stream.<String>builder();
        if( isEmpty( requireNonNullArgument( string, "string" ) ) )
        {
            builder.add( EMPTY_STRING );
        }
        else
        {
            final var parts = pattern.split( string );
            for( final var part : parts )
            {
                builder.add( part );
            }
            final var matcher = pattern.matcher( string );
            var count = 0;
            while( matcher.find() ) ++count;
            //noinspection ForLoopWithMissingComponent
            for( ; count >= parts.length; --count )
            {
                builder.add( EMPTY_STRING );
            }
        }

        //---* Create the return value *---------------------------------------
        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  stream()

    /**
     *  Strips HTML or XML tags from the given String, without touching other
     *  entities (like {@code &amp;} or {@code &nbsp;}). The result would be
     *  the effective text, stripped from all other whitespace (except single
     *  blanks).<br>
     *  <br>This means that the result for
     *  <pre><code>stripTags( &quot;&lt;html&gt;
     *      &lt;head&gt;
     *        &hellip;
     *      &lt;/head&gt;
     *      &lt;body&gt;
     *        &lt;a href='&hellip;'&gt;       Simple          &lt;br&gt;
     *          &lt;br&gt;           Text       &lt;/a&gt;
     *      &lt;/body&gt;
     *  &lt;/html&gt;&quot; )</code></pre> would be just
     *  &quot;{@code Simple Text}&quot;.<br>
     *  <br>Comments will be stripped as well, and {@code <pre>} tags are not
     *  interpreted, with the consequence that any formatting with whitespace
     *  gets lost. {@code CDATA} elements are stripped, too.
     *
     *  @param  s   The HTML/XML string.
     *  @return The string without the tags.
     *
     *  @since 0.0.7
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String stripTags( final CharSequence s )
    {
        final var retValue = new StringBuilder();
        if( isNotEmptyOrBlank( requireNonNullArgument( s, "s" ) ) )
        {
            final var matcher = m_TagRemovalPattern.matcher( s );
            final var buffer = matcher.replaceAll( " " ).trim().codePoints().toArray();
            int lastChar = NULL_CHAR;
            ScanLoop: for( final var c : buffer )
            {
                if( isWhitespace( c ) )
                {
                    //---* Consecutive whitespace detected *-------------------
                    if( isWhitespace( lastChar ) ) continue ScanLoop;

                    //---* All resulting whitespace have to be blanks *--------
                    retValue.append( " " );
                }
                else
                {
                    //---* Write the character *-------------------------------
                    retValue.append( toChars( c ) );
                }
                lastChar = c;
            }   //  ScanLoop:
        }

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  stripTags()

    /**
     *  Strips characters from the given input that are not allowed (or should
     *  be at least avoided) for a file or folder name on most or all operating
     *  systems.<br>
     *  <br>The following characters will be stripped:
     *  <dl>
     *  <dt><b>:</b> (colon)</dt><dd>On Windows systems it is used to separate
     *  the drive letter from the path and file name; on Unix-like operating
     *  systems it would be valid, but it can cause issues on the {@code PATH}
     *  and {@code CLASSPATH} variables on that operating systems.</dd>
     *  <dt><b>\</b> (backslash)</dt><dd>On Windows systems it is used as the
     *  path separator, while on Unix-like operating systems it is problematic
     *  in other ways. For example, it is used to escape blanks in not-quoted
     *  file or folder names.</dd>
     *  <dt><b>/</b> (slash or forward slash)</dt><dd>The path separator on
     *  Unix-like operating systems, but Java will use it that way on Windows
     *  systems, too.</dd>
     *  <dt><b>;</b> (semicolon)</dt><dd>It can cause issues on the {@code PATH}
     *  and {@code CLASSPATH} variables on Windows.</dd>
     *  <dt><b>*</b> (asterisk)</dt><dd>The asterisk is often used as wild card
     *  character in shell programs to find groups of files; using it in a file
     *  name can cause funny effects.</dd>
     *  <dt><b>?</b> (question mark)</dt><dd>The question mark is used on
     *  Windows as a wild card for a single character; similar to the asterisk,
     *  it can cause funny effects when used in a file name.</dd>
     *  <dt><b>&quot;</b> (double quotes)</dt>
     *  <dt><b>'</b> (single quotes)</dt><dd>Both have some potential to
     *  confuse the various shell programs of all operating systems.</dd>
     *  <dt><b>@</b> ('at'-sign)</dt><dd>Although it is allowed for file and
     *  folder names, it causes issues when used in the URL for that respective
     *  file.</dd>
     *  <dt><b>|</b> (pipe symbol)</dt><dd>Similar to the '*' (asterisk), the
     *  pipe-symbol has – as the name already indicates - a meaning on most
     *  shells that would make it difficult to manage files that contains this
     *  character in their names.</dd>
     *  <dt><b>&lt;</b> (less than)</dt>
     *  <dt><b>&gt;</b> (greater than)</dt><dd>Like the pipe, these two have a
     *  meaning on most shells that would make it difficult to manage files
     *  that contains one of these characters in their names.</dd>
     *  <dt>Whitespace</dt><dd>Only blanks will remain, any other whitespace
     *  characters are stripped.</dd>
     *  </dl>
     *  Finally, the method will strip all leading and trailing blanks;
     *  although blanks are usually allowed, they are confusing when not
     *  surrounded by some visible characters.
     *  Especially regarding the characters that are critical for shells ('*',
     *  '?', '&quot;', ''', '|', '&lt;', and '&gt;') this method is
     *  over-cautious, as most shells could handle them after proper escaping
     *  the offending characters or quoting the file name.<br>
     *  <br>This method furthermore assumes that any other Unicode character is
     *  valid for a file or folder name; unfortunately, there are filesystems
     *  where this is not true.
     *
     *  @note   This method will not take care about the length of the returned
     *      String; this means the result to a call to this method may still be
     *      invalid as a file or folder name because it is too long.
     *
     *  @param  s   The input String, denoting a file or folder name -
     *      <i>not</i> a full path.
     *  @return The String without the characters that are invalid for a file
     *      name. This value will never be {@code null} or empty.
     *  @throws NullArgumentException   The input is {@code null}.
     *  @throws EmptyArgumentException  The input is the empty String.
     *  @throws ValidationException After stripping the invalid characters the
     *      return value would be empty.
     *
     *  @since 0.0.5
     */
    @SuppressWarnings( "SwitchStatementWithTooManyBranches" )
    @API( status = STABLE, since = "0.0.5" )
    public static final String stripToFilename( final CharSequence s ) throws ValidationException
    {
        final var len = requireNotEmptyArgument( s, "s" ).length();
        final var buffer = new StringBuilder( len );
        char c;
        ScanLoop: for( var i = 0; i < len; ++i )
        {
            c = s.charAt( i );
            Selector: switch( c )
            {
                case ':':
                case '\\':
                case '/':
                case ';':
                case '*':
                case '"':
                case '\'':
                case '@':
                case '|':
                case '?':
                case '<':
                case '>':
                    continue ScanLoop;

                default:
                {
                    if( (c == ' ') || (!isISOControl( c ) && !isWhitespace( c )) )
                    {
                        buffer.append( c );
                    }
                    break Selector;
                }
            }   //  Selector:
        }   //  ScanLoop:

        final var retValue = buffer.toString().trim();
        if( retValue.length() < 1 )
        {
            throw new ValidationException( format( "After stripping the invalid characters from '%1$s' there do not remain enough characters for a valid file name", s.toString() ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  stripToFilename()

    /**
     *  Strips HTML or XML comments from the given String.
     *
     *  @param  s   The HTML/XML string.
     *  @return The string without the comments.
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String stripXMLComments( final CharSequence s )
    {
        final var matcher = m_CommentRemovalPattern.matcher( requireNonNullArgument( s, "s" ) );
        final var retValue = matcher.replaceAll( EMPTY_STRING );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  stripXMLComments()

    /**
     *  Translates the given String to a String that will only contain
     *  printable ASCII characters; all other characters will be 'escaped' to
     *  the format &quot;&#92;uXXXX&quot;.
     *
     *  @param  s   The input string; may be {@code null}.
     *  @return The output string; {@code null} if the input string was
     *      already {@code null}.
     *
     *  @since 0.0.5
     *  @deprecated Use instead
     *      {@link CharSetUtils#convertUnicodeToASCII(java.text.Normalizer.Form, CharSequence)}
     *      with
     *      {@link java.text.Normalizer.Form#NFKC}
     *      as the first argument.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String toUnicode( final CharSequence s )
    {
        return CharSetUtils.convertUnicodeToASCII( NFKC, s );
    }   //  toUnicode()

    /**
     *  Changes the first letter of the given String tolower case as per
     *  {@link Character#toLowerCase(char)}.
     *  No other letters are changed. A {@code null} input String returns
     *  {@code null}.<br>
     *  <br>Samples:<pre><code>  StringUtils.uncapitalize( null ) = null;
     *  StringUtils.uncapitalize(&quot;&quot;)     = &quot;&quot;;
     *  StringUtils.uncapitalize(&quot;Cat&quot;)  = &quot;cat&quot;;
     *  StringUtils.uncapitalize(&quot;CAT&quot;)  = &quot;cAT&quot;;</code></pre>
     *  <br>Basically, this is the complementary method to
     *  {@link #capitalize(CharSequence)}.
     *  Use this method to normalise the name of bean attributes.
     *
     *  @param  s   The String to <i>uncapitalise</i>, may be {@code null}.
     *  @return The <i>uncapitalised</i> String, {@code null} if the argument
     *      was {@code null}.
     *  @see #capitalize(CharSequence)
     *
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link #decapitalize(CharSequence)}
     *      instead.
     */
    @Deprecated( since = "0.1.0" )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String uncapitalize( final CharSequence s ) { return decapitalize( s ); }

    /**
     *  Unescapes a string containing entity escapes to a string containing the
     *  actual Unicode characters corresponding to the escapes. Supports HTML
     *  5.0 entities.<br>
     *  <br>For example, the string
     *  &quot;&amp;lt;Fran&amp;ccedil;ais&amp;gt;&quot; will become
     *  &quot;&lt;Fran&ccedil;ais&gt;&quot;.<br>
     *  <br>If an entity is unrecognised, it is left alone, and inserted
     *  verbatim into the result string. e.g. &quot;&amp;gt;&amp;zzzz;x&quot;
     *  will become &quot;&gt;&amp;zzzz;x&quot;.
     *
     *  @param  str The {@code String} to unescape, may be {@code null}.
     *  @return A new unescaped {@code String}, {@code null} if the given
     *      string was already {@code null}.
     *
     *  @see #escapeHTML(CharSequence)
     *  @see #escapeHTML(Appendable,CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String unescapeHTML( final CharSequence str )
    {
        final var retValue = nonNull( str ) ? HTML50.unescape( str ) : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  unescapeHTML()

    /**
     *  Unescapes a string containing entity escapes to a string containing the
     *  actual Unicode characters corresponding to the escapes and writes it to
     *  the given
     *  {@link Appendable}.
     *  Supports HTML 4.0 entities.<br>
     *  <br>For example, the string
     *  &quot;&amp;lt;Fran&amp;ccedil;ais&amp;gt;&quot; will become
     *  &quot;&lt;Fran&ccedil;ais&gt;&quot;.<br>
     *  <br>If an entity is unrecognised, it is left alone, and inserted
     *  verbatim into the result string. e.g. &quot;&amp;gt;&amp;zzzz;x&quot;
     *  will become &quot;&gt;&amp;zzzz;x&quot;.
     *
     *  @param  appendable  The appendable receiving the unescaped string.
     *  @param  str The {@code String} to unescape, may be {@code null}.
     *  @throws NullArgumentException   The appendable is {@code null}.
     *  @throws IOException An IOException occurred.
     *
     *  @see #escapeHTML(CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void unescapeHTML( final Appendable appendable, final CharSequence str ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" );

        if( nonNull( str ) ) HTML50.unescape( appendable, str );
    }   //  unescapeHTML()

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
     *  @since 0.0.5
     *  @deprecated Use
     *      {@link CharSetUtils#unescapeUnicode(CharSequence)}
     *      instead.
     */
    @Deprecated( since ="0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String unescapeUnicode( final CharSequence s )
    {
        return CharSetUtils.unescapeUnicode( s );
    }   //  unescapeUnicode()

    /**
     *  <p>{@summary Unescapes an XML string containing XML entity escapes to a
     *  string containing the actual Unicode characters corresponding to the
     *  escapes.}</p>
     *  <p>If an entity is unrecognised, it is left alone, and inserted
     *  verbatim into the result string. e.g. &quot;&amp;gt;&amp;zzzz;x&quot;
     *  will become &quot;&gt;&amp;zzzz;x&quot;.</p>
     *
     *  @param  str The {@code String} to unescape, may be {@code null}.
     *  @return A new unescaped {@code String}, {@code null} if the given
     *      string was already {@code null}.
     *
     *  @see #escapeXML(CharSequence)
     *  @see #escapeXML(Appendable,CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String unescapeXML( final CharSequence str )
    {
        final var retValue = nonNull( str ) ? XML.unescape( str ) : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  unescapeXML()

    /**
     *  <p>{@summary Unescapes an XML String containing XML entity escapes to a
     *  String containing the actual Unicode characters corresponding to the
     *  escapes and writes it to the given
     *  {@link Appendable}.}</p>
     *  <p>If an entity is unrecognised, it is left alone, and inserted
     *  verbatim into the result string. e.g. &quot;&amp;gt;&amp;zzzz;x&quot;
     *  will become &quot;&gt;&amp;zzzz;x&quot;.</p>
     *
     *  @param  appendable  The appendable receiving the unescaped string.
     *  @param  str The {@code String} to unescape, may be {@code null}.
     *  @throws NullArgumentException   The writer is {@code null}.
     *  @throws IOException An IOException occurred.
     *
     *  @see #escapeXML(CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void unescapeXML( final Appendable appendable, final CharSequence str ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" );

        if( nonNull( str ) ) XML.unescape( appendable, str );
    }   //  unescapeXML()

    /**
     *  Returns the Unicode escape sequence for the given character. This will
     *  return &quot;&#92;u0075&quot; for the letter 'u', and
     *  &quot;&#92;u003c&quot; for the smaller-than sign '&lt;'.
     *
     *  @param  c   The character.
     *  @return The escape sequence.
     *
     *  @deprecated Use
     *      {@link CharSetUtils#escapeCharacter(char)}
     *      instead.
     *
     *  @since 0.0.5
     */
    @API( status = DEPRECATED, since = "0.0.5" )
    @Deprecated( forRemoval = true )
    public static final String unicodeEscape( final char c ) { return escapeCharacter( c ); }

    /**
     *  Returns the Unicode escape sequence for the given code point. This will
     *  return &quot;&#92;u0075&quot; for the letter 'u', and
     *  &quot;&#92;u003c&quot; for the smaller-than sign '&lt;'.
     *
     *  @param  codePoint   The character.
     *  @return The escape sequence.
     *
     *  @deprecated Use
     *      {@link #escapeUnicode(int)}
     *      instead.
     *
     *  @since 0.0.5
     */
    @API( status = DEPRECATED, since = "0.0.5" )
    @Deprecated( forRemoval = true )
    public static final String unicodeEscape( final int codePoint ) { return escapeUnicode( codePoint ); }

    /**
     *  Returns the given URL encoded String in its decoded form, using the
     *  UTF-8 character encoding.<br>
     *  <br>Internally, this method and
     *  {@link #urlEncode(CharSequence)}
     *  make use of the methods from
     *  {@link java.net.URLDecoder}
     *  and
     *  {@link java.net.URLEncoder}, respectively. The methods here were
     *  introduced to simplify the handling, as first only the UTF-8 encoding
     *  should be used - making the second argument for the methods
     *  {@link java.net.URLDecoder#decode(String, String) decode()}/
     *  {@link java.net.URLEncoder#encode(String, String) encode()}
     *  obsolete - and second, they could throw an
     *  {@link UnsupportedEncodingException} - although this should never occur
     *  when UTF-8 encoding is used.
     *
     *  @param  s   The input String.
     *  @return The decoded result.
     *
     *  @see java.net.URLDecoder#decode(String, String)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String urlDecode( final CharSequence s )
    {
        final var retValue = decode( requireNonNullArgument( s, "s" ).toString(), UTF8 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  urlDecode()

    /**
     *  Returns the given String in its URL encoded form, using the
     *  UTF-8 character encoding.
     *
     *  @param  s   The input String.
     *  @return The URL encoded result.
     *
     *  @see java.net.URLEncoder#encode(String, String)
     *  @see #urlDecode(CharSequence)
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String urlEncode( final CharSequence s )
    {
        final var retValue = encode( requireNonNullArgument( s, "s" ).toString(), UTF8 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  urlEncode()
}
//  class StringUtils

/*
 *    End of File
 */