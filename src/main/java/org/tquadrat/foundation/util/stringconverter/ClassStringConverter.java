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
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.JavaUtils.getCallersClassLoader;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link Class}
 *  values.<br>
 *  <br>The method
 *  {@link #fromString(CharSequence)}
 *  will use
 *  {@link Class#forName(String, boolean, ClassLoader)}
 *  to load the class with the given name. This means that the conversion may
 *  fail even for an otherwise valid class name when the respective class is
 *  not on the CLASSPATH or otherwise loadable.<br>
 *  <br>It uses the
 *  {@link ClassLoader} that was used to load the caller for this method,
 *  and the {@code boolean} argument will be set to {@code false}, meaning that
 *  the class will not be initialised if not loaded previously.
 *
 *  @see Class#forName(String, boolean, ClassLoader)
 *  @see org.tquadrat.foundation.util.JavaUtils#getCallersClassLoader()
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ClassStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ClassStringConverter.java 1060 2023-09-24 19:21:40Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class ClassStringConverter implements StringConverter<Class<?>>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for the name of an unknown class on the command line:
     *  {@value}.
     */
    public static final String MSG_UnknownClass = "'%s' cannot be parsed to the name of a known Java class";

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
    public static final ClassStringConverter INSTANCE = new ClassStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code ClassStringConverter}.
     */
    public ClassStringConverter() { /* Just exists */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Class<?> fromString( final CharSequence source ) throws IllegalArgumentException
    {
        Class<?> retValue = null;
        if( nonNull( source ) )
        {
            if( isEmptyOrBlank( source ) ) throw new IllegalArgumentException( format( MSG_UnknownClass, source ) );
            try
            {
                final var classLoader = getCallersClassLoader();
                retValue = Class.forName( source.toString(), false, classLoader );
            }
            catch( final ClassNotFoundException e )
            {
                throw new IllegalArgumentException( format( MSG_UnknownClass, source ), e );
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
    public static final ClassStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final Class<?> source )
    {
        final var retValue = isNull( source) ? null : source.getName();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ClassStringConverter

/*
 *  End of File
 */