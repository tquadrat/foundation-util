/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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
import static org.tquadrat.foundation.util.StringUtils.isEmpty;
import static org.tquadrat.foundation.util.stringconverter.FileStringConverter.MSG_InvalidFileName;

import java.io.Serial;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link Path}
 *  values.<br>
 *  <br>The method
 *  {@link #fromString(CharSequence)}
 *  uses
 *  {@link Path#of(String, String...)}
 *  to create the {@code Path} instance for the given value.<br>
 *  <br>The file or folder that will be identified by the respective
 *  {@code Path} object do not need to exist nor is it guaranteed that it can
 *  be accessed or create through the current user.<br>
 *  <br>Names will not be normalised or canonicalized.<br>
 *  <br>A path name of only blanks will be accepted as valid, while the empty
 *  String will cause an
 *  {@link IllegalArgumentException}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PathStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PathStringConverter.java 1032 2022-04-10 17:27:44Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class PathStringConverter implements StringConverter<Path>
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
    public static final PathStringConverter INSTANCE = new PathStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code PathStringConverter}.
     */
    public PathStringConverter() {}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public Path fromString( final CharSequence source ) throws IllegalArgumentException
    {
        Path retValue = null;
        if( nonNull( source ) )
        {
            if( isEmpty( source ) ) throw new IllegalArgumentException( format( MSG_InvalidFileName, source ) );
            try
            {
                retValue = Path.of( source.toString() );
            }
            catch( final InvalidPathException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidFileName, source ), e );
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
    @SuppressWarnings( "UseOfConcreteClass" )
    public static final PathStringConverter provider() { return INSTANCE; }
}
//  class PathStringConverter

/*
 *  End of File
 */