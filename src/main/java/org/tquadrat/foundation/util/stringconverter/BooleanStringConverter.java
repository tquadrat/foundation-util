/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.stringconverter;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.isNull;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.MountPoint;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for {@code boolean} and
 *  {@link Boolean}
 *  values.<br>
 *  <br>The method
 *  {@link #fromString(CharSequence)}
 *  will accept the String &quot;true&quot;, irrespective of case, for the
 *  value {@code true}, and any other String for {@code false} (including the
 *  empty String!), while
 *  {@link #toString()}
 *  will only return &quot;true&quot; or &quot;false&quot; (or {@code null} if
 *  the input is {@code null}). This behaviour can be changed by providing
 *  different implementations for
 *  {@link #translate(CharSequence)}
 *  and
 *  {@link #toString(Boolean)}.
 *
 *  @see Boolean#valueOf(String)
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: BooleanStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: BooleanStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public class BooleanStringConverter implements StringConverter<Boolean>
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
    public static final BooleanStringConverter INSTANCE = new BooleanStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code BooleanStringConverter}.
     */
    public BooleanStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public Boolean fromString( final CharSequence source )
    {
        final var translated = translate( source );
        final var retValue = isNull( translated ) ? null : Boolean.valueOf( translated.toString() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  Provides the subject class for this converter.
     *
     * @return The subject class.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<Class<?>> getSubjectClass() { return List.of( boolean.class, Boolean.class ); }

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link org.tquadrat.foundation.lang.StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final BooleanStringConverter provider() { return INSTANCE; }

    /**
     * {@inheritDoc}
     */
    @MountPoint
    @Override
    public String toString( final Boolean source )
    {
        final var retValue = isNull( source ) ? null : source.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  Translates the given String to either &quot;true&quot; or
     *  &quot;false&quot;. The default implementation just returns the
     *  argument.<br>
     *  <br>If this method will be implemented differently, it is no longer
     *  guaranteed that
     *  <pre><code>toString( fromString( s ) ) == s</code></pre>
     *  yields {@code true} for all {@code s}.
     *
     *  @param  source  The original text; can be {@code null}.
     *  @return The translated source; can be {@code null} if {@code source}
     *      was already {@code null}.
     */
    @SuppressWarnings( "static-method" )
    @MountPoint
    protected CharSequence translate( final CharSequence source ) { return source; }
}
//  class BooleanStringConverter

/*
 *  End of File
 */