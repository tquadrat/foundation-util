/*
 * ============================================================================
 * Copyright © 2002-2019 by Thomas Thrien.
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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.SystemUtils.getZoneIdAliasMap;

import java.io.Serial;
import java.time.DateTimeException;
import java.time.ZoneId;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link ZoneId}
 *  values.<br>
 *  <br>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link ZoneId#of(String, java.util.Map)}
 *  to retrieve a {@code ZoneId} based on the given value. The second parameter
 *  will be retrieved by a call to
 *  {@link org.tquadrat.foundation.util.SystemUtils#getZoneIdAliasMap()}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ZoneIdStringConverter.java 892 2021-04-03 18:07:28Z tquadrat $
 *  @since 0.0.6
 *
 *  @see org.tquadrat.foundation.util.SystemUtils#createZoneIdAliasMap()
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ZoneIdStringConverter.java 892 2021-04-03 18:07:28Z tquadrat $" )
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
            try
            {
                retValue = ZoneId.of( source.toString(), getZoneIdAliasMap() );
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