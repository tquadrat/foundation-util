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

import static java.lang.System.arraycopy;
import static java.lang.System.getProperties;
import static java.lang.System.getenv;
import static java.util.regex.Pattern.compile;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.util.SystemUtils.determineIPAddress;
import static org.tquadrat.foundation.util.SystemUtils.getMACAddress;
import static org.tquadrat.foundation.util.SystemUtils.getNodeId;
import static org.tquadrat.foundation.util.SystemUtils.getPID;

import java.io.Serial;
import java.io.Serializable;
import java.net.SocketException;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.MountPoint;
import org.tquadrat.foundation.exception.ImpossibleExceptionError;

/**
 *  <p>{@summary An instance of this class is basically a wrapper around a
 *  String that contains placeholders (&quot;Variables&quot;) in the form
 *  <code>${&lt;<i>name</i>&gt;}</code>, where &lt;<i>name</i> is the variable
 *  name.}</p>
 *  <p>The variables names are case-sensitive.</p>
 *  <p>Valid variable names may not contain other characters than the letters
 *  from 'a' to 'z' (upper case and lower case), the digits from '0' to '9' and
 *  the special characters underscore ('_') and dot ('.'), after an optional
 *  prefix character.</p>
 *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal sign
 *  ('='), the colon (':'), the percent sign ('%'), and the ampersand
 *  ('&amp;').</p>
 *  <p>The prefix character is part of the name.</p>
 *  <p>Finally, there is the single underscore that is allowed as a special
 *  variable.</p>
 *  <p>When the system data is added as source (see
 *  {@link #replaceVariableFromSystemData(CharSequence, Map[]) replaceVariableFromSystemData()}
 *  and
 *  {@link #replaceVariable(boolean, Map[]) replaceVariable()}
 *  with {@code addSystemData} set to {@code true}), some additional variables
 *  are available:</p>
 *  <dl>
 *      <dt>{@value #VARNAME_IPAddress}</dt>
 *      <dd>One of the outbound IP addresses of the machine that executes the
 *      current program, if network is configured at all.</dd>
 *      <dt>{@value #VARNAME_MACAddress}</dt>
 *      <dd>The MAC address of the machine that executes the current program;
 *      if no network is configured, a dummy address is used.</dd>
 *      <dt>{@value #VARNAME_NodeId}</dt>
 *      <dd>The node id of the machines that executes the current program; if
 *      no network is configured, a pseudo node id is used.</dd>
 *      <dt>{@value #VARNAME_Now}</dt>
 *      <dd>The current data and time in UTC time zone.</dd>
 *      <dt>{@value #VARNAME_pid}</dt>
 *      <dd>The process id of the current program.</dd>
 *  </dl>
 *
 *  @see #VARNAME_IPAddress
 *  @see #VARNAME_MACAddress
 *  @see #VARNAME_NodeId
 *  @see #VARNAME_Now
 *  @see #VARNAME_pid
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Template.java 1015 2022-02-09 08:25:36Z tquadrat $
 *
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: Template.java 1015 2022-02-09 08:25:36Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class Template implements Serializable
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The variable name for the IP address of the executing machine:
     *  {@value}.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARNAME_IPAddress = "org.tquadrat.ipaddress";

    /**
     *  The variable name for the MAC address of the executing machine:
     *  {@value}.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARNAME_MACAddress = "org.tquadrat.macaddress";

    /**
     *  The variable name for the node id of the executing machine: {@value}.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARNAME_NodeId = "org.tquadrat.nodeid";

    /**
     *  The variable name for the current date and time: {@value}.
     *
     * @see Instant#now()
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARNAME_Now = "org.tquadrat.now";

    /**
     *  The variable name for the id of the current process, executing this
     *  program: {@value}.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARNAME_pid = "org.tquadrat.pid";

    /**
     *  The regular expression to identify a variable in a char sequence:
     *  {@value}.
     *
     *  @see #findVariables(CharSequence)
     *  @see #findVariables()
     *  @see #isValidVariableName(CharSequence)
     *  @see #replaceVariable(CharSequence,Map...)
     *  @see #replaceVariable(Map...)
     *  @see #replaceVariable(CharSequence, Function)
     *  @see #replaceVariable(Function)
     *
     *  @since 0.1.0
     */
    @SuppressWarnings( "RegExpUnnecessaryNonCapturingGroup" )
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARIABLE_PATTERN = "\\$\\{((?:_)|(?:[~/=%:&]?\\p{IsAlphabetic}(?:\\p{IsAlphabetic}|\\p{Digit}|_|.)*?))}";

    /**
     *  <p>{@summary The template for variables: {@value}.} The argument is the
     *  name of the variable itself; after an optional prefix character, it may
     *  not contain other characters than the letters from 'a' to 'z' (upper
     *  case and lower case), the digits from '0' to '9' and the special
     *  characters underscore ('_') and dot ('.').</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>The prefix character is part of the name.</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String VARIABLE_TEMPLATE = "${%1$s}";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The template text.
     */
    private final String m_TemplateText;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The pattern that is used to identify a variable in a char sequence.
     *
     *  @see #replaceVariable(CharSequence, Map...)
     *  @see #replaceVariable(Map...)
     *  @see #findVariables(CharSequence)
     *  @see #findVariables()
     *  @see #VARIABLE_PATTERN
     */
    private static final Pattern m_VariablePattern;

    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

    static
    {
        //---* The regex patterns *--------------------------------------------
        try
        {
            m_VariablePattern = compile( VARIABLE_PATTERN );
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
     *  Creates a new instance of {@code Template}.
     *
     *  @param  templateText    The template text, containing variable in the
     *      form <code>${&lt;<i>name</i>&gt;}</code>.
     */
    public Template( final CharSequence templateText )
    {
        m_TemplateText = requireNonNullArgument( templateText, "templateText" ).toString();
    }   //  Template()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary The mount point for template manipulations in derived
     *  classes.}</p>
     *  <p>The default implementation will just return the argument.</p>
     *
     *  @param  templateText    The template text, as it was given to the
     *      constructor on creation of the object instance.
     *  @return The adjusted template text.
     */
    @SuppressWarnings( "static-method" )
    @MountPoint
    protected String adjustTemplate( final String templateText )
    {
        return templateText;
    }   //  adjustTemplate()

    /**
     *  Builds the source map with the additional data.
     *
     *  @return The source map.
     *
     *  @see #VARNAME_IPAddress
     *  @see #VARNAME_MACAddress
     *  @see #VARNAME_NodeId
     *  @see #VARNAME_Now
     *  @see #VARNAME_pid
     */
    private static final Map<String,Object> createAdditionalSource()
    {
        final Map<String,Object> retValue = new HashMap<>(
            Map.of( VARNAME_MACAddress, getMACAddress(), VARNAME_pid, Long.valueOf( getPID() ), VARNAME_NodeId, Long.valueOf( getNodeId() ), VARNAME_Now, Instant.now() )
        );
        try
        {
            determineIPAddress().ifPresent( i -> retValue.put( VARNAME_IPAddress, i ) );
        }
        catch( @SuppressWarnings( "unused" ) final SocketException e ) { /* Deliberately ignored */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createAdditionalSource()

    /**
     *  Escapes backslash ('\') and dollar sign ('$') for regex replacements.
     *
     *  @param  s   The source string.
     *  @return The string with the escaped characters.
     *
     *  @see java.util.regex.Matcher#appendReplacement(StringBuffer,String)
     *
     *  @since 0.1.0
     */
    @API( status = INTERNAL, since = "0.1.0" )
    private static String escapeRegexReplacement( final CharSequence s )
    {
        assert nonNull( s ) : "s is null";

        //---* Escape the backslashes and dollar signs *-------------------
        final var len = s.length();
        final var retValue = new StringBuilder( (len * 12) / 10 );
        @SuppressWarnings( "LocalVariableNamingConvention" )
        char c;
        EscapeLoop: for( var i = 0; i < len; ++i )
        {
            c = s.charAt( i );
            switch( c )
            {
                case '\\':
                case '$':
                    retValue.append( '\\' ); // The fall through is intended here!
                    //$FALL-THROUGH$
                default: // Do nothing ...
            }
            retValue.append( c );
        }   //  EscapeLoop:

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  escapeRegexReplacement()

    /**
     *  <p>{@summary Collects all the variables of the form
     *  <code>${<i>&lt;name&gt;</i>}</code> in the given String.}</p>
     *  <p>If there are not any variables in the given String, an empty
     *  {@link Set}
     *  will be returned.</p>
     *  <p>A valid variable name may not contain any other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @return A {@code Collection} with the variable (names).
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.0.5
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Set<String> findVariables( final CharSequence text )
    {
        final Collection<String> buffer = new HashSet<>();
        if( nonNull( text ) )
        {
            final var matcher = m_VariablePattern.matcher( text );
            String found;
            while( matcher.find() )
            {
                found = matcher.group( 1 );
                buffer.add( found );
            }
        }
        final var retValue = Set.copyOf( buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findVariables()

    /**
     *  <p>{@summary Collects all the variables of the form
     *  <code>${<i>&lt;name&gt;</i>}</code> in the adjusted template.}</p>
     *  <p>If there are not any variables in there, an empty
     *  {@link Collection}
     *  will be returned.</p>
     *  <p>A valid variable name may not contain any other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @return A {@code Collection} with the variable (names).
     *
     *  @see #VARIABLE_PATTERN
     */
    public final Set<String> findVariables()
    {
        final var retValue = findVariables( getTemplateText() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findVariables()

    /**
     *  <p>{@summary Mountpoint for the formatting of the result after the variables have
     *  been replaced.}</p>
     *  <p>The default implementation just returns the result.</p>
     *
     *  @param  text    The result from replacing the variables in the template
     *      text.
     *  @return The reformatted result.
     */
    @SuppressWarnings( "static-method" )
    @MountPoint
    protected String formatResult( final String text )
    {
        return text;
    }   //  formatResult()

    /**
     *  Returns the template text after it has been processed by
     *  {@link #adjustTemplate(String)}.
     *
     *  @return The adjusted template text.
     */
    protected final String getTemplateText() { return adjustTemplate( m_TemplateText ); }

    /**
     *  Checks whether the adjusted template contains the variable of
     *  the form <code>${<i>&lt;name&gt;</i>}</code> (matching the pattern
     *  given in
     *  {@link #VARIABLE_PATTERN})
     *  with the given name.
     *
     *  @param  name    The name of the variable to look for.
     *  @return {@code true} if the template contains the variable,
     *      {@code false} otherwise.
     *  @throws IllegalArgumentException    The given argument is not valid as
     *      a variable name.
     *
     *  @see #VARIABLE_PATTERN
     */
    public final boolean hasVariable( final String name )
    {
        if( !isValidVariableName( name ) ) throw new IllegalArgumentException( format( "%s is not a valid variable name", name ) );

        final var retValue = findVariables().contains( name );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasVariable()

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
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final boolean hasVariables( final CharSequence s )
    {
        final var retValue = isNotEmptyOrBlank( s ) && m_VariablePattern.matcher( s ).find();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasVariables()

    /**
     *  Checks whether the adjusted template contains at least one variable of
     *  the form <code>${<i>&lt;name&gt;</i>}</code> (matching the pattern
     *  given in
     *  {@link #VARIABLE_PATTERN}).
     *
     *  @return {@code true} if the template contains at least one variable,
     *      {@code false} otherwise.
     *
     *  @see #VARIABLE_PATTERN
     */
    public final boolean hasVariables() { return hasVariables( getTemplateText() ); }

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
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final boolean isValidVariableName( final CharSequence name )
    {
        var retValue = isNotEmptyOrBlank( requireNonNullArgument( name, "name" ) );
        if( retValue )
        {
            final var text = format( VARIABLE_TEMPLATE, name );
            retValue = m_VariablePattern.matcher( text ).matches();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
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
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final boolean isVariable( final CharSequence s )
    {
        final var retValue = isNotEmptyOrBlank( s ) && m_VariablePattern.matcher( s ).matches();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isVariable()

    /**
     *  <p>{@summary Replaces the variables of the form
     *  <code>${&lt;<i>name</i>&gt;}</code> in the given String with values
     *  from the given maps.} The method will try the maps in the given
     *  sequence, it stops after the first match.</p>
     *  <p>If no replacement value could be found, the variable will not be
     *  replaced at all.</p>
     *  <p>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.</p>
     *  <p>The variables names are case-sensitive.</p>
     *  <p>Valid variable names may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>The prefix character is part of the name.</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @param  sources The maps with the replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.1.0
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.1.0" )
    public static final String replaceVariable( final CharSequence text, final Map<String,? extends Object>... sources )
    {
        requireNonNullArgument( sources, "sources" );

        final var retValue = replaceVariable( text, variable -> retrieveVariableValue( variable, sources ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  <p>{@summary Replaces the variables of the form
     *  <code>${&lt;<i>name</i>&gt;}</code> in the adjusted template with
     *  values from the given maps and returns it after formatting the result.}
     *  The method will try the maps in the given sequence, it stops after the
     *  first match.</p>
     *  <p>If no replacement value could be found, the variable will not be
     *  replaced at all.</p>
     *  <p>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.</p>
     *  <p>The variables names are case-sensitive.</p>
     *  <p>Valid variable names may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>The prefix character is part of the name.</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @param  sources The maps with the replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     */
    @SafeVarargs
    public final String replaceVariable( final Map<String,? extends Object>... sources )
    {
        return replaceVariable( false, sources );
    }   //  replaceVariable()

    /**
     *  <p>{@summary Replaces the variables of the form
     *  <code>${&lt;<i>name</i>&gt;}</code> in the adjusted template with
     *  values from the given maps and returns it after formatting the result.}
     *  The method will try the maps in the given sequence, it stops after the
     *  first match.</p>
     *  <p>If {@code addSystemData} is provided as {@code true}, the
     *  {@linkplain System#getProperties() system properties}
     *  and
     *  {@linkplain System#getenv()} system environment}
     *  will be searched for replacement values before any other source.</p>
     *  <p>If no replacement value could be found, the variable will not be
     *  replaced at all.</p>
     *  <p>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.</p>
     *  <p>The variables names are case-sensitive.</p>
     *  <p>Valid variable names may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>The prefix character is part of the name.</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @param  addSystemData   {@code true} if the system properties and the
     *      system environment should be searched for replacement values, too,
     *      otherwise {@code false}.
     *  @param  sources The maps with the replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *  @see #replaceVariableFromSystemData(CharSequence, Map[])
     */
    @SafeVarargs
    public final String replaceVariable( final boolean addSystemData, final Map<String,? extends Object>... sources )
    {
        final var rawTemplate = getTemplateText();
        final var processedText = addSystemData
            ? replaceVariableFromSystemData( rawTemplate, sources )
            : replaceVariable( rawTemplate, sources );
        final var retValue = formatResult( processedText );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  <p>{@summary Replaces the variables of the form
     *  <code>${&lt;<i>name</i>&gt;}</code> in the adjusted template with
     *  values returned by the given retriever function for the variable name,
     *  and returns it after formatting the result.}</p>
     *  <p>If no replacement value could be found, the variable will not be
     *  replaced at all.</p>
     *  <p>If the retriever function returns a value that contains a variable
     *  itself, this will not be replaced.</p>
     *  <p>The retriever function will be called only once for each variable
     *  name; if the text contains the same variable multiple times, it will
     *  always be replaced with the same value.</p>
     *  <p>The variables names are case-sensitive.</p>
     *  <p>Valid variable names may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>The prefix character is part of the name.</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @param  retriever   The function that will retrieve the replacement
     *      values for the given variable names.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     */
    public final String replaceVariable( final Function<? super String, Optional<String>> retriever )
    {
        final var retValue = formatResult( replaceVariable( getTemplateText(), retriever ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  <p>{@summary Replaces the variables of the form
     *  <code>${&lt;<i>name</i>&gt;}</code> in the given String with values
     *  returned by the given retriever function for the variable name.}</p>
     *  <p>If no replacement value could be found, the variable will not be
     *  replaced at all.</p>
     *  <p>If the retriever function returns a value that contains a variable
     *  itself, this will not be replaced.</p>
     *  <p>The retriever function will be called only once for each variable
     *  name; if the text contains the same variable multiple times, it will
     *  always be replaced with the same value.</p>
     *  <p>The variables names are case-sensitive.</p>
     *  <p>Valid variable name may not contain other characters than the
     *  letters from 'a' to 'z' (upper case and lower case), the digits from
     *  '0' to '9' and the special characters underscore ('_') and dot ('.'),
     *  after an optional prefix character.</p>
     *  <p>Allowed prefixes are the tilde ('~'), the slash ('/'), the equal
     *  sign ('='), the colon (':'), the percent sign ('%'), and the ampersand
     *  ('&amp;').</p>
     *  <p>The prefix character is part of the name.</p>
     *  <p>Finally, there is the single underscore that is allowed as a
     *  special variable.</p>
     *
     *  @param  text    The text with the variables; may be {@code null}.
     *  @param  retriever   The function that will retrieve the replacement
     *      values for the given variable names.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String replaceVariable( final CharSequence text, final Function<? super String, Optional<String>> retriever )
    {
        requireNonNullArgument( retriever, "retriever" );

        final Map<String,String> cache = new HashMap<>();

        String retValue = null;
        if( nonNull( text ) )
        {
            final var matcher = m_VariablePattern.matcher( text );
            final var buffer = new StringBuilder();
            while( matcher.find() )
            {
                final var variable = matcher.group( 0 );
                final var replacement = cache.computeIfAbsent( variable, v -> escapeRegexReplacement( retriever.apply( matcher.group( 1 ) ).orElse( v ) ) );
                matcher.appendReplacement( buffer, replacement );
            }
            matcher.appendTail( buffer );
            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariable()

    /**
     *  <p>{@summary Replaces the variables of the form
     *  <code>${<i>&lt;name&gt;</i>}</code> in the given String with values
     *  from the
     *  {@linkplain System#getProperties() system properties},
     *  the
     *  {@linkplain System#getenv() system environment}
     *  and the given maps.} The method will try the maps in the given
     *  sequence, it stops after the first match.</p>
     *  <p>In addition, five more variables are recognised:</p>
     *  <dl>
     *      <dt><b><code>{@value #VARNAME_IPAddress}</code></b></dt>
     *      <dd>The first IP address for the machine that executes this Java
     *      virtual machine.</dd>
     *      <dt><b><code>{@value #VARNAME_MACAddress}</code></b></dt>
     *      <dd>The MAC address of the first NIC in this machine.</dd>
     *      <dt><b><code>{@value #VARNAME_NodeId}</code></b></dt>
     *      <dd>The node id from the first NIC in this machine.</dd>
     *      <dt><b><code>{@value #VARNAME_Now}</code></b></dt>
     *      <dd>The current date and time as returned by
     *      {@link Instant#now}.</dd>
     *      <dt><b><code>{@value #VARNAME_pid}</code></b></dt>
     *      <dd>The process id of this Java virtual machine.</dd>
     *  </dl>
     *  <p>If no replacement value could be found, the variable will not be
     *  replaced at all; no exception will be thrown.</p>
     *  <p>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.</p>
     *  <p>The variables names are case-sensitive.</p>
     *
     *  @param  text    The text with the variables; can be {@code null}.
     *  @param  additionalSources The maps with additional replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see #VARIABLE_PATTERN
     *  @see #replaceVariable(CharSequence, Map...)
     */
    @SafeVarargs
    @API( status = STABLE, since = "0.1.0" )
    public static final String replaceVariableFromSystemData( final CharSequence text, final Map<String,? extends Object>... additionalSources )
    {
        final var currentLen = requireNonNullArgument( additionalSources, "additionalSources" ).length;
        final var newLen = currentLen + 3;
        @SuppressWarnings( "unchecked" )
        final Map<String,? extends Object> [] sources =  new Map [newLen];
        if( currentLen > 0 )
        {
            arraycopy( additionalSources, 0, sources, 3, currentLen );
        }

        @SuppressWarnings( {"unchecked", "rawtypes"} )
        final Map<String,? extends Object> systemProperties = (Map) getProperties();

        sources [0] = createAdditionalSource();
        sources [1] = systemProperties;
        sources [2] = getenv();

        final var retValue = replaceVariable( text, sources );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceVariableFromSystemData()

    /**
     *  Tries to obtain a value for the given key from one of the given
     *  sources that will be searched in the given sequence order.
     *
     *  @param  name    The name of the value.
     *  @param  sources The maps with the values.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the value from one of the sources.
     */
    @SafeVarargs
    private static final Optional<String> retrieveVariableValue( final String name, final Map<String,? extends Object>... sources )
    {
        assert nonNull( name ) : "name is null";
        assert nonNull( sources ) : "sources is null";

        Optional<String> retValue = Optional.empty();

        //---* Search the sources *--------------------------------------------
        Object value = null;
        SearchLoop: for( final var map : sources )
        {
            value = map.get( name );
            if( nonNull( value ) ) break SearchLoop;
        }   //  SearchLoop:

        if( nonNull( value ) )
        {
            //---* Escape the backslashes and dollar signs *-------------------
            retValue = Optional.of( value.toString() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveVariableValue()
}
//  class Template

/*
 *  End of File
 */