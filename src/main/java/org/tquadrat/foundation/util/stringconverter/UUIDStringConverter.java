/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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
import static org.tquadrat.foundation.util.UniqueIdUtils.uuidFromString;

import java.io.Serial;
import java.util.UUID;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link UUID}
 *  values.}</p>
 *  <p>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link org.tquadrat.foundation.util.UniqueIdUtils#uuidFromString(CharSequence)}
 *  to create a {@code UUID} instance based on the given value.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UUIDStringConverter.java 1039 2022-12-15 00:57:02Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: UUIDStringConverter.java 1039 2022-12-15 00:57:02Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class UUIDStringConverter implements StringConverter<UUID>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid UUID on the command line: {@value}.
     */
    public static final String MSG_InvalidUUIDFormat = "'%1$s' cannot be parsed as a valid UUID";

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
    public static final UUIDStringConverter INSTANCE = new UUIDStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code UUIDStringConverter}.
     */
    public UUIDStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final UUID fromString( final CharSequence source ) throws IllegalArgumentException
    {
        UUID retValue = null;
        if( nonNull( source ) )
        {
            try
            {
                retValue = uuidFromString( source );
            }
            catch( @SuppressWarnings( "OverlyBroadCatchBlock" ) final NullArgumentException e )
            {
                throw e;
            }
            catch( final IllegalArgumentException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidUUIDFormat, source ), e );
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
    public static final UUIDStringConverter provider() { return INSTANCE; }
}
//  class UUIDStringConverter

/*
 *  End of File
 */