/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotBlankArgument;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.time.zone.ZoneRulesException;
import java.util.HashMap;
import java.util.Map;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  Additional helpers for the work with date/time values.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DateTimeUtils.java 1080 2024-01-03 11:05:21Z tquadrat $
 *  @since 0.3.0
 *
 *  @UMLGraph.link
 */
@UtilityClass
@ClassVersion( sourceVersion = "$Id: DateTimeUtils.java 1080 2024-01-03 11:05:21Z tquadrat $" )
@API( status = STABLE, since = "0.3.0" )
public final class DateTimeUtils
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The cached zone ids.
     */
    @SuppressWarnings( "StaticCollection" )
    private static final Map<String,ZoneId> m_ZoneIdCache = new HashMap<>();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private DateTimeUtils() { throw new PrivateConstructorForStaticClassCalledError( DateTimeUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Replaces the given instance of
     *  {@link ZoneId}
     *  by one from the cache.}</p>
     *
     *  @param  zoneId  The instance of {@code ZoneId} that needs to be
     *      replaced.
     *  @return The instance of {@code ZoneId} from the cache; this may be the
     *      same as the argument in case the zone id was not yet in the cache.
     *
     *  @see #retrieveCachedZoneId(String)
     */
    public static final ZoneId replaceByCachedZoneId( final ZoneId zoneId )
    {
        final ZoneId retValue;
        synchronized( m_ZoneIdCache )
        {
            retValue = m_ZoneIdCache.computeIfAbsent( requireNonNullArgument( zoneId, "zoneId" ).getId(), $ -> zoneId );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  replaceByCachedZoneId()

    /**
     *  <p>{@summary Retrieves a cached instance of
     *  {@link ZoneId}.}</p>
     *
     *  <p>Usually, each call to
     *  {@link ZoneId#of(String)}
     *  returns a new instance, even if the argument remains the same. This
     *  means that it cannot be assumed that</p>
     *  <pre><code>ZoneId.of( "UTC" ) == ZoneId.of( "UTC" )</code></pre>
     *  <p>returns {@code true} (although it cannot be excluded either).</p>
     *  <p>If an application uses {@code ZoneId}s a lot, this could cause
     *  significant memory pressure, so it would make sense to cache them.</p>
     *  <p>This is safe because the instances of {@code ZoneId} are immutable
     *  (the documentation says, they should be treated as
     *  <i>ValueBased</i>).</p>
     *  <p>As the number of distinct timezones is limited, there is no
     *  housekeeping for the cache itself.</p>
     *
     *  @note The id strings are case-sensitive!
     *
     *  @param  id  The id for the time zone.
     *  @return The instance of {@code ZoneId} for the given id.
     *  @throws DateTimeException   The given id has an invalid format.
     *  @throws ZoneRulesException  The given id is a region id that cannot be
     *      found.
     *
     *  @see <a href="https://stackoverflow.com/a/77660700/1554195">stackoverflow: &quot;Many instances of java.time.ZoneRegion in Java heap&quot;</a>
     */
    public static final ZoneId retrieveCachedZoneId( final String id ) throws DateTimeException, ZoneRulesException
    {
        final ZoneId retValue;
        synchronized( m_ZoneIdCache )
        {
            retValue = m_ZoneIdCache.computeIfAbsent( requireNotBlankArgument( id, "id" ), ZoneId::of );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrievedCachedZoneId()

    /**
     *  <p>{@summary Retrieves a cached instance of
     *  {@link ZoneId}
     *  using a map of aliases.}</p>
     *
     *  @param  id  The id for the time zone.
     *  @param  aliases A map of alias zone ids (typically abbreviations) to
     *      real zone ids.
     *  @return The instance of {@code ZoneId} for the given id.
     *  @throws DateTimeException   The given id has an invalid format.
     *  @throws ZoneRulesException  The given id is a region id that cannot be
     *      found.
     *
     *  @see #retrieveCachedZoneId(String)
     *  @see ZoneId#of(String,Map)
     */
    public static final ZoneId retrieveCachedZoneId( final String id, final Map<String,String> aliases ) throws DateTimeException, ZoneRulesException
    {
        final var zoneId = ZoneId.of( requireNonNullArgument( id, "id" ), requireNonNullArgument( aliases, "aliases" ) );
        final var retValue = replaceByCachedZoneId( zoneId );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveCacheZoneId()

    /**
     *  <p>{@summary Retrieves a cached instance of
     *  {@link ZoneId}
     *  from the given {@code temporal}.}</p>
     *
     *  @param  temporal    The temporal object.
     *  @return The instance of {@code ZoneId} for the temporal.
     *  @throws DateTimeException   The given temporal cannot be converted to a
     *      {@code ZoneId}.
     *
     *  @see #retrieveCachedZoneId(String)
     *  @see ZoneId#from(TemporalAccessor)
     */
    public static final ZoneId retrieveCachedZoneId( final TemporalAccessor temporal ) throws DateTimeException
    {
        final var zoneId = ZoneId.from( requireNonNullArgument( temporal, "temporal" ) );
        final var retValue = replaceByCachedZoneId( zoneId );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveCachedZoneId()

    /**
     *  <p>{@summary Retrieves a cached instance of
     *  {@link ZoneId}
     *  for the given offset.}</p>
     *
     *  @param  prefix  One of &quot;GMT&quot;, &quot;UTC&quot;, &quot;UT&quot;
     *      or the empty string.
     *  @param  offset  The offset.
     *  @return The instance of {@code ZoneId} for the arguments.
     *  @throws IllegalArgumentException    The prefix is not one of
     *      &quot;GMT&quot;, &quot;UTC&quot;, &quot;UT&quot; or the empty
     *      string.
     *
     *  @see #retrieveCachedZoneId(String)
     *  @see ZoneId#ofOffset(String, ZoneOffset)
     */
    public static final ZoneId retrieveCachedZoneId( final String prefix, final ZoneOffset offset ) throws IllegalArgumentException
    {
        final var zoneId = ZoneId.ofOffset( requireNonNullArgument( prefix, "prefix" ), requireNonNullArgument( offset, "offset" ) );
        final var retValue = replaceByCachedZoneId( zoneId );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveCachedZoneId()
}
//  class DateTimeUtils

/*
 *  End of File
 */