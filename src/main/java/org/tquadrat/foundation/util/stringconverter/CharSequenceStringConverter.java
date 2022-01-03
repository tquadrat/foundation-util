/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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
import static org.tquadrat.foundation.lang.Objects.isNull;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary The implementation of
 *  {@link StringConverter}
 *  for
 *  {@link CharSequence}
 *  values.}</p>
 *  <p>Although a conversion from {@code CharSequence} to String is redundant
 *  (at best), this implementation exists for cases where a string converter is
 *  retrieved based on the data type; it can simplify the code when no special
 *  case must be considered for {@code CharSequence}.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CharSequenceStringConverter.java 897 2021-04-06 21:34:01Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CharSequenceStringConverter.java 897 2021-04-06 21:34:01Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class CharSequenceStringConverter implements StringConverter<CharSequence>
{
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
    public static final CharSequenceStringConverter INSTANCE = new CharSequenceStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final CharSequence fromString( final CharSequence source ) throws IllegalArgumentException
    {
        final var retValue = isNull( source ) ? null : source.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  Provides the subject class for this converter.
     *
     * @return The subject class.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<Class<CharSequence>> getSubjectClass() { return List.of( CharSequence.class ); }

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    public static final CharSequenceStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final CharSequence source )
    {
        final var retValue = isNull( source ) ? null : source.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class StringStringConverter

/*
 *  End of File
 */