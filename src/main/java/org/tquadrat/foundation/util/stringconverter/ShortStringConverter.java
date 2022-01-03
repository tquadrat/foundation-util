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

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The implementation of
 *  {@link NumberStringConverter}
 *  for
 *  {@link java.lang.Short}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ShortStringConverter.java 897 2021-04-06 21:34:01Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ShortStringConverter.java 897 2021-04-06 21:34:01Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public final class ShortStringConverter extends NumberStringConverter<Short>
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
    public static final ShortStringConverter INSTANCE = new ShortStringConverter();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code ShortStringConverter}.
     */
    public ShortStringConverter() { super( short.class, Short.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Short parseNumber( final String value ) throws NumberFormatException
    {
        return Short.decode( value );
    }   //  parseNumber()
}
//  class ShortStringConverter

/*
 *  End of File
 */