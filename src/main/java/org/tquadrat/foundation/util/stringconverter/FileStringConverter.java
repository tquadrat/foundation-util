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
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isEmpty;

import java.io.File;
import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link File}
 *  values.<br>
 *  <br>The file or folder that will be identified by the respective
 *  {@code File} object do not need to exist nor is it guaranteed that it can
 *  be accessed or create through the current user.<br>
 *  <br>File names will not be normalised or canonicalized.<br>
 *  <br>A file name of only blanks will be accepted as valid, while the empty
 *  String will cause an
 *  {@link IllegalArgumentException}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: FileStringConverter.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: FileStringConverter.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class FileStringConverter implements StringConverter<File>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid file name {@value}.
     */
    public static final String MSG_InvalidFileName = "'%s' cannot be parsed as a valid file name";

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
    public static final FileStringConverter INSTANCE = new FileStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final File fromString( final CharSequence source ) throws IllegalArgumentException
    {
        File retValue = null;
        if( nonNull( source ) )
        {
            if( isEmpty( source ) ) throw new IllegalArgumentException( format( MSG_InvalidFileName, source ) );
            retValue = new File( source.toString() );
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
    public static final FileStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final File source )
    {
        final var retValue = isNull( source ) ? null : source.getPath();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class FileStringConverter

/*
 *  End of File
 */