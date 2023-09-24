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
import static org.tquadrat.foundation.util.UniqueIdUtils.tsidFromString;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.util.TSID;

/**
 *  <p>{@summary An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link TSID}
 *  values.}</p>
 *  <p>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link org.tquadrat.foundation.util.UniqueIdUtils#tsidFromString(CharSequence)}
 *  to create a {@code TSID} instance based on the given value.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TSIDStringConverter.java 1037 2022-12-15 00:35:17Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: TSIDStringConverter.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class TSIDStringConverter implements StringConverter<TSID>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid TSID on the command line: {@value}.
     */
    public static final String MSG_InvalidTSIDFormat = "'%1$s' cannot be parsed as a valid TSID";

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
    public static final TSIDStringConverter INSTANCE = new TSIDStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code TSIDStringConverter}.
     */
    public TSIDStringConverter() { /* Just exists */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final TSID fromString( final CharSequence source ) throws IllegalArgumentException
    {
        TSID retValue = null;
        if( nonNull( source ) )
        {
            try
            {
                retValue = tsidFromString( source );
            }
            catch( final NullArgumentException e )
            {
                throw e;
            }
            catch( final IllegalArgumentException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidTSIDFormat, source ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    public static final TSIDStringConverter provider() { return INSTANCE; }
}
//  class TSIDStringConverter

/*
 *  End of File
 */