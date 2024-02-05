/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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

package org.tquadrat.foundation.util.stringconverter;

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.DateTimeUtils.getZoneIdAliasMap;
import static org.tquadrat.foundation.util.DateTimeUtils.retrieveCachedZoneId;

import java.io.Serial;
import java.time.DateTimeException;
import java.time.ZoneId;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link ZoneId}
 *  values.}</p>
 *  <p>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link org.tquadrat.foundation.util.DateTimeUtils#retrieveCachedZoneId(String,java.util.Map)}
 *  to retrieve a {@code ZoneId} based on the given value. The second parameter
 *  will be retrieved by a call to
 *  {@link org.tquadrat.foundation.util.DateTimeUtils#getZoneIdAliasMap()}.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ZoneIdStringConverter.java 1091 2024-01-25 23:10:08Z tquadrat $
 *  @since 0.0.6
 *
 *  @see org.tquadrat.foundation.util.SystemUtils#createZoneIdAliasMap()
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ZoneIdStringConverter.java 1091 2024-01-25 23:10:08Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class ZoneIdStringConverter implements StringConverter<ZoneId>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid zone id on the command line: {@value}.
     */
    public static final String MSG_InvalidZoneId = "'%1$s' cannot be parsed as a valid time zone id";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *  An instance of this class.
     */
    public static final ZoneIdStringConverter INSTANCE = new ZoneIdStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code ZoneIdStringConverter}.
     */
    public ZoneIdStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final ZoneId fromString( final CharSequence source ) throws IllegalArgumentException
    {
        ZoneId retValue = null;
        if( nonNull( source ) )
        {
            //noinspection OverlyBroadCatchBlock
            try
            {
                retValue = retrieveCachedZoneId( source.toString(), getZoneIdAliasMap() );
            }
            catch( final DateTimeException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidZoneId, source ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link org.tquadrat.foundation.lang.StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    public static final ZoneIdStringConverter provider() { return INSTANCE; }
}
//  class ZoneIdStringConverter

/*
 *  End of File
 */