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

package org.tquadrat.foundation.util.internal;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.builder;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.UnexpectedExceptionError;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Provides HTML and XML entity utilities.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Alexander Day Chaffee - alex@purpletech.com
 *  @thanks Gary Gregory - ggregory@seagullsw.com
 *  @inspired Some code I found somewhere long time ago, originally written by
 *      Alexander Day Chaffee and Gary Gregory
 *  @version $Id: Entities.java 966 2022-01-04 22:28:49Z tquadrat $
 *  @since 0.0.5
 *
 *  @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
 *  @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
 *  @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
 *  @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
 *  @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
 *  @see <a href="https://www.quackit.com/character_sets/html5_entities/html5_entities_all.cfm">HTML5 Entities in Alphabetical Order - Complete List</a>
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Entities.java 966 2022-01-04 22:28:49Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class Entities
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  Local interface for the data structure that is used to store the
     *  entity mappings.
     *
     *  @extauthor Alexander Day Chaffee - alex@purpletech.com
     *  @extauthor Gary Gregory - ggregory@seagullsw.com
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: Entities.java 966 2022-01-04 22:28:49Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: Entities.java 966 2022-01-04 22:28:49Z tquadrat $" )
    private static interface EntityMap
    {
            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Adds an entry to this entity map.<br>
         *  <br>If the value is negative, only the name to value relation will
         *  be stored.
         *
         *  @param  name    The entity name.
         *  @param  value   The entity value (the Unicode code).
         */
        public void add( String name, Integer value );

        /**
         *  Returns the entities.
         *
         *  @return The entities.
         */
        public Stream<String> list();

        /**
         *  Returns the name of the entity identified by the specified value.
         *
         *  @param  value   The value to locate.
         *  @return An instance of
         *      {@link Optional}
         *      that holds the entity name that is associated with the
         *      specified value.
         */
        public Optional<String> name( int value );

        /**
         *  Returns the value of the entity identified by the specified name.
         *
         *  @param  name    The name of the entity to locate
         *  @return An instance of
         *      {@link Optional}
         *      that holds the entity value associated with the specified name.
         */
        public Optional<Integer> value( String name );
    }
    //  interface EntityMap

    /**
     *  A simple implementation for the interface
     *  {@link EntityMap}.
     *
     *  @extauthor Alexander Day Chaffee - alex@purpletech.com
     *  @extauthor Gary Gregory - ggregory@seagullsw.com
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: Entities.java 966 2022-01-04 22:28:49Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: Entities.java 966 2022-01-04 22:28:49Z tquadrat $" )
    private static class PrimitiveEntityMap implements EntityMap
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The map that holds the references from the name to the value.
         */
        private final Map<String,Integer> m_NameToValue = new TreeMap<>();

        /**
         *  The map that holds the references from the value to the name.
         */
        private final Map<Integer,String> m_ValueToName = new TreeMap<>();

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new primitive entity map.
         */
        public PrimitiveEntityMap() { /* Does nothing but exist */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         * {@inheritDoc}
         */
        @Override
        public final void add( final String name, final Integer value )
        {
            assert isNotEmptyOrBlank( name ) : "name is empty or null";
            assert nonNull( value ) : "value is null";
            assert value.intValue() != 0 : "value is 0";

            if( value.intValue() > 0 )
            {
                m_NameToValue.put( name, value );
                final var previousName = m_ValueToName.put( value, name );
                assert isNull( previousName ) : format( "Duplicate: %s, %s, %d", name, previousName, value );
            }
            else
            {
                m_NameToValue.put( name, Integer.valueOf( -value.intValue() ) );
            }
        }   //  add()

        /**
         * {@inheritDoc}
         */
        @Override
        public final Stream<String> list() { return m_NameToValue.keySet().stream(); }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Optional<String> name( final int value ) { return Optional.ofNullable( m_ValueToName.get( value ) ); }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Optional<Integer> value( final String name )
        {
            final var retValue = Optional.ofNullable( m_NameToValue.get( requireNotEmptyArgument( name, "name" ) ) );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  value()
    }
    //  class PrimitiveEntityMap

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name for the resource file that holds the additional XML entities:
     *  {@value}. These entities are not defined for HTML before HTML&nbsp;5.
     */
    public static final String ADDITIONAL_XML_ENTITIES = "apos_entities.data";

    /**
     *  The name for the resource file that holds the basic entities that are
     *  common for both XML and HTML: {@value}.
     */
    public static final String BASIC_ENTITIES = "basic_entities.data";

    /**
     *  The name for the resource final that holds the entities that were
     *  introduced for HTML&nbsp;3.2: {@value}.
     */
    public static final String HTML32_ENTITIES = "ISO8859_1_entities.data";

    /**
     *  The name for the resource final that holds the entities that were
     *  introduced for HTML&nbsp;4.0: {@value}.
     */
    public static final String HTML40_ENTITIES = "html40_entities.data";

    /**
     *  The name for the resource final that holds the entities that were
     *  introduced for HTML&nbsp;5.0: {@value}.
     */
    public static final String HTML50_ENTITIES = "html50_entities.data";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The entity mapping.
     */
    private final Lazy<EntityMap> m_EntityMap;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The set of basic entities.
     */
    public static final Entities BASIC;

    /**
     *  The set of entities supported by HTML 3.2.
     */
    public static final Entities HTML32;

    /**
     *  The set of entities supported by HTML 4.0.
     */
    public static final Entities HTML40;

    /**
     *  The set of entities supported by HTML 5.0.
     */
    public static final Entities HTML50;

    /**
     *  The set of entities supported by standard XML.
     */
    public static final Entities XML;

    static
    {
        //---* Set the BASIC entities *----------------------------------------
        BASIC = new Entities( BASIC_ENTITIES );

        //---* Set the HTML 3.2 entities *-------------------------------------
        HTML32 = new Entities( BASIC_ENTITIES, HTML32_ENTITIES );

        //---* Set the HTML 4.0 entities *-------------------------------------
        HTML40 = new Entities( BASIC_ENTITIES, HTML32_ENTITIES, HTML40_ENTITIES );

        //---* Set the HTML 5.0 entities *-------------------------------------
        HTML50 = new Entities( BASIC_ENTITIES, HTML32_ENTITIES, HTML40_ENTITIES, HTML50_ENTITIES );

        //---* Set the HTML XML entities *-------------------------------------
        XML = new Entities( BASIC_ENTITIES, ADDITIONAL_XML_ENTITIES );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Entities} instance.
     *
     *  @param  resourceNames   The names of the resource files with the entity
     *      definitions.
     */
    private Entities( final String... resourceNames )
    {
        final var thisClass = getClass();
        final var packageName = thisClass.getPackageName().replace( '.', '/' );
        final var resourceURLs = stream( resourceNames )
            .map( n ->
            {
                final var resourceName = format( "/%s/%s", packageName, n );
                final var resourceURL = thisClass.getResource( resourceName );
                assert nonNull( resourceURL ) : format( "URL is null for %s", resourceName );
                return resourceURL;
            } )
            .toArray( URL []::new );

        final var supplier = (Supplier<EntityMap>) () ->
        {
            final EntityMap map = new PrimitiveEntityMap();
            for( final var resourceURL : resourceURLs )
            {
                loadEntities( map, resourceURL );
            }
            return map;
        };

        m_EntityMap = Lazy.use( supplier );
    }   //  Entities()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Underlying unescape method that allows the optimisation of not starting
     *  from the 0 index again.
     *
     *  @param  buffer  The buffer to write the results to.
     *  @param  s   The source {@code String} to unescape.
     *  @param  firstAmp    The index of the first ampersand in the source.
     *  @throws IOException Problems on writing to the {@code buffer}.
     */
    @SuppressWarnings( "MagicNumber" )
    private void doUnescape( final Appendable buffer, final CharSequence s, final int firstAmp ) throws IOException
    {
        assert nonNull( buffer ) : "buffer is null";
        assert nonNull( s ) : "s is null";
        assert firstAmp >= 0 : "firstAmp is less than 0";

        buffer.append( s, 0, firstAmp );
        final var str = s.toString();
        final var len = str.length();
        char c, isHexChar;
        int nextIndex, semiColonIndex, ampersandIndex, entityContentLen;
        Optional<Integer> entityValue;
        ScanLoop: for( var i = firstAmp; i < len; ++i )
        {
            c = str.charAt( i );
            if( c == '&' )
            {
                nextIndex = i + 1;
                semiColonIndex = str.indexOf( ';', nextIndex );
                if( semiColonIndex == -1 )
                {
                    buffer.append( c );
                    continue ScanLoop;
                }
                ampersandIndex = str.indexOf( '&', i + 1 );
                if( (ampersandIndex != -1) && (ampersandIndex < semiColonIndex) )
                {
                    //---* The text looks like "&...&...;" *-------------------
                    buffer.append( c );
                    continue ScanLoop;
                }
                final var entityContent = str.substring( nextIndex, semiColonIndex );
                entityValue = Optional.empty();
                entityContentLen = entityContent.length();
                if( entityContentLen > 0 )
                {
                    if( entityContent.charAt( 0 ) == '#' )
                    {
                        /*
                         * Escaped value content is an integer (decimal or
                         * hexadecimal)
                         */
                        if( entityContentLen > 1 )
                        {
                            isHexChar = entityContent.charAt( 1 );
                            try
                            {
                                final var value = switch( isHexChar )
                                {
                                    case 'X', 'x' -> Integer.parseInt( entityContent.substring( 2 ), 0x10 );
                                    default -> Integer.parseInt( entityContent.substring( 1 ), 10 );
                                };
                                entityValue = value > 0xFFFFFF ? Optional.empty() : Optional.of( Integer.valueOf( value ) );
                            }
                            catch( @SuppressWarnings( "unused" ) final NumberFormatException e )
                            {
                                entityValue = Optional.empty();
                            }
                        }
                    }
                    else
                    {
                        //---* Escaped value content is an entity name *-------
                        entityValue = entityValue( entityContent );
                    }
                }
                buffer.append( entityValue.map( v -> Character.toString( v.intValue() ) ).orElseGet( () -> format( "&%s;", entityContent ) ) );

                //---* Move  the index up to the semi-colon *------------------
                //noinspection AssignmentToForLoopParameter
                i = semiColonIndex;
            }
            else
            {
                buffer.append( c );
            }
        }   //  ScanLoop:
    }   //  doUnescape()

    /**
     *  Returns the name of the entity identified by the specified value.
     *
     *  @param  value   The value to locate.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the entity name that is associated with the specified
     *      value.
     */
    public final Optional<String> entityName( final int value ) { return m_EntityMap.get().name( value ); }

    /**
     *  Returns the value of the entity identified by the specified name.
     *
     *  @param  name    The name to locate.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the entity value associated with the specified name.
     */
    public final Optional<Integer> entityValue( final String name ) { return m_EntityMap.get().value( name ); }

    /**
     *  Escapes the characters in a {@code String}.<br>
     *  <br>For example, if you have called
     *  {@code addEntity( "foo", "0xA1" )}, a call to
     *  {@code escape( "\u00A1" )} will return {@code "&foo;"}.
     *
     *  @param  str The {@code String} to escape.
     *  @return A new escaped {@code String}.
     */
    public final String escape( final CharSequence str )
    {
        final var retValue = requireNonNullArgument( str, "str" ).codePoints()
            .mapToObj( c -> entityName( c ).map( n -> format( "&%s;", n ) ).orElseGet( () -> c > 0x7F ? formatCodePoint( c ) : Character.toString( (char) c ) ) )
            .collect( joining() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  escape()

    /**
     *  Escapes the characters in the {@code String} passed and writes the
     *  result to the
     *  {@link Appendable}
     *  passed.
     *
     *  @param  appendable  The {@code Appendable} to write the results of the
     *      escaping to.
     *  @param  str The {@code String} to escape.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(char)}
     *      method.
     *  @see #escape(CharSequence)
     */
    public final void escape( final Appendable appendable, final CharSequence str ) throws IOException
    {
        requireNonNullArgument( appendable, "appendable" ).append( escape( requireNonNullArgument( str, "str" ) ) );
    }   //  escape()

    /**
     *  Converts a code point into the numerical HTML escape format.
     *
     *  @param  codePoint   The code point.
     *  @return The HTML escaped code point.
     */
    private static final String formatCodePoint( final int codePoint )
    {
        final Builder<String> builder = builder();
        for( final var c : Character.toChars( codePoint ) )
        {
            builder.add( format( "&#x%X;", (int) c ) );
        }
        final var retValue = builder.build().collect( joining() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  formatCodePoint()

    /**
     *  Returns a list of all known entities.
     *
     *  @return An array of String with the entities, there numerical values
     *      and the Unicode name of the entity.
     */
    public final String [] listEntities()
    {
        final var retValue = m_EntityMap.get().list()
            .sorted()
            .map( e ->
            {
                /*
                 * For all existing entities, entityValue() will return a
                 * value, so that the check on presence is obsolete.
                 */
                @SuppressWarnings( "OptionalGetWithoutIsPresent" )
                final var value = entityValue( e ).get().intValue();
                final var unicode = Character.getName( value );
                return format( "&%1$s; = &#%2$d = &#%2$X%3$s", e, value, StringUtils.isEmptyOrBlank( unicode ) ? "" : format( " (%s)", unicode ) );
            } )
            .toArray( String []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  listEntities()

    /**
     *  Load the entities from the resource identified by the given URL to the
     *  given target entity map.
     *
     *  @param  entityMap   The map that is the target for the entities.
     *  @param  resourceURL The URL for the resource.
     */
    @SuppressWarnings( "ProhibitedExceptionThrown" )
    private static void loadEntities( final EntityMap entityMap, final URL resourceURL )
    {
        assert nonNull( entityMap ) : "entityMap is null";
        assert nonNull( resourceURL ) : "resourceURL is null";

        try( final var reader = new BufferedReader( new InputStreamReader( resourceURL.openStream() ) ) )
        {
            reader.lines()
                .filter( StringUtils::isNotEmptyOrBlank )
                .filter( l -> !l.startsWith( "#" ) )
                .forEach( l -> parseAndAdd( entityMap, l ) );
        }
        catch( final IOException e )
        {
            throw new Error( "Failed to read resource " + resourceURL, e );
        }
    }   //  loadEntities()

    /**
     *  Parses the given input line for an entity name and the related code
     *  point, and adds both to the given entity map.
     *
     *  @param  entityMap   The map that is the target for the entities.
     *  @param  inputLine   The input line.
     */
    @SuppressWarnings( "ProhibitedExceptionThrown" )
    private static void parseAndAdd( final EntityMap entityMap, final String inputLine )
    {
        //---* Strip the comment *---------------------------------------------
        var pos = inputLine.indexOf( "#" );
        final var data = (pos < 0 ? inputLine : inputLine.substring( 0, pos )).trim();

        //---* Split the data *------------------------------------------------
        pos = data.indexOf( "=" );
        if( pos < 0 ) throw new Error( "Invalid input data: " + inputLine );
        final var entityName = data.substring( 0, pos ).trim();
        final var value = data.substring( pos + 1 ).trim();
        try
        {
            final var codePoint = Integer.valueOf( value );
            entityMap.add( entityName, codePoint );
        }
        catch( final NumberFormatException e )
        {
            throw new Error( "Invalid input data: " + inputLine, e );
        }
    }   //  parseAndAdd()

    /**
     *  Unescapes the entities in a {@code String}.<br>
     *  <br>For example, if you have called {@code addEntity( "foo", 0xA1 )},
     *  a call to {@code unescape( "&foo;")} will return {@code "\u00A1"}.
     *
     *  @param  str The {@code String} to escape.
     *  @return A new escaped {@code String}.
     */
    public final String unescape( final CharSequence str )
    {
        var retValue = requireNonNullArgument( str, "str" ).toString();
        final var firstAmp = retValue.indexOf( '&' );
        if( firstAmp >= 0 )
        {
            final var buffer = new StringBuilder( str.length() * 2 );
            try
            {
                doUnescape( buffer, str, firstAmp );
            }
            catch( final IOException e )
            {
                /*
                 * Operations on a StringBuilder should not cause an
                 * IOException.
                 */
                throw new UnexpectedExceptionError( e );
            }
            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  unescape()

    /**
     *  Unescapes the escaped entities in the {@code String} passed and writes
     *  the result to the
     *  {@link Appendable}
     *  passed.
     *
     *  @param  appendable  The {@code Appendable} to write the results to.
     *  @param  str The source {@code String} to unescape.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(char)}
     *      method.
     *  @see #unescape(CharSequence)
     */
    public final void unescape( final Appendable appendable, final CharSequence str ) throws IOException
    {
        final var firstAmp = requireNonNullArgument( str, "str" ).toString().indexOf( "&" );
        if( firstAmp >= 0 )
        {
            doUnescape( requireNonNullArgument( appendable, "appendable" ), str, firstAmp );
        }
        else
        {
            requireNonNullArgument( appendable, "appendable" ).append( str );
        }
    }   //  unescape()
}
//  class Entities

/*
 *  End of File
 */